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
     * Extrait toules régions de la BDD
     * @param connection LA connexion
     * @return Liste de régions une liste de régions
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
        // Fermeture ressources
        finally {
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
        }


        return regions;
    }

    /**
     * Insert une region dans la BDD à partir de l'objet Region
     * @param region La région
     * @param connection La connexion
     */
    public void insert(Region region, Connection connection) {
        //Init objet PreparedStatement
        PreparedStatement preparedStatement = null;
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
            }catch (SQLIntegrityConstraintViolationException e){ //// colonnes code et nom ont des contraintes d'unicité au niveau de la table. Une exception sera donc levée si insere un doublon
                System.out.println("Doublon interdit car la region " + nom + " existe déjà");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Fermeture ressources
        finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }

    }

    /**
     * Insert une région dans la BDD à partir du code et du nom de la région
     * @param code Le code de la région
     * @param nom Le nom de la région
     * @param connection La connexion
     */
    public void insert(String code, String nom, Connection connection) {
        //Initialisation objet PreparedStatement
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

    /**
     * Permet de modifier une région  à partir des attributs ci-dessous
     * @param ancienCode L'ancien code de la région
     * @param ancienNom  L'ancien nom de la région
     * @param nouveauCode Le nouveau code de la région
     * @param nouveauNom Le nouveau nom de la région
     * @param connection La  connexion
     * @return le nombre de ligne modifiées
     */
    public int update(String ancienCode, String ancienNom, String nouveauCode, String nouveauNom, Connection connection){
       // Initialisation objet PreparedStatement
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;

        try{
            preparedStatement = connection.prepareStatement("update region set code = ? , nom = ? where code = ? And nom = ?");
            preparedStatement.setString(1, nouveauCode);
            preparedStatement.setString(2,nouveauNom);
            preparedStatement.setString(3,ancienCode);
            preparedStatement.setString(4,ancienNom);
            try {
                 nbLignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){// colonnes code et nom ont des contraintes d'unicité au niveau de la table. Une exception sera donc levée si insere un doublon
                System.out.println("Doublon interdit car la region " + nouveauNom + " existe déjà");
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
        return nbLignes;
    }

    /**
     * Supprime un region de la base.
     * @param region La région
     * @param connection La connexion
     * @return un booleéen si sur une ligne a été modifiée ou pas
     */
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

    /**
     * Permet de recuper une region de la BDD à partir de son identifiant
     * @param idRegion L'identifiant de la région
     * @param connection La connexion
     * @return Region demandée.
     */
    public Region extraireParId(int idRegion, Connection connection) {
        //Initialisation objets ResultSet et PreparedStatement
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Region region = null;
        try {
            preparedStatement = connection.prepareStatement("select * from region where id = ?");
            preparedStatement.setInt(1, idRegion);
            resultSet = preparedStatement.executeQuery();

            // Parcours Objet ResultSet afin de constituer la région
            while(resultSet.next()){

                String codeRegion = resultSet.getString("code");
                String nomRegion = resultSet.getString("nom");
                region = new Region(idRegion, codeRegion, nomRegion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }try {// Fermeture ressources
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }

        } catch (SQLException e) {
            System.err.println("Problème de fermeture des ressources :" + e.getMessage());
        }
        return region;
    }

    /**
     * Permet de recuperer une région à partir de son nom
     * @param nomRegion Le nom de la région
     * @param connection La connexion
     * @return La région demandée
     */
    public Region extraireParNom(String nomRegion, Connection connection) {
        //Initialisation objets ResultSet et PreparedStatement
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Region region = null;
        try {
            preparedStatement = connection.prepareStatement("select * from region where nom = ?");
            preparedStatement.setString(1, nomRegion);
            resultSet = preparedStatement.executeQuery();

            // Parcours Objet ResultSet afin de constituer la région
            while(resultSet.next()){
                int idRegion = resultSet.getInt("id");
                String codeRegion = resultSet.getString("code");

                region = new Region(idRegion, codeRegion, nomRegion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {//Fermeture ressources
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
        }
        return region;
    }
}
