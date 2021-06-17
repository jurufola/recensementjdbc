package jurufola.moulaye.entites.dao;

import jurufola.moulaye.entites.Departement;
import jurufola.moulaye.entites.Region;
import jurufola.moulaye.entites.Ville;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Departement
 * @author juruf_000
 */
public class DepartementDao {
    /**
     * Extrait tous les département de la BDD
     * @param connection l'objet Connection
     * @return une liste de département
     */
    public List<Departement> extraire(Connection connection){
        //Init objets Resultset et PreparedStatement
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<Departement> departements = new ArrayList<>();
        try {
            //Jointure en tables departement et region afin de récuperer toutes les colonnes du departement ainsi que celles de la region associée
            preparedStatement = connection.prepareStatement("select (departement.id, departement.code, id_region, region.code, nom ) from departement, region where departement.id_region = region.id");
            resultSet = preparedStatement.executeQuery();

            //Constitution liste des départements extraits
            while(resultSet.next()){
                int id = resultSet.getInt("departement.id");
                String code = resultSet.getString("departement.code");
                int idRegion = resultSet.getInt("id_region");
                String codeRegion = resultSet.getString("region.code");
                String nomRegion = resultSet.getString("nom");

                departements.add(new Departement(id, code, new Region(idRegion, codeRegion, nomRegion)));
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

        return departements;
    }

    /**
     * Insert un département  dans la BDD à partir de l'objet Département
     * @param departement Le département
     * @param connection La connexion
     */
    public void insert(Departement departement, Connection connection){
        //Init objet PreparedStatement
        PreparedStatement preparedStatement = null;

        try{
            String code = departement.getCode();
            Region region = departement.getRegion();
            preparedStatement = connection.prepareStatement("insert into departement values(?, ?)");
            preparedStatement.setString(1,code);
            preparedStatement.setInt(2,region.getId());
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){// colonnes code et nom ont des contraintes d'unicité au niveau de la table. Une exception sera donc levée si insere un doublon
                System.out.println("Doublon interdit car le département " + code + " existe déjà");
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
     * Insert un département dans la BDD à partir du code départemental et de l'id de la région
     * @param code le code du département
     * @param idRegion L'id de la region associée
     * @param connection La connexion
     */

    public void insert(String code, int idRegion, Connection connection){
        //Initialisation objet PreparedStatement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("insert into departement(code, id_region) values(?, ?)");
            preparedStatement.setString(1, code);
            preparedStatement.setInt(2, idRegion);
            try {
                int nblignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){// colonnes code et nom ont des contrainte d'unicité au niveau de la table
                System.out.println("Doublon interdit car le département " + code + " existe déjà");
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
     * Modifie un département à partir des attributs ci-dessous
     * @param ancienCode L'ancien code du département
     * @param ancienIdRegion L'ancien id de la région associée
     * @param nouveauCode Le nouveau code du département
     * @param nouveauIdRegion Le nouvel id de la région associée
     * @param connection La connexion
     * @return le nombre de ligne modifiées
     */
    public int update(String ancienCode, int ancienIdRegion, String nouveauCode, int nouveauIdRegion, Connection connection){

        // Initialisation objet PreparedStatement
        PreparedStatement preparedStatement = null;
        int nbLignes = 0;

        try{
            preparedStatement = connection.prepareStatement("update departement set code = ? , id_region = ? where code = ? And id_region = ?");
            preparedStatement.setString(1, nouveauCode);
            preparedStatement.setInt(2,nouveauIdRegion);
            preparedStatement.setString(3,ancienCode);
            preparedStatement.setInt(4,ancienIdRegion);
            try {
                nbLignes = preparedStatement.executeUpdate();
            }catch (SQLIntegrityConstraintViolationException e){// colonnes code et nom ont des contraintes d'unicité au niveau de la table. Une exception sera donc levée si insere un doublon
                System.out.println("Doublon interdit car le département " + nouveauCode + " existe déjà");
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
     * Suppression département à partir de l'objet Departement
     * @param departement Le département
     * @param connection La connexion
     * @return un booleéen si sur une ligne a été modifiée ou pas
     */

    public boolean delete(Departement departement, Connection connection){

        //D'abord supprimer toutes les villes appartenant au departement
        VilleDao villeDao = new VilleDao();
        List<Ville> villes = villeDao.extraireParIdDepartement(departement.getId(), connection);
        for (Ville ville : villes) {
            villeDao.delete(ville, connection);
        }

        PreparedStatement preparedStatement = null;
        int nbLignes = 0;
        try {
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
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return nbLignes == 0 ? false : true;
    }

    /**
     * Extrait un département à partir de son identifiant
     * @param id L'identifiant du département
     * @param connection La connexion
     * @return déparement demandé
     */

    public Departement extraireParId(int id, Connection connection) {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Departement departement = null;
        try {
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
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departement;
    }

    /**
     * Extrait un département à partir de son code
     * @param codeDepartement Le code du département
     * @param connection La connexion
     * @return département démandé
     */
    public Departement extraireParCode(String codeDepartement, Connection connection) {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Departement departement = null;
        try {
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
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departement;
    }

    /**
     * Extrait une liste de départements à partir de l'id de la région associé
     * @param idRegion L'identifaint de la région associée
     * @param connection La connection
     * @return Liste départements demandés
     */
    public List<Departement> extraireParIdRegion(int idRegion, Connection connection) {
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
            } catch (SQLException e) {
                System.err.println("Problème de fermeture des ressources :" + e.getMessage());
            }
        }
        return departements;
    }
}
