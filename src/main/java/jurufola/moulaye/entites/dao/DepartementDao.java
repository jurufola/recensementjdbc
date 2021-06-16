package jurufola.moulaye.entites.dao;

import jurufola.moulaye.entites.Departement;
import jurufola.moulaye.entites.Region;
import jurufola.moulaye.entites.Ville;
import jurufola.moulaye.utils.ConfigDatabase;
import jurufola.moulaye.utils.Database;
import org.mariadb.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartementDao {
    public List<Departement> extraire(Connection connection){
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        //ResultSet resultSet2 = null;
        PreparedStatement preparedStatement = null;
        List<Departement> departements = new ArrayList<>();
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select (departement.id, departement.code, id_region, region.code, nom ) from departement, region where departement.id_region = region.id");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("departement.id");
                String code = resultSet.getString("departement.code");
                int idRegion = resultSet.getInt("id_region");
               /*preparedStatement = connection.prepareStatement("select nom, code from region where id = ?");
                preparedStatement.setInt(1, idRegion);*/
                //resultSet2 = preparedStatement.executeQuery();
                //resultSet2.next();
                String codeRegion = resultSet.getString("region.code");
                String nomRegion = resultSet.getString("nom");

                departements.add(new Departement(id, code, new Region(idRegion, codeRegion, nomRegion)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                /*if (resultSet2 != null) {
                    resultSet2.close();
                }*/
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }

        return departements;
    }

    public void insert(Departement departement, Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            //int id = departement.getId();
            String code = departement.getCode();
            Region region = departement.getRegion();
            preparedStatement = connection.prepareStatement("insert into departement values(?, ?)");
           // preparedStatement.setInt(1,id);
            preparedStatement.setString(1,code);
            preparedStatement.setInt(2,region.getId());
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Doublon interdit car le département " + code + " existe déjà");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
    }

    public void insert(String code, int idRegion, Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

            preparedStatement = connection.prepareStatement("insert into departement(code, id_region) values(?, ?)");
            preparedStatement.setString(1, code);
            preparedStatement.setInt(2, idRegion);
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Doublon interdit car le département " + code + " existe déjà");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
    }

    public int update(String ancienCode, int ancienIdRegion, String nouveauCode, int nouveauIdRegion, Connection connection){

        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("update departement set code = ? , id_region = ? where code = ? And id_region = ?");
            preparedStatement.setString(1, nouveauCode);
            preparedStatement.setInt(2,nouveauIdRegion);
            preparedStatement.setString(3,ancienCode);
            preparedStatement.setInt(4,ancienIdRegion);
            try {
                nbLignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Doublon interdit car le département " + nouveauCode + " existe déjà");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return nbLignes;
    }

    public boolean delete(Departement departement, Connection connection){

        //D'abord supprimer toutes les villes appartenant au departement
        VilleDao villeDao = new VilleDao();
        List<Ville> villes = villeDao.extraireParIdDepartement(departement.getId(), connection);
        for (Ville ville : villes) {
            villeDao.delete(ville, connection);
        }
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            int id = departement.getId();
            preparedStatement = connection.prepareStatement("delete from departement where id = ?");
            preparedStatement.setInt(1,id);
            nbLignes = preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return nbLignes == 0 ? false : true;
    }

    public Departement extraireParId(int id, Connection connection) {
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Departement departement = null;
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from departement where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();


            while(resultSet.next()){
                int idDepartement = resultSet.getInt("id");
                String codeDepartement = resultSet.getString("code");
                int idRegion = resultSet.getInt("id_region");
                RegionDao regionDao = new RegionDao();
                Region region = regionDao.extraireParId(idRegion, connection);
                departement = new Departement(idDepartement, codeDepartement, region);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departement;
    }

    public Departement extraireParCode(String codeDepartement, Connection connection) {
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Departement departement = null;
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from departement where code = ?");
            preparedStatement.setString(1, codeDepartement);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int idDepartement = resultSet.getInt("id");
                int idRegion = resultSet.getInt("id_region");
                RegionDao regionDao = new RegionDao();
                Region region = regionDao.extraireParId(idRegion, connection);
                departement = new Departement(idDepartement, codeDepartement, region);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departement;
    }

    public List<Departement> extraireParIdRegion(int idRegion, Connection connection) {
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Departement> departements = new ArrayList<>();
        RegionDao regionDao = new RegionDao();
        Region region = regionDao.extraireParId(idRegion, connection);
        try {
            preparedStatement = connection.prepareStatement("select * from departement where id_region = ?");
            preparedStatement.setInt(1, idRegion);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int idDepartement = resultSet.getInt("id");
                String codeDepartement = resultSet.getString("code");
                departements.add(new Departement(idDepartement, codeDepartement, region));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                /*if (connection != null) {
                    connection.close();
                }*/
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departements;
    }
}
