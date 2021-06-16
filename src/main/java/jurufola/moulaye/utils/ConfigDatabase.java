package jurufola.moulaye.utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigDatabase {
    public static Database extractConfig(){
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties("database.properties");
            String databaseUrl = config.getString("database.url");
            String databaseUser = config.getString("database.user");
            String databasePwd = config.getString("database.password");
            return new Database(databaseUrl, databaseUser, databasePwd);

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
