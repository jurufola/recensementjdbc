package jurufola.moulaye.entites.dao;


import jurufola.moulaye.entites.Departement;
import jurufola.moulaye.entites.Region;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Region
 * @author juruf_000
 */
public class RegionDao {
    /**
     * Extrait toules region de la BDD
     * @param connection
     * @return List<{@link Region}> une liste de régions
     */
    public List<Region> extraire(Connection connection){

        //Init objets Resultset et PreparedStatement
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Region> regions = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select * from region");
            resultSet = preparedStatement.executeQuery();
            //Constitution List des regions extraites
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String nom = resultSet.getString("nom");
                regions.add(new Region(id, code, nom));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // Fermetures ressoursses
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }

        return regions;
    }

    /**
     * Insert une region dans la BDD à partir de l'objet Region
     * @param region
     * @param connection
     */
    public void insert(Region region, Connection connection) {
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        //Connexion BDD
        try{
            int id = region.getId();
            String code = region.getCode();
            String nom = region.getNom();
            preparedStatement = connection.prepareStatement("insert into region values(?, ?, ?)");
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,code);
            preparedStatement.setString(3,nom);
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){ //// colonnes code et nom ont des contrainte d'unicité au niveau de la table
                System.out.println("Doublon interdit car la region " + nom + " existe déjà");
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

    /**
     * Insert une région dans la BDD à partir du code et du nom de la région
     * @param code
     * @param nom
     * @param connection
     */
    public void insert(String code, String nom, Connection connection) {
        //Iniitalisation objet PreparedStatement
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("insert into region(code, nom) values(?, ?)");
            preparedStatement.setString(1,code);
            preparedStatement.setString(2,nom);
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){// colonnes code et nom ont des contrainte d'unicité au niveau de la table
                System.out.println("Doublon interdit car la region " + nom + " existe déjà");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }

    }

    public int update(String ancienCode, String ancienNom, String nouveauCode, String nouveauNom, Connection connection){
       // Connection connection = null;
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("update region set code = ? , nom = ? where code = ? And nom = ?");
            preparedStatement.setString(1, nouveauCode);
            preparedStatement.setString(2,nouveauNom);
            preparedStatement.setString(3,ancienCode);
            preparedStatement.setString(4,ancienNom);
            try {
                 nbLignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Doublon interdit car la region " + nouveauNom + " existe déjà");
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

    public boolean delete(Region region, Connection connection) {
        //Supprimer d'abord tous les départements liés
        DepartementDao departementDao = new DepartementDao();
        List<Departement> departements = departementDao.extraireParIdRegion(region.getId(), connection);
        for (Departement departement : departements) {
            departementDao.delete(departement, connection);
        }

        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        try {

            int id = region.getId();
            preparedStatement = connection.prepareStatement("delete from region where id = ?");
            preparedStatement.setInt(1,id);
            nbLignes = preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return nbLignes == 0 ? false : true;
    }

    public Region extraireParId(int idRegion, Connection connection) {
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Region region = null;
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from region where id = ?");
            preparedStatement.setInt(1, idRegion);
            resultSet = preparedStatement.executeQuery();


            while(resultSet.next()){

                String codeRegion = resultSet.getString("code");
                String nomRegion = resultSet.getString("nom");
                region = new Region(idRegion, codeRegion, nomRegion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }try {
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
        return region;
    }

    public Region extraireParNom(String nomRegion, Connection connection) {
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Region region = null;
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from region where nom = ?");
            preparedStatement.setString(1, nomRegion);
            resultSet = preparedStatement.executeQuery();


            while(resultSet.next()){
                int idRegion = resultSet.getInt("id");
                String codeRegion = resultSet.getString("code");

                region = new Region(idRegion, codeRegion, nomRegion);
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
        return region;
    }
}
