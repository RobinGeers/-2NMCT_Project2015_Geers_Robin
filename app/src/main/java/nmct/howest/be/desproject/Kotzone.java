package nmct.howest.be.desproject;

/**
 * Created by robin_000 on 25/04/2015.
 */
public class Kotzone {

    private int id;
    private String coordinates;
    private String kotzonenaam;

    public Kotzone(int id, String coordinates, String kotzonenaam) {
        this.id = id;
        this.coordinates = coordinates;
        this.kotzonenaam = kotzonenaam;
    }

    public String getKotzonenaam() {
        return kotzonenaam;
    }
}
