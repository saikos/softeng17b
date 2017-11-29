package gr.ntua.ece.softeng17b.conf;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileReader;
import java.util.Properties;

public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext ctx = servletContextEvent.getServletContext();

            Properties props = new Properties();

            String pathToPropertiesInWebApp = ctx.getInitParameter("properties");
            String pathToPropertiesInFileSystem = ctx.getRealPath(pathToPropertiesInWebApp);

            props.load(new FileReader(pathToPropertiesInFileSystem));

            Configuration.getInstance().setup(ctx.getContextPath(), props);
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
