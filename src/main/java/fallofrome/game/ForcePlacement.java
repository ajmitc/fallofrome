package fallofrome.game;

public class ForcePlacement {
    private String province;
    private String area;  // If null, player chooses Area in province, otherwise, place units there automatically
    private Allegiance allegiance;
    private UnitType unitType;
    private int strength;

    public ForcePlacement(String province, Allegiance allegiance, UnitType unitType, int strength){
        this(province, null, allegiance, unitType, strength);
    }

    public ForcePlacement(String province, String area, Allegiance allegiance, UnitType unitType, int strength){
        this.province = province;
        this.area = area;
        this.allegiance = allegiance;
        this.unitType = unitType;
        this.strength = strength;
    }

    public Force toForce(){
        Force force = new Force(allegiance, unitType, strength);
        return force;
    }

    public String getProvince() {
        return province;
    }

    public String getArea() {
        return area;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public int getStrength() {
        return strength;
    }
}
