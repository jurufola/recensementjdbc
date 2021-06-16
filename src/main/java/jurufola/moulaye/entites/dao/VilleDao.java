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

public class VilleDao {
    public List<Ville> extraire(Connection connection){
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Ville> villes = new ArrayList<>();
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from ville");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int code = resultSet.getInt("codeCommune");
                String nom = resultSet.getString("nom");
                int population = resultSet.getInt("populationTotale");
                int idDepartement = resultSet.getInt("id_departement");
                int idRegion = resultSet.getInt("id_region");
                DepartementDao departementDao = new DepartementDao();
                Departement departement = departementDao.extraireParId(idDepartement, connection);
                RegionDao regionDao = new RegionDao();
                Region region = regionDao.extraireParId(idRegion, connection);
                villes.add(new Ville(id, code, nom, population, departement, region));
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

        return villes;
    }

    public List<Ville> extraireParIdDepartement(int idDepartement, Connection connection){
        // Connection bdd
        //Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Ville> villes = new ArrayList<>();
        try {
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            preparedStatement = connection.prepareStatement("select * from ville where id_departement = ?");
            preparedStatement.setInt(1, idDepartement);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int code = resultSet.getInt("codeCommune");
                String nom = resultSet.getString("nom");
                int population = resultSet.getInt("populationTotale");
                int idRegion = resultSet.getInt("id_region");
                DepartementDao departementDao = new DepartementDao();
                Departement departement = departementDao.extraireParId(idDepartement, connection);
                RegionDao regionDao = new RegionDao();
                Region region = regionDao.extraireParId(idRegion, connection);
                villes.add(new Ville(id, code, nom, population, departement, region));
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

        return villes;
    }

    public void insert(Ville ville, Connection connection){
        //On verifie d'abord la presence de la ville dans la BDD avant toute insertion
        if (verifVillePresent(ville, connection)==false){
            //Connection connection = null;
            PreparedStatement preparedStatement = null;
            //Connexion BDD
            try{
                //DriverManager.registerDriver(new Driver());
                //Database db = ConfigDatabase.extractConfig();
                //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
                int id = ville.getId();
                int code = ville.getCodeCommune();
                String nom = ville.getNom();
                int population = ville.getPopulationTotale();
                Departement departement = ville.getDepartement();
                Region region = ville.getRegion();

                preparedStatement = connection.prepareStatement("insert into ville values(?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1,id);
                preparedStatement.setInt(2,code);
                preparedStatement.setString(3,nom);
                preparedStatement.setInt(4,population);
                preparedStatement.setInt(5,departement.getId());
                preparedStatement.setInt(6,region.getId());
                try {
                    int nblignes = preparedStatement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException e){
                    //e.printStackTrace();
                    System.out.println("Doublon interdit car la commune  " + ville + " existe déjà");
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
        }else {
            System.out.println("La ville " + ville.getNom() + " est déjà présente dans la base");
        }
    }

    public void insert(int codeCommune, String nom, int populationTotale, int idDepartement, int idRegion, Connection connection){
        //On verifie d'abord la presence de la ville dans la BDD avant toute insertion
        if (verifVillePresent(codeCommune, nom, populationTotale, idDepartement, idRegion, connection) == false ){
            //Connection connection = null;
            PreparedStatement preparedStatement = null;
            //Connexion BDD
            try{
                //DriverManager.registerDriver(new Driver());
                //Database db = ConfigDatabase.extractConfig();
                //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

                preparedStatement = connection.prepareStatement("insert into ville(codeCommune, nom, populationTotale, id_departement, id_region) values(?, ?, ?, ?, ?)");
                preparedStatement.setInt(1,codeCommune);
                preparedStatement.setString(2,nom);
                preparedStatement.setInt(3,populationTotale);
                preparedStatement.setInt(4,idDepartement);
                preparedStatement.setInt(5,idRegion);
                try {
                    int nblignes = preparedStatement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException e){
                    //e.printStackTrace();
                    System.out.println("Doublon interdit car la commune  " + nom +  " existe déjà");
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
        } else {
            System.out.println(" La ville " + nom + " est deja presente dans la base");
        }


    }

    public int update(int ancienCodeCommune, String ancienNom, int ancienPopulationTotale, int ancienIdDepartement, int ancienIdRegion, int nouveauCodeCommune, String nouveauNom, int nouveauPopulationTotale, int nouveauIdDepartement, int nouveauIdRegion, Connection connection){
        //On verifie d'abord la presence de la ville dans la BDD avant toute insertion
        int nbLignes =0;
        if (verifVillePresent(nouveauCodeCommune, nouveauNom, nouveauPopulationTotale, nouveauIdDepartement, nouveauIdRegion, connection) == false ){
            //Connection connection = null;
            PreparedStatement preparedStatement = null;
            //Connexion BDD
            try{
                //DriverManager.registerDriver(new Driver());
                //Database db = ConfigDatabase.extractConfig();
                //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

                preparedStatement = connection.prepareStatement("update ville set codeCommune = ?, nom = ?, populationTotale = ?, id_departement = ?, id_region = ? " +
                        "where  codeCommune = ? and nom = ? and populationTotale = ? and id_departement = ? and id_region = ?");
                preparedStatement.setInt(1,nouveauCodeCommune);
                preparedStatement.setString(2,nouveauNom);
                preparedStatement.setInt(3,nouveauPopulationTotale);
                preparedStatement.setInt(4,nouveauIdDepartement);
                preparedStatement.setInt(5,nouveauIdRegion);
                preparedStatement.setInt(6,ancienCodeCommune);
                preparedStatement.setString(7,ancienNom);
                preparedStatement.setInt(8,ancienPopulationTotale);
                preparedStatement.setInt(9,ancienIdDepartement);
                preparedStatement.setInt(10,ancienIdRegion);
                try {
                    nbLignes = preparedStatement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException e){
                    //e.printStackTrace();
                    System.out.println("Doublon interdit car la commune  " + nouveauNom +  " existe déjà");
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
        } else {
            System.out.println(" La ville " + nouveauNom + " est deja presente dans la base");
        }

        return nbLignes;
    }

    public boolean delete(Ville ville, Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        try {
           //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
            int id = ville.getId();
            preparedStatement = connection.prepareStatement("delete from ville where id = ?");
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

    public boolean verifVillePresent(int codeCommune, String nom, int populationTotale, int idDepartement, int idRegion, Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean verif = false;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

            preparedStatement = connection.prepareStatement("select * from ville where codeCommune = ? and nom = ? and populationTotale = ? and id_departement = ? and id_region = ?");
            preparedStatement.setInt(1,codeCommune);
            preparedStatement.setString(2,nom);
            preparedStatement.setInt(3,populationTotale);
            preparedStatement.setInt(4,idDepartement);
            preparedStatement.setInt(5,idRegion);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();

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
        return false;
    }

    public boolean verifVillePresent(Ville ville, Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean verif = false;
        //Connexion BDD
        try{
            //DriverManager.registerDriver(new Driver());
            //Database db = ConfigDatabase.extractConfig();
            //connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());

            preparedStatement = connection.prepareStatement("select * from ville where id = ?");
            preparedStatement.setInt(1,ville.getId());

            resultSet = preparedStatement.executeQuery();
            verif = resultSet.next()==false ? false : true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
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
        return verif;
    }

}
