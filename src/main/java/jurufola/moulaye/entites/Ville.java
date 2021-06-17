package jurufola.moulaye.entites;

/**
 * Classe représentant le ville
 * @author juruf_000
 */
public class Ville {
    private int id;
    private int codeCommune;
    private String nom;
    private int populationTotale;
    private Departement departement;
    private Region region;

    /**
     * Constructeur
     * @param id L'identifiant
     * @param codeCommune Le code de la commune
     * @param nom Le nom de la commune
     * @param populationTotale La population tatale de la commune
     * @param departement Le département
     * @param region La région
     */
    public Ville(int id, int codeCommune, String nom, int populationTotale, Departement departement, Region region) {
        this.id = id;
        this.codeCommune = codeCommune;
        this.nom = nom;
        this.populationTotale = populationTotale;
        this.departement = departement;
        this.region = region;
    }

    /**
     * Getteur id
     * @return L'identifiant de la ville
     */
    public int getId() {
        return id;
    }

    /**
     * Setteur id
     * @param id L'identifiant de la commune
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getteur code de la commune
     * @return Le code de la commune
     */
    public int getCodeCommune() {
        return codeCommune;
    }

    /**
     * Setteur code de la commune
     * @param codeCommune Le code de la commune
     */
    public void setCodeCommune(int codeCommune) {
        this.codeCommune = codeCommune;
    }

    /**
     * Getteur nom commune
     * @return Le nom de la commune
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setteur nom commune
     * @param nom Le nom de la commune
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getteur population totale
     * @return La population totale de la commune
     */
    public int getPopulationTotale() {
        return populationTotale;
    }

    /**
     * Setteur population totale
     * @param populationTotale La populmation toitale de la commune
     */
    public void setPopulationTotale(int populationTotale) {
        this.populationTotale = populationTotale;
    }

    /**
     * Getteur département
     * @return LE département
     */
    public Departement getDepartement() {
        return departement;
    }

    /**
     * Setteur département
     * @param departement Le département associé
     */
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    /**
     * Getteur région
     * @return LA région
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Setteur region
     * @param region La région associée
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Redéfinition toString
     * @return Une chaine de caractère décrivant la ville
     */
    @Override
    public String toString() {
        return "Ville{" +
                "id=" + id +
                ", codeCommune=" + codeCommune +
                ", nom='" + nom + '\'' +
                ", populationTotale=" + populationTotale +
                ", departement=" + departement +
                ", region=" + region +
                '}';
    }
}
