package slanzini.zookeeper.demo.exception;

public class ClusterSynchronizationException extends Exception{
    private static final long serialVersionUID = -5081219364553577082L;

    public ClusterSynchronizationException() {
        super();
    }

    public ClusterSynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterSynchronizationException(String message) {
        super(message);
    }
}
