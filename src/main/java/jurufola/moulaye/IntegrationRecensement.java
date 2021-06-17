package jurufola.moulaye;

import jurufola.moulaye.entites.Departement;
import jurufola.moulaye.entites.Region;
import jurufola.moulaye.entites.Ville;
import jurufola.moulaye.entites.dao.DepartementDao;
import jurufola.moulaye.entites.dao.RegionDao;
import jurufola.moulaye.entites.dao.VilleDao;
import jurufola.moulaye.utils.ConfigDatabase;
import jurufola.moulaye.utils.Database;
import org.mariadb.jdbc.Driver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Classe exécutable permettant intégration du fichier recensement.csv dans la BDD
 * @author juruf_000
 */
public class IntegrationRecensement {
    public static void main(String[] args) {

        //On recupère le chemin du fichier
        Path path = Paths.get("C:\\Users\\juruf_000\\Documents\\Formation Java\\13 - JDBC\\TP\\recensement population.csv");

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);// On lie toules les lignes du fichier
            Iterator<String> iterator = lines.iterator();
            System.out.println(iterator.next()); // Pour lire la première ligne et enlever l'entête
            RegionDao regionDao = new RegionDao();
            DepartementDao departementDao = new DepartementDao();
            VilleDao villeDao = new VilleDao();

            Connection connection = null;
            try {
                //Connexion BDD
                DriverManager.registerDriver(new Driver());
                Database db = ConfigDatabase.extractConfig();
                connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

                //Integration Fichier recensement.csv
               while (iterator.hasNext()) {
                    String line = iterator.next();
                    String[] tabLine = line.split(";");
                    String codeRegion = tabLine[0];
                    String nomRegion = tabLine[1];
                    String codeDepartement = tabLine[2];
                    int codeCommune = Integer.parseInt(tabLine[5]);
                    String nomCommune = tabLine[6];
                    System.out.println(codeDepartement + " / " + codeCommune);
                    int  populationCommune = Integer.parseInt(tabLine[9].replaceAll(" ", ""));
                    Region region = new Region(0, codeRegion, nomRegion);
                    //regionDao.insert(codeRegion, nomRegion);
                    Region regionBase = regionDao.extraireParNom(nomRegion, connection);
                    if(regionBase == null) {
                        regionDao.insert(codeRegion, nomRegion, connection);
                        regionBase = regionDao.extraireParNom(nomRegion, connection);
                    }
                    region.setId(regionBase.getId());
                    Departement departement = new Departement(0, codeDepartement, region);
                    Departement departementBase = departementDao.extraireParCode(codeDepartement, connection);
                    if (departementBase == null){
                        departementDao.insert(codeDepartement, region.getId(), connection);
                        departementBase = departementDao.extraireParCode(codeDepartement, connection);
                    }
                    departement.setId(departementBase.getId());

                    Ville ville = new Ville(0, codeCommune, nomCommune, populationCommune, departement, region);
                    villeDao.insert(codeCommune, nomCommune, populationCommune, departement.getId(), regionBase.getId(), connection);

                }

                //drop de la BDD
                /*List<Region> regions = regionDao.extraire(connection);

                for (Region region : regions) {
                    regionDao.delete(region, connection);
                }*/

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Problème de fermeture des ressources :" + e.getMessage());
                }
            }

            System.out.println(lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
