package fallofrome.game;

import fallofrome.game.board.Area;
import fallofrome.game.board.AreaConnection;

import java.util.List;

public class Force {
    private Allegiance allegiance;
    private UnitType unitType;
    private int strength;
    private boolean rebelling;

    private int movementPoints;

    // Used by Barbarians
    private Area destinationArea;
    private List<AreaConnection> pathToDestination;

    private int x, y;

    public Force(Allegiance allegiance, UnitType unitType, int strength){
        this.allegiance = allegiance;
        this.unitType = unitType;
        this.strength = strength;
        this.rebelling = false;

        movementPoints = 0;
        destinationArea = null;
        pathToDestination = null;

        x = 0;
        y = 0;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean isRebelling() {
        return rebelling;
    }

    public void setRebelling(boolean rebelling) {
        this.rebelling = rebelling;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public Area getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(Area destinationArea) {
        this.destinationArea = destinationArea;
    }

    public List<AreaConnection> getPathToDestination() {
        return pathToDestination;
    }

    public void setPathToDestination(List<AreaConnection> pathToDestination) {
        this.pathToDestination = pathToDestination;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoord(int mx, int my){
        this.x = mx;
        this.y = my;
    }

    public String toString(){
        return allegiance + " " + unitType + " (" + strength + ")";
    }
}
