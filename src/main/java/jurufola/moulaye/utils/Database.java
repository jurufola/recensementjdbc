package jurufola.moulaye.utils;

/**
 * Classe representant les infos de connection à la BDD
 * @author juruf_000
 */
public class Database {
    private String url;
    private String user;
    private String pwd;

    /**
     * Constructeur
     * @param url
     * @param user
     * @param pwd
     */
    public Database(String url, String user, String pwd) {
        this.url = url;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * getteur url
     * @return url de la BDD
     */
    public String getUrl() {
        return url;
    }

    /**
     * getteur utilisateur
     * @return utilisateur de la BDD
     */
    public String getUser() {
        return user;
    }

    /**
     * getteur mot de passe
     * @return mote de passe de de l'utilisateur de la BDD
     */
    public String getPwd() {
        return pwd;
    }


}
