package co.com.ies.smol.domain.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import java.util.Optional;

public abstract class ControlTxDomainImpl {

    public ControlInterfaceBoardDTO validateExistingBoardControl(Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO)
        throws ControlTxException {
        if (oControlInterfaceBoardDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.BOARD_NOT_FOUND);
        }
        ControlInterfaceBoardDTO controlBoardExists = oControlInterfaceBoardDTO.get();
        if ("OPERATION".equals(controlBoardExists.getState().name())) {
            throw new ControlTxException(ControlTxException.BOARD_ASSIGNED);
        }
        return controlBoardExists;
    }

    public InterfaceBoardDTO validateExistingInterfaceBoard(Optional<InterfaceBoardDTO> oInterfaceBoardDTO) throws ControlTxException {
        if (oInterfaceBoardDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.BOARD_NOT_FOUND);
        }
        return oInterfaceBoardDTO.get();
    }

    public ControlInterfaceBoardDTO validateChangeState(
        Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO,
        InterfaceBoardDTO interfaceBoard,
        StatusInterfaceBoard state
    ) throws ControlTxException {
        if (oControlInterfaceBoardDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.BOARD_NOT_FOUND);
        }
        ControlInterfaceBoardDTO controlBoardExists = oControlInterfaceBoardDTO.get();

        StatusInterfaceBoard currentState = controlBoardExists.getState();
        System.out.println("-------------------- estado actual " + currentState);

        if (currentState.equals(StatusInterfaceBoard.LOW)) {
            throw new ControlTxException(ControlTxException.BOARD_LOW);
        }
        if (currentState.equals(state)) {
            throw new ControlTxException(ControlTxException.SAME_STATE);
        }

        if (currentState.equals(StatusInterfaceBoard.STOCK)) {
            throw new ControlTxException(ControlTxException.CAN_NOT_CHANGE_OF_STATE);
        }

        return controlBoardExists;
    }
}
