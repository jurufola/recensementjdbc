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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;
import java.util.List;

public class IntegrationRecensement {
    public static void main(String[] args) {


        Path path = Paths.get("C:\\Users\\juruf_000\\Documents\\Formation Java\\13 - JDBC\\TP\\recensement population.csv");
        System.out.println(path);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            Iterator<String> iterator = lines.iterator();
            System.out.println(iterator.next()); // Pour lire la première ligne
            RegionDao regionDao = new RegionDao();
            DepartementDao departementDao = new DepartementDao();
            VilleDao villeDao = new VilleDao();
            //Connexion BDD
            Connection connection = null;
            try {
                DriverManager.registerDriver(new Driver());
                Database db = ConfigDatabase.extractConfig();
                connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
                /*while (iterator.hasNext()) {
                    String line = iterator.next();
                    String[] tabLine = line.split(";");
                    String codeRegion = tabLine[0];
                    String nomRegion = tabLine[1];
                    String codeDepartement = tabLine[2];
                    int codeCommune = Integer.parseInt(tabLine[5]);
                    String nomCommune = tabLine[6];
                    System.out.println(codeDepartement + " / " + codeCommune);
                    int  populationCommune = Integer.parseInt(tabLine[9].replaceAll(" ", ""));
                    //System.out.println( codeRegion + "  " + nomRegion + "  " + codeDepartement + "  " + codeCommune + "  " + nomCommune + "  " + populationCommune);
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

                }*/

                Region corse = regionDao.extraireParId(59, connection);
                regionDao.delete(corse, connection);
                /*List<Ville> villesCorses2A = villeDao.extraireParIdDepartement(161, connection);
                for (Ville ville : villesCorses2A) {
                    System.out.println(ville);
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
