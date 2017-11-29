package gr.ntua.ece.softeng17b.conf;

public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
