package co.com.ies.smol.service.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.util.List;

public interface ControlTxService {
    void createBoardRegister(BoardRegisterDTO boardRegisterDTO);

    void assignInterfaceBoard(AssignBoardDTO assignBoardDTO) throws ControlTxException;

    List<InterfaceBoardDTO> getInterfaceBoardByBrand(String brandName);

    Long getCountInterfaceBoardByBrand(String brandName);

    void statusChangeBoard(String mac, StatusInterfaceBoard state) throws ControlTxException;
    List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException;

    Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException;
}
