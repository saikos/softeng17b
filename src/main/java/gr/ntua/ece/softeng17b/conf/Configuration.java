package gr.ntua.ece.softeng17b.conf;

import gr.ntua.ece.softeng17b.data.DataAccess;

import java.util.Properties;
import java.util.Set;

public class Configuration {

    private static final Configuration self = new Configuration();

    private String contextPath = null;
    private Properties props = new Properties();
    private DataAccess dataAccess = new DataAccess();

    private Configuration() {

    }

    public static Configuration getInstance() {
        return self;
    }

    void setup(String contextPath, Properties props) throws ConfigurationException{
        this.contextPath = contextPath;
        this.props = props;
        try {
            dataAccess.setup(
                getProperty("db.driver"),
                getProperty("db.url"),
                getProperty("db.user"),
                getProperty("db.pass")
            );
        }
        catch(Exception e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getProperty(String name) {
        return getProperty(name, null);
    }

    public String getProperty(String name, String defaultValue) {
        return props.getProperty(name, defaultValue);
    }

    public Set<String> propertyNames() {
        return props.stringPropertyNames();
    }

    public DataAccess getDataAccess() {
        return dataAccess;
    }
}
