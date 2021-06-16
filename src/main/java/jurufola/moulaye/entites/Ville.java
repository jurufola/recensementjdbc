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
     * @param id
     * @param codeCommune
     * @param nom
     * @param populationTotale
     * @param departement
     * @param region
     */
    public Ville(int id, int codeCommune, String nom, int populationTotale, Departement departement, Region region) {
        this.id = id;
        this.codeCommune = codeCommune;
        this.nom = nom;
        this.populationTotale = populationTotale;
        this.departement = departement;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(int codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulationTotale() {
        return populationTotale;
    }

    public void setPopulationTotale(int populationTotale) {
        this.populationTotale = populationTotale;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

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
