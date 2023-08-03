package co.com.ies.smol.domain.core.error;

public class ControlTxException extends Exception {

    public static final String BOARD_NOT_FOUND = "Board not found";
    public static final String BOARD_IS_NOT_IN_STOCK = "Board is not in stock";
    public static final String BOARD_ASSIGNED = "Board assigned";

    public static final String IVALID_STATUS = "The card cannot return to the state stock";
    public static final String CAN_NOT_CHANGE_TO_STOCK = "can not change to stock";
    public static final String SAME_STATE = "the board already has that status";
    public static final String BOARD_LOW = "can not change because board is low";
    public static final String CAN_NOT_CHANGE_OF_STATE = "can not change of stock to another state";

    public ControlTxException() {
        super("Error - ControlTxException ");
    }

    public ControlTxException(String msg) {
        super(msg);
    }
}
