package fallofrome.game.board;

import fallofrome.game.AStarAlgorithm;
import fallofrome.game.Allegiance;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Province> provinces = new ArrayList<>();
    private List<AreaConnection> connections = new ArrayList<>();

    public Board(){
        provinces.add(new Province(Province.AEGYPTUS, "Ag", 10, 10, 'B'));
        provinces.add(new Province(Province.AFRICA, "Af", 4, 2, 'C'));
        provinces.add(new Province(Province.ARMENIA, "Ar", 2, 4, 'A'));
        provinces.add(new Province(Province.ASIA, "As", 12, 5, 'C'));
        provinces.add(new Province(Province.BRITANNIA, "Br", 3, 6, 'B'));
        provinces.add(new Province(Province.CYPRUS, "Cy", 1, 2, 'A'));
        provinces.add(new Province(Province.DACIA, "Da", 3, 12, 'B'));
        provinces.add(new Province(Province.GALLIA, "Ga", 12, 7, 'D'));
        provinces.add(new Province(Province.GERMANIA, "Gr", 8, 25, 'E'));
        provinces.add(new Province(Province.GRAECIA, "Gc", 5, 3, 'B'));
        provinces.add(new Province(Province.HISPANIA, "Hs", 5, 5, 'C'));
        provinces.add(new Province(Province.ILLYRIA, "Il", 6, 4, 'C'));
        provinces.add(new Province(Province.ITALIA, "It", 20, 1, 'B'));
        provinces.add(new Province(Province.MESOPOTAMIA, "Ms", 10, 8, 'C'));
        provinces.add(new Province(Province.PERSIA, "Pr", 15, 15, 'C'));
        provinces.add(new Province(Province.PICTUM, "Pc", 1, 6, 'B'));
        provinces.add(new Province(Province.SCYTHIA, "Sc", 1, 20, 'A'));
        provinces.add(new Province(Province.SICILIA, "Si", 4, 4, 'A'));
        provinces.add(new Province(Province.SYRIA, "Sy", 8, 10, 'C'));
        provinces.add(new Province(Province.TAURICA, "Ta", 1, 1, 'A'));
        provinces.add(new Province(Province.THRACIA, "Th", 4, 10, 'B'));


        // Intra-Province Connections
        connect(Province.AEGYPTUS, 'A', 'B', 5, 1);

        connect(Province.AFRICA, 'A', 'B', 5, 5);
        connect(Province.AFRICA, 'B','C', 1, 5);

        connect(Province.ASIA, 'A', 'B', 1, 1);
        connect(Province.ASIA, 'A', 'C', 1, 1);
        connect(Province.ASIA, 'B', 'C', 1, 1);

        connect(Province.BRITANNIA, 'A', 'B', 1, 1);

        connect(Province.DACIA, 'A', 'B', 3, 3);

        connect(Province.GALLIA, 'A', 'C', 1, 1);
        connect(Province.GALLIA, 'A', 'D', 1, 1);
        connect(Province.GALLIA, 'B', 'C', 1, 1);
        connect(Province.GALLIA, 'B', 'D', 1, 1);

        connect(Province.GERMANIA, 'A', 'B', 3, 3);
        connect(Province.GERMANIA, 'A', 'C', 3, 3);
        connect(Province.GERMANIA, 'A', 'D', 3, 3);
        connect(Province.GERMANIA, 'B', 'D', 3, 3);
        connect(Province.GERMANIA, 'B', 'E', 3, 3);
        connect(Province.GERMANIA, 'C', 'D', 3, 3);
        connect(Province.GERMANIA, 'D', 'E', 3, 3);

        connect(Province.GRAECIA, 'A', 'B', 1, 3);

        connect(Province.HISPANIA, 'A','B', 1, 1);
        connect(Province.HISPANIA, 'B','C', 1, 1);
        connect(Province.HISPANIA, 'A','C', 1, 1);

        connect(Province.ILLYRIA, 'A','B', 1, 1);
        connect(Province.ILLYRIA, 'A','C', 1, 1);
        connect(Province.ILLYRIA, 'B','C', 1, 1);

        connect(Province.ITALIA, 'A','B', 1, 5);

        connect(Province.MESOPOTAMIA, 'A','B', 1, 1);
        connect(Province.MESOPOTAMIA, 'B','C', 1, 1);

        connect(Province.PERSIA, 'A','B', 3, 3);
        connect(Province.PERSIA, 'A','C', 5, 3);
        connect(Province.PERSIA, 'B','C', 5, 3);

        connect(Province.PICTUM, 'A','B', 5, 1);

        connect(Province.SYRIA, 'A','B', 1, 5);
        connect(Province.SYRIA, 'A','C', 1, 5);
        connect(Province.SYRIA, 'B','C', 1, 1);

        connect(Province.THRACIA, 'A','B', 1, 3);

        // Extra-Province Land Connections
        connect(Province.AEGYPTUS, 'A', Province.SYRIA, 'B', 1, 1);
        connect(Province.AEGYPTUS, 'B', Province.AFRICA, 'B', 5, 5);

        connect(Province.ARMENIA, 'A', Province.ASIA, 'B', 1, 3);
        connect(Province.ARMENIA, 'A', Province.ASIA, 'C', 1, 3);
        connect(Province.ARMENIA, 'A', Province.MESOPOTAMIA, 'C', 1, 3);
        connect(Province.ARMENIA, 'A', Province.PERSIA, 'B', 3, 3);

        connect(Province.ASIA, 'A', Province.SYRIA, 'C', 1, 1);
        connect(Province.ASIA, 'A', Province.THRACIA, 'B', 1, 1);
        connect(Province.ASIA, 'B', Province.MESOPOTAMIA, 'C', 1, 1);
        connect(Province.ASIA, 'B', Province.SYRIA, 'C', 1, 1);
        connect(Province.ASIA, 'C', Province.THRACIA, 'B', 1, 1);

        connect(Province.BRITANNIA, 'A', Province.PICTUM, 'B', 1, 1);

        connect(Province.DACIA, 'A', Province.SCYTHIA, 'A', 1, 1);
        connect(Province.DACIA, 'A', Province.THRACIA, 'A', 1, 1);
        connect(Province.DACIA, 'A', Province.THRACIA, 'B', 1, 1);
        connect(Province.DACIA, 'B', Province.GERMANIA, 'B', 1, 1);
        connect(Province.DACIA, 'B', Province.GERMANIA, 'E', 1, 1);
        connect(Province.DACIA, 'B', Province.ILLYRIA, 'B', 1, 1);
        connect(Province.DACIA, 'B', Province.SCYTHIA, 'A', 1, 1);
        connect(Province.DACIA, 'B', Province.THRACIA, 'A', 1, 1);

        connect(Province.GALLIA, 'A', Province.ITALIA, 'A', 1, 1);
        connect(Province.GALLIA, 'A', Province.ITALIA, 'B', 1, 1);
        connect(Province.GALLIA, 'A', Province.HISPANIA, 'C', 1, 1);
        connect(Province.GALLIA, 'C', Province.GERMANIA, 'A', 1, 1);
        connect(Province.GALLIA, 'C', Province.GERMANIA, 'C', 1, 1);
        connect(Province.GALLIA, 'C', Province.ILLYRIA, 'C', 1, 1);
        connect(Province.GALLIA, 'C', Province.ITALIA, 'B', 1, 1);
        connect(Province.GALLIA, 'D', Province.HISPANIA, 'C', 1, 1);

        connect(Province.GERMANIA, 'A', Province.ILLYRIA, 'C', 1, 1);
        connect(Province.GERMANIA, 'B', Province.ILLYRIA, 'B', 1, 1);
        connect(Province.GERMANIA, 'B', Province.ILLYRIA, 'C', 1, 1);
        connect(Province.GERMANIA, 'E', Province.SCYTHIA, 'A', 1, 1);

        connect(Province.GRAECIA, 'B', Province.ILLYRIA, 'A', 1, 1);
        connect(Province.GRAECIA, 'B', Province.THRACIA, 'A', 1, 1);

        connect(Province.ILLYRIA, 'A', Province.ITALIA, 'A', 1, 1);
        connect(Province.ILLYRIA, 'A', Province.THRACIA, 'A', 1, 1);
        connect(Province.ILLYRIA, 'B', Province.THRACIA, 'A', 1, 1);
        connect(Province.ILLYRIA, 'C', Province.ITALIA, 'A', 1, 1);
        connect(Province.ILLYRIA, 'C', Province.ITALIA, 'B', 1, 1);

        connect(Province.MESOPOTAMIA, 'A', Province.PERSIA, 'A', 3, 1);
        connect(Province.MESOPOTAMIA, 'B', Province.PERSIA, 'A', 3, 1);
        connect(Province.MESOPOTAMIA, 'B', Province.PERSIA, 'B', 3, 1);
        connect(Province.MESOPOTAMIA, 'C', Province.PERSIA, 'B', 3, 1);
        connect(Province.MESOPOTAMIA, 'C', Province.SYRIA, 'A', 5, 1);
        connect(Province.MESOPOTAMIA, 'C', Province.SYRIA, 'C', 1, 1);

        connect(Province.SCYTHIA, 'A', Province.TAURICA, 'A', 1, 1);

        // Extra-Province Water Connections
        connect(Province.AEGYPTUS, 'B', Province.GRAECIA, 'A', 3);
        connect(Province.AEGYPTUS, 'A', Province.CYPRUS, 'A', 3);
        connect(Province.AFRICA, 'A', Province.HISPANIA, 'A', 1);
        connect(Province.AFRICA, 'C', Province.SICILIA, 'A', 1);
        connect(Province.AFRICA, 'C', Province.ITALIA, 'A', 3);
        connect(Province.ASIA, 'A', Province.CYPRUS, 'A', 1);
        connect(Province.ASIA, 'A', Province.GRAECIA, 'A', 3);
        connect(Province.ASIA, 'A', Province.GRAECIA, 'B', 3);
        connect(Province.ASIA, 'C', Province.SCYTHIA, 'A', 5);
        connect(Province.ASIA, 'C', Province.TAURICA, 'A', 3);
        connect(Province.BRITANNIA, 'A', Province.GALLIA, 'B', 3);
        connect(Province.BRITANNIA, 'B', Province.PICTUM, 'A', 3);
        connect(Province.CYPRUS, 'A', Province.SYRIA, 'C', 1);
        connect(Province.GALLIA, 'A', Province.SICILIA, 'A', 1);
        connect(Province.GRAECIA, 'B', Province.ITALIA, 'A', 1);
        connect(Province.HISPANIA, 'C', Province.SICILIA, 'A', 3);
        connect(Province.ITALIA, 'A', Province.SICILIA, 'A', 1);
        connect(Province.TAURICA, 'A', Province.THRACIA, 'B', 3);
    }

    private void connect(String provinceName, char id1, char id2, int movementCost1_2, int movementCost2_1){
        connect(provinceName, id1, provinceName, id2, movementCost1_2, movementCost2_1);
    }

    private void connect(String province1Name, char id1, String province2Name, char id2, int movementCost){
        connect(province1Name, id1, province2Name, id2, movementCost, movementCost);
    }

    private void connect(String province1Name, char id1, String province2Name, char id2, int movementCost1_2, int movementCost2_1){
        Area area1 = getArea(province1Name, id1);
        Area area2 = getArea(province2Name, id2);
        connections.add(new AreaConnection(area1, area2, movementCost1_2));
        connections.add(new AreaConnection(area2, area1, movementCost2_1));
    }

    /**
     * Return the Area defined as ProvinceName-ID or ProvinceAbbr-ID
     * @param code
     * @return
     */
    public Area getArea(String code){
        String[] parts = code.trim().split("-");
        return getArea(parts[0], parts[1].charAt(0));
    }

    public Area getArea(String provinceName, char id){
        Province province = getProvince(provinceName);
        return province.getArea(id);
    }

    public Province getProvince(String name){
        return provinces.stream().filter(p -> p.getName().equalsIgnoreCase(name) || p.getAbbreviation().equalsIgnoreCase(name)).findFirst().get();
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public List<AreaConnection> getConnections() {
        return connections;
    }

    /**
     * Return the total movement point cost for a force to move from one Area to another
     * @param fromArea
     * @param toArea
     * @return
     */
    public int getTotalMovementCostToEnter(Area fromArea, Area toArea){
        return getPathToArea(fromArea, toArea).stream().mapToInt(ac -> ac.getMovementCost()).sum();
    }

    /**
     * Get the shortest path (by total movement point) to get from one area to another
     * @param fromArea
     * @param toArea
     * @return
     */
    public List<AreaConnection> getPathToArea(Area fromArea, Area toArea){
        return AStarAlgorithm.findShortestPath(fromArea, toArea, this);
    }

    public boolean isAreaAdjacentToControlledProvince(Area area, Allegiance controller){
        return connections.stream().anyMatch(ac -> ac.hasArea(area) && ac.getOtherArea(area).getProvince().getController() == controller);
    }
}
