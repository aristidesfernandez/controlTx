package co.com.ies.smol.service.core.impl;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.core.ControlTxDomainImpl;
import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.ContractService;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.DataSheetInterfaceService;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.web.rest.core.ControlTxController;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ControlTxServiceImpl extends ControlTxDomainImpl implements ControlTxService {

    private final Logger log = LoggerFactory.getLogger(ControlTxController.class);

    private final DataSheetInterfaceService dataSheetInterfaceService;
    private final InterfaceBoardService interfaceBoardService;
    private final ControlInterfaceBoardService controlInterfaceBoardService;
    private final ContractService contractService;
    private final OperatorService operatorService;

    public ControlTxServiceImpl(
        DataSheetInterfaceService dataSheetInterfaceService,
        InterfaceBoardService interfaceBoardService,
        ControlInterfaceBoardService controlInterfaceBoardService,
        ContractService contractService,
        OperatorService operatorService
    ) {
        this.dataSheetInterfaceService = dataSheetInterfaceService;
        this.interfaceBoardService = interfaceBoardService;
        this.controlInterfaceBoardService = controlInterfaceBoardService;
        this.contractService = contractService;
        this.operatorService = operatorService;
    }

    @Override
    public void createBoardRegister(BoardRegisterDTO boardRegisterDTO) {
        List<String> macs = boardRegisterDTO.getMacs();
        DataSheetInterfaceDTO dataSheetInterfaceDTO = createDataSheetInterface(boardRegisterDTO);
        dataSheetInterfaceDTO = dataSheetInterfaceService.save(dataSheetInterfaceDTO);

        for (String mac : macs) {
            InterfaceBoardDTO interfaceBoardDTO = createInterfaceBoard(mac, dataSheetInterfaceDTO);

            interfaceBoardDTO = interfaceBoardService.save(interfaceBoardDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = createControlInterfaceBoard(
                Location.IES,
                StatusInterfaceBoard.STOCK,
                interfaceBoardDTO,
                null
            );
            controlInterfaceBoardDTO = controlInterfaceBoardService.save(controlInterfaceBoardDTO);

            log.info(
                "dataSheetInterfaceDTO ID {}  - interfaceBoardDTO ID {} - controlInterfaceBoardDTO ID {} ",
                dataSheetInterfaceDTO.getId(),
                interfaceBoardDTO.getId(),
                controlInterfaceBoardDTO.getId()
            );
        }
    }

    protected DataSheetInterfaceDTO createDataSheetInterface(BoardRegisterDTO boardRegisterDTO) {
        DataSheetInterfaceDTO dataSheetInterfaceDTO = new DataSheetInterfaceDTO();
        dataSheetInterfaceDTO.setColcircuitosLotNumber(boardRegisterDTO.getColcircuitosLotNumber());
        dataSheetInterfaceDTO.setOrderAmount(boardRegisterDTO.getOrderAmount());
        dataSheetInterfaceDTO.setAmountReceived(boardRegisterDTO.getAmountReceived());
        dataSheetInterfaceDTO.setRemission(boardRegisterDTO.getRemission());
        dataSheetInterfaceDTO.setEntryDate(ZonedDateTime.now());
        dataSheetInterfaceDTO.setIesOrderNumber(boardRegisterDTO.getIesOrderNumber());

        return dataSheetInterfaceDTO;
    }

    protected InterfaceBoardDTO createInterfaceBoard(String mac, DataSheetInterfaceDTO dataSheetInterfaceDTO) {
        InterfaceBoardDTO interfaceBoardDTO = new InterfaceBoardDTO();
        interfaceBoardDTO.setMac(mac);
        interfaceBoardDTO.setDataSheetInterface(dataSheetInterfaceDTO);

        return interfaceBoardDTO;
    }

    protected ControlInterfaceBoardDTO createControlInterfaceBoard(
        Location location,
        StatusInterfaceBoard status,
        InterfaceBoardDTO interfaceBoardDTO,
        ContractDTO contract
    ) {
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = new ControlInterfaceBoardDTO();
        controlInterfaceBoardDTO.setLocation(location);
        controlInterfaceBoardDTO.setState(status);
        controlInterfaceBoardDTO.setStartTime(ZonedDateTime.now());
        controlInterfaceBoardDTO.setInterfaceBoard(interfaceBoardDTO);
        controlInterfaceBoardDTO.setContract(contract);

        return controlInterfaceBoardDTO;
    }

    @Transactional
    @Override
    public void assignInterfaceBoard(AssignBoardDTO assignBoardDTO) throws ControlTxException {
        List<String> macs = assignBoardDTO.getMacs();
        String reference = assignBoardDTO.getReference();

        for (String mac : macs) {
            Optional<InterfaceBoardDTO> oInterfaceBoardDTO = interfaceBoardService.getInterfaceBoardByMac(mac);

            InterfaceBoardDTO interfaceBoardDTO = validateExistingInterfaceBoard(oInterfaceBoardDTO);
            InterfaceBoard interfaceBoard = interfaceBoardService.toEntity(interfaceBoardDTO);

            Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO = controlInterfaceBoardService.getControlInterfaceBoardByInterfaceBoard(
                interfaceBoard
            );

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = validateExistingBoardControl(oControlInterfaceBoardDTO);

            Optional<ContractDTO> oContract = Optional.empty(); //contractService.getContractByReference(reference);
            ContractDTO contract = new ContractDTO(); // validateExistingContract(oContract);

            controlInterfaceBoardDTO.setFinishTime(ZonedDateTime.now());
            controlInterfaceBoardService.save(controlInterfaceBoardDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardNewDTO = createControlInterfaceBoard(
                Location.CLIENT,
                StatusInterfaceBoard.OPERATION,
                interfaceBoardDTO,
                contract
            );
            controlInterfaceBoardService.save(controlInterfaceBoardNewDTO);
        }
    }

    @Override
    public List<InterfaceBoardDTO> getInterfaceBoardByBrand(String brandName) {
        List<Operator> operators = operatorService.findAllOperatorsByBrandName(brandName);
        List<ContractDTO> contractList = contractService.findAllContractByOpeatorIn(operators);
        List<Long> contractIds = contractList.stream().map(ContractDTO::getId).toList();

        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractIds(
            contractIds
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByBrand(String brandName) {
        List<Operator> operators = operatorService.findAllOperatorsByBrandName(brandName);
        List<ContractDTO> contractList = contractService.findAllContractByOpeatorIn(operators);

        return contractList.stream().mapToLong(ContractDTO::getNumberInterfaceBoard).sum();
    }

    @Override
    public void statusChangeBoard(String mac, StatusInterfaceBoard state) throws ControlTxException {
        Optional<InterfaceBoardDTO> oInterfaceBoardDTO = interfaceBoardService.getInterfaceBoardByMac(mac);
        System.out.println("------------ ");
        InterfaceBoardDTO interfaceBoardDTO = validateExistingInterfaceBoard(oInterfaceBoardDTO);
        InterfaceBoard interfaceBoard = interfaceBoardService.toEntity(interfaceBoardDTO);

        Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO = controlInterfaceBoardService.getControlInterfaceBoardByInterfaceBoard(
            interfaceBoard
        );

        ControlInterfaceBoardDTO controlInterfaceBoardDTO = validateChangeState(oControlInterfaceBoardDTO, interfaceBoardDTO, state);

        ContractDTO contract = !state.equals(StatusInterfaceBoard.STOCK) ? controlInterfaceBoardDTO.getContract() : null;

        controlInterfaceBoardDTO.setFinishTime(ZonedDateTime.now());
        controlInterfaceBoardService.save(controlInterfaceBoardDTO);

        Location location = getLocationFromState(state);

        ControlInterfaceBoardDTO controlInterfaceBoardNewDTO = createControlInterfaceBoard(location, state, interfaceBoardDTO, contract);
        controlInterfaceBoardService.save(controlInterfaceBoardNewDTO);
    }

    private Location getLocationFromState(StatusInterfaceBoard state) {
        Location location = Location.IES;

        switch (state) {
            case OPERATION:
                location = Location.CLIENT;
                break;
            case STOCK:
                location = Location.IES;
                break;
            case IN_TRANSFER:
                location = Location.CLIENT;
                break;
            case REPAIR:
                location = Location.IES;
                break;
            case WARRANTY:
                location = Location.IES;
                break;
        }
        return location;
    }

    public List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException {
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReference(
            reference
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException {
        List<ContractDTO> contractList = contractService.getContractByReference(reference);
        validateExistingContract(contractList);

        return contractList.stream().mapToLong(ContractDTO::getNumberInterfaceBoard).sum();
    }
}
