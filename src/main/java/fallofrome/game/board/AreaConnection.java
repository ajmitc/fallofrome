package fallofrome.game.board;

/**
 * Defines a movement connection between two Areas and the associated cost
 */
public class AreaConnection {
    private Area fromArea;
    private Area toArea;
    private int movementCost;

    public AreaConnection(Area from, Area to, int movementCost){
        this.fromArea = from;
        this.toArea = to;
        this.movementCost = movementCost;
    }

    public Area getFromArea() {
        return fromArea;
    }

    public Area getToArea() {
        return toArea;
    }

    public boolean hasArea(Area area){
        return fromArea == area || toArea == area;
    }

    public Area getOtherArea(Area area){
        return fromArea == area? toArea: fromArea;
    }

    public int getMovementCost() {
        return movementCost;
    }
}
