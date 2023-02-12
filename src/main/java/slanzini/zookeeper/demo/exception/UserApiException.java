package slanzini.zookeeper.demo.exception;

public class UserApiException extends Exception{
    private static final long serialVersionUID = -4081219364553577082L;

    public UserApiException() {
        super();
    }

    public UserApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserApiException(String message) {
        super(message);
    }
}
