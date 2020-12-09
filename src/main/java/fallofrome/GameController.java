package fallofrome;

import fallofrome.game.*;
import fallofrome.game.board.AreaConnection;
import fallofrome.game.board.Province;
import fallofrome.game.table.BarbarianCreationTable;
import fallofrome.game.table.CombatResultsTable;
import fallofrome.game.table.InternalRevolutionTable;
import fallofrome.game.board.Area;
import fallofrome.game.table.LegionRebellionTable;
import fallofrome.util.Util;
import fallofrome.view.GameSidePanel;
import fallofrome.view.PopupUtil;
import fallofrome.view.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GameController implements MouseListener, MouseMotionListener {
    private static Logger logger = Logger.getLogger(GameController.class.getName());

    private static GameController instance = null;

    public static GameController getInstance(Model model, View view){
        if (instance == null)
            instance = new GameController(model, view);
        return instance;
    }

    public static GameController getInstance(){
        return instance;
    }

    private Model model;
    private View view;

    private List<ForcePlacement> setupForcePlacements;

    private ForcePlacement forceToPlace;

    private int replacementsAvailable;

    private GameController(Model model, View view){
        this.model = model;
        this.view = view;
        forceToPlace = null;
        replacementsAvailable = 0;

        if (!Model.getProperty("mapbuilder.show", false)) {
            view.getGamePanel().getBoardPanel().addMouseListener(this);
            view.getGamePanel().getBoardPanel().addMouseMotionListener(this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (model.getGame().isPhase(Phase.SETUP, PhaseStep.SETUP_PLACE_FORCES)){
            if (forceToPlace != null){
                // Get Area clicked
                Area area = view.getGamePanel().getBoardPanel().getAreaClicked(e.getX(), e.getY());
                // check if valid selection
                if (area.getProvince().getName().equals(forceToPlace.getProvince())){
                    // place unit
                    Force f = forceToPlace.toForce();
                    f.setStrength(1);
                    f.setCoord(e.getX(), e.getY());
                    area.addForce(f);
                    forceToPlace.setStrength(forceToPlace.getStrength() - 1);
                    if (forceToPlace.getStrength() == 0)
                        forceToPlace = null;
                    view.refresh();
                }
                else {
                    PopupUtil.popupNotification(view.getFrame(), "Place Force", "You must select an Area in " + forceToPlace.getProvince());
                    return;
                }
            }

            run();
        }
        else if (model.getGame().isPhase(Phase.LOYAL_ROMAN_MOVEMENT_PHASE, PhaseStep.LOYAL_ROMAN_MOVEMENT)){

        }
        else if (model.getGame().isPhase(Phase.LOYAL_ROMAN_COMBAT_PHASE, PhaseStep.LOYAL_ROMAN_COMBAT)){

        }
        else if (model.getGame().isPhase(Phase.ROMAN_PERSIAN_REPLACEMENT_PHASE, PhaseStep.PLACE_ROMAN_REPLACEMENTS)){

        }
        else if (model.getGame().isPhase(Phase.BARBARIAN_BRIBE_PHASE, PhaseStep.BRIBE_BARBARIANS)){

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void run(){
        while (model.getGame().getPhase() != Phase.GAMEOVER){
            view.refresh();
            switch (model.getGame().getPhase()) {
                case SETUP:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Setup ***", GameSidePanel.BOLD_ITALIC);
                            doSetup();
                            setupForcePlacements = model.getGame().getScenario().getForcePlacements().stream().filter(fp -> fp.getArea() == null).collect(Collectors.toList());
                            if (!setupForcePlacements.isEmpty()) {
                                model.getGame().setPhaseStep(PhaseStep.SETUP_PLACE_FORCES);
                                break;
                            }
                            break;
                        case SETUP_PLACE_FORCES:
                            if (!setupForcePlacements.isEmpty()) {
                                if (forceToPlace == null) {
                                    forceToPlace = setupForcePlacements.remove(0);
                                    PopupUtil.popupNotification(
                                            view.getFrame(),
                                            "Place Force",
                                            "Place " + forceToPlace.getStrength() + " " + forceToPlace.getAllegiance() + " Legions in " + forceToPlace.getProvince());
                                    view.getGamePanel().getGameSidePanel().appendOutputLn("Place " + forceToPlace.getStrength() + " Legions in " + forceToPlace.getProvince());
                                }
                                return;
                            }
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.INTERNAL_REVOLUTION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case INTERNAL_REVOLUTION_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Internal Revolution Phase ***", GameSidePanel.BOLD_ITALIC);
                            doInternalRevolutionPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.NON_ROMAN_NON_LOYAL_ROMAN_MOVEMENT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case NON_ROMAN_NON_LOYAL_ROMAN_MOVEMENT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Non-Roman & Non-Loyal-Roman Movement Phase ***", GameSidePanel.BOLD_ITALIC);
                            doNonRomanNonLoyalRomanMovementPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.NON_ROMAN_NON_LOYAL_ROMAN_COMBAT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case NON_ROMAN_NON_LOYAL_ROMAN_COMBAT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Non-Roman & Non-Loyal-Roman Combat Phase ***", GameSidePanel.BOLD_ITALIC);
                            doNonRomanNonLoyalRomanCombatPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.BARBARIAN_CREATION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case BARBARIAN_CREATION_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Barbarian Creation Phase ***", GameSidePanel.BOLD_ITALIC);
                            doBarbarianCreationPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.LOYAL_ROMAN_MOVEMENT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case LOYAL_ROMAN_MOVEMENT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Loyal Roman Movement Phase ***", GameSidePanel.BOLD_ITALIC);
                            doLoyalRomanMovementPhase();
                            model.getGame().setPhaseStep(PhaseStep.LOYAL_ROMAN_MOVEMENT);
                            break;
                        case LOYAL_ROMAN_MOVEMENT:
                            return;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.LOYAL_ROMAN_COMBAT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case LOYAL_ROMAN_COMBAT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Loyal Roman Combat Phase ***", GameSidePanel.BOLD_ITALIC);
                            doLoyalRomanCombatPhase();
                            model.getGame().setPhaseStep(PhaseStep.LOYAL_ROMAN_COMBAT);
                            break;
                        case LOYAL_ROMAN_COMBAT:
                            return;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.LEGION_REBELLION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case LEGION_REBELLION_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Legion Rebelliono Phase ***", GameSidePanel.BOLD_ITALIC);
                            doLegionRebellionPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.CONTROL_DETERMINATION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case CONTROL_DETERMINATION_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Control Determination Phase ***", GameSidePanel.BOLD_ITALIC);
                            doControlDeterminationPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.BARBARIAN_ATTRITION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case BARBARIAN_ATTRITION_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Barbarian Attrition Phase ***", GameSidePanel.BOLD_ITALIC);
                            doBarbarianAttritionPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.TAX_COLLECTION_DISBURSEMENT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case TAX_COLLECTION_DISBURSEMENT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Tax Collection & Distribution Phase ***", GameSidePanel.BOLD_ITALIC);
                            doTaxCollectionDisbursementPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.ROMAN_PERSIAN_REPLACEMENT_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case ROMAN_PERSIAN_REPLACEMENT_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Roman & Persian Replacement Phase ***", GameSidePanel.BOLD_ITALIC);
                            doRomanPersianReplacementPhase();
                            // Don't set END_PHASE here, it is taken care of in the method above
                            break;
                        case PLACE_ROMAN_REPLACEMENTS:
                            return;
                        case END_PHASE:
                            endRomanPersianReplacementPhase();
                            model.getGame().setPhase(Phase.BARBARIAN_BRIBE_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case BARBARIAN_BRIBE_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Barbarian Bribe Phase ***", GameSidePanel.BOLD_ITALIC);
                            doBarbarianBribePhase();
                            // Don't set END_PHASE here, it is taken care of in the method above
                            break;
                        case BRIBE_BARBARIANS:
                            return;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.GAME_TURN_RECORD_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
                case GAME_TURN_RECORD_PHASE:
                    switch (model.getGame().getPhaseStep()) {
                        case START_PHASE:
                            view.getGamePanel().getGameSidePanel().appendOutputLn("*** Game Record Phase ***", GameSidePanel.BOLD_ITALIC);
                            doGameTurnRecordPhase();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.INTERNAL_REVOLUTION_PHASE);
                            break;
                        default:
                            logger.warning("Invalid PhaseStep (" + model.getGame().getPhaseStep() + ") for Phase " + model.getGame().getPhase());
                            break;
                    }
                    break;
            }
        }
    }

    public void doSetup(){
        // Update coords of non-roman forces
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                for (int i = 0; i < a.getForces().size(); ++i){
                    Force f = a.getForces().get(i);
                    if (f.getX() == 0 && f.getY() == 0) {
                        List<Point> placements = view.getGamePanel().getBoardPanel().getAreaForcePlacements(a);
                        Point point = placements.get(i);
                        f.setCoord(point.x, point.y);
                        view.getGamePanel().getGameSidePanel().appendOutputLn(f.getStrength() + " " + f.getAllegiance() + " " + f.getUnitType() + " placed in " + a);
                    }
                }
            });
        });
    }

    /**
     * Internal Revolution Phase
     * The Player rolls the die for the Internal Revolution Probability Table.  If the result is "yes," the die is
     * rolled for the Internal Revolution Results Table.  This table depicts the number of Revolting Militia Strength
     * Points that appear in a Roman-controlled province.  Units are placed in accordance with non-Roman unit placement
     * procedures (see 13.0).  Already existing revolutions are also incremented.
     */
    public void doInternalRevolutionPhase(){
        if (InternalRevolutionTable.getProbability(model.getGame().getGamePeriod())){
            view.getGamePanel().getGameSidePanel().appendOutputLn("Internal Revolutions activated!");
            List<ForcePlacement> forcePlacements = InternalRevolutionTable.getResults(model.getGame().getGamePeriod());
            for (ForcePlacement forcePlacement: forcePlacements){
                Province province = model.getGame().getBoard().getProvince(forcePlacement.getProvince());
                if (province.getController() != Allegiance.ROMAN && model.getGame().getGamePeriod() != GamePeriod.G)
                    continue;
                // Ignore result if Game Period 'G' and Persian controlled
                if (province.getController() == Allegiance.PERSIAN && model.getGame().getGamePeriod() == GamePeriod.G)
                    continue;
                // Provinces in revolt do not revolt again, ignore result
                if (province.isMilitiaRebelling())
                    continue;
                // Roman controlled provinces may reduce the strength of revolting militia by strength of legions in province
                if (province.getController() == Allegiance.ROMAN){
                    int romanStrength = province.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR);
                    if (romanStrength >= forcePlacement.getStrength())
                        continue;
                    forcePlacement.setStrength(forcePlacement.getStrength() - romanStrength);
                }

                province.setMilitiaRebelling(true);
                if (province.isMilitiaActivated()){
                    // Change existing militia on map to rebelling
                    List<Force> rebelMilitia = province.getForces(province.getController(), UnitType.MILITIA);
                    rebelMilitia.stream().forEach(m -> m.setRebelling(true));
                }
                else {
                    placeNonRomanForce(forcePlacement);
                }
            }
        }
        else
            view.getGamePanel().getGameSidePanel().appendOutputLn("No Revolutions this turn");
    }

    /**
     * Non-Roman and Non-loyal Roman Movement Phase.
     * The Player moves all the non-Roman and non-loyal Roman forces in accordance with the dictates of their movement
     * restrictions.  The forces are moved one type at a time, in this order.
     * 1. Rebellious Roman Legions
     * 2. Hun Barbarians
     * 3. German Barbarians
     * 4. Scythian Barbarians
     * 5. Dacian Barbarians
     * 6. Pictish Barbarians
     * 7. Taurican Barbarians
     * 8. Independent States' Regular Units (by number)
     * 9. Persians
     *
     * Note that each of these types are separate and distinct, and all these forces are inimical to one another as well.
     * As each force enters an Area, if it contains any opposing Strength Points at all, it may move no further in this
     * Game-Turn.  If it enters an Area containing a non-loyal Roman Force or non-Roman of a different type, it must
     * immediately attack that force.
     */
    private void doNonRomanNonLoyalRomanMovementPhase(){
        final Allegiance[] ORDER = {
                Allegiance.REBELLIOUS_ROMAN,
                Allegiance.HUN_BARBARIAN,
                Allegiance.GERMAN_BARBARIAN,
                Allegiance.SCYTHIAN_BARBARIAN,
                Allegiance.DACIAN_BARBARIAN,
                Allegiance.PICTISH_BARBARIAN,
                Allegiance.TAURICAN_BARBARIAN,
                Allegiance.INDEPENDENT_STATE,
                Allegiance.PERSIAN
        };

        for (Allegiance allegiance: ORDER){
            resetMovement(allegiance);
        }

        for (Allegiance allegiance: ORDER){
            move(allegiance);
        }
    }

    /**
     * Apply the movement allowance (5 points) to all forces on the board
     *
     * - Cannot move if opposing force with greater strength (from a single type, Regular/Militia/etc) in same
     *   Area (exception: Rebellious Roman)
     *
     * @param allegiance
     */
    private void resetMovement(Allegiance allegiance){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                OptionalInt forceSize = a.getForces().stream().filter(f -> f.getAllegiance() == allegiance).mapToInt(f -> f.getStrength()).max();
                if (forceSize.isPresent()) {
                    OptionalInt opposingForceSize = a.getForces().stream().filter(f -> f.getAllegiance() != allegiance).mapToInt(f -> f.getStrength()).max();
                    if (!opposingForceSize.isPresent() || forceSize.getAsInt() >= opposingForceSize.getAsInt()) {
                        a.getForces().stream().filter(f -> f.getAllegiance() == allegiance).forEach(f -> f.setMovementPoints(5));
                    }
                }
            });
        });
    }

    /**
     * As each group of Non-Roman units enters an Area, one by one they must stop if there are any units in that Area
     * belonging to a different force.  If the units in that Area are Loyal Roman, no combat occurs until Combat Phase
     * C; if the units in that Area are not Loyal Roman, combat must occur immediately (see Combat, 6.33).
     *
     * All non-Roman units (no rebellious legions) must move towards the smallest group of loyal Roman legions in the
     * same Province with them (Exception: cases 5.63, 5.65, 5.72 and 5.75).  If there are no Romans in that Province
     * they must move toward the smallest force contesting their control of the Province.
     *
     * Independent State Regulars must first move upon the smallest concentration of loyal Romans in their Home
     * Province.  Barbarian Regulars, if they have not left their Home Province (for a calculated “richest province”),
     * must move upon the smallest concentration of loyal Romans in their Home Province.
     *
     * All Barbarian units, created as a group, must move as a group, and may not sub-divide (Exception: see Case 5.64).
     *
     * A die must be rolled whenever any group of Barbarian units attempts to enter an Area.  A die result of “1” or “2”
     * indicates that the group may not leave its present Area and may attempt to move no further in that Movement
     * Phase.
     * This Procedure is repeated for each Area moved into until the Barbarian force encounters a hostile force or a
     * die roll of “1” or “2” comes up, whichever is first.
     *
     * Barbarian forces, after gaining Control (see Control, 7.0) of a Region, must leave behind a sufficient number of
     * Strength Points to control the Region and then must move the remaining Strength Points toward the new lowest
     * Victory Point Region or “richest province” (see. 5.65).
     *
     * Barbarians never have to garrison their Home Provinces, nor do they have to garrison (or Control) any Province
     * they pass through on the way to their “richest province” target.  If on the way they incidentally control a
     * Province, this helps them in that they will not suffer from Barbarian Attrition while in that Province.
     * Barbarian Forces must garrison conquered target Provinces with a number of Strength Points equal to the
     * Province's Victory Point total.  If they do not have enough Strength to maintain a garrison, they do not leave
     * for a new target.
     *
     * When any Barbarian unit, or group of units, have two or more equidistant paths to their destinations, they must
     * attempt to move through the smallest individual group of hostile, non-Romans (I.e. Units not belong to the same
     * Province or the same Independent State of origin as the moving units) if any (Exception: Movement, case 5.45).
     *
     * If any Barbarian unit, or group of units, remaining in a Region during the Control Determination Phase (see:
     * Sequence of Play, 4.2) fulfills the control conditions of that Region, then in the next non-Roman Movement Phase
     * those Strength Points in excess of the amount needed to maintain control must attempt to move toward their
     * predetermined destination (“richest province”).
     *
     * The destinations for all newly created Barbarian units, or Barbarian units initially placed on the map, are
     * determined separately, for each group of units (even of the same force) in the same placement Area and only at
     * the beginning of the non-Roman Movement Phase immediately following their replacement on the map.  These
     * destinations may not be changed unless two different Barbarian groups belonging to the same force are heading
     * for different destinations and end their Movement Phase in the same Area.  At this point, the groups must be
     * combined and re-determine their destination
     *
     * @param allegiance
     */
    private void move(Allegiance allegiance){
        view.getGamePanel().getGameSidePanel().appendOutputLn("Moving " + allegiance);
        if (allegiance == Allegiance.REBELLIOUS_ROMAN){
            moveRebelliousRomanLegions();
        }
        else if (allegiance == Allegiance.PERSIAN){
            movePersians();
        }
        else {
            moveBarbarianRegulars(allegiance);
            moveRaidingParties(allegiance);
        }
    }

    /**
     * Find all barbarian regulars and move them
     *
     * @param allegiance
     */
    private void moveBarbarianRegulars(Allegiance allegiance){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                a.getForces().stream().filter(f -> f.getAllegiance() == allegiance && f.getUnitType() == UnitType.REGULAR).forEach(f -> {
                    moveBarbarianRegulars(f, a);
                });
            });
        });
    }

    /**
     * Barbarian regulars must move into the Empire by way of the shortest and most direct route, toward the
     * “richest province” controlled by Roman forces.
     *
     * The “target” Province is not changed for a given Force until it reaches its
     * destination unless it merges with another barbarian force (see 5.69) at which point, a new “target” Province must
     * be determined based on the group's current position.
     *
     * The new lowest Victory Point Region or “richest province” is determined immediately upon gaining control of the
     * old one for each group during the Control Determination Phase.
     *
     * @param force
     * @param area
     */
    private void moveBarbarianRegulars(Force force, Area area){
        if (force.getDestinationArea() == null){
            assignBarbarianRegularDestinationProvince(force, area);
        }

        List<AreaConnection> areaConnections = force.getPathToDestination();
        while (!areaConnections.isEmpty()) {
            // Pop the first connection
            AreaConnection areaConnection = areaConnections.remove(0);
            if (areaConnection.getFromArea() != area) {
                logger.severe(force + " at " + area + " path to destination (" + force.getDestinationArea() + ") does not start at their Area (first node: " + areaConnection.getFromArea() + ")!");
                return;
            }

            // Move force to new Area
            if (force.getMovementPoints() >= areaConnection.getMovementCost()) {
                area.getForces().remove(force);
                areaConnection.getToArea().getForces().add(force);
                force.setMovementPoints(force.getMovementPoints() - areaConnection.getMovementCost());
                view.getGamePanel().getGameSidePanel().appendOutputLn("Moved " + force + " from " + area + " to " + areaConnection.getToArea());

                // Stop moving if there's an opposing force
                if (areaConnection.getToArea().hasOpposingForce(force.getAllegiance())){
                    view.getGamePanel().getGameSidePanel().appendOutputLn("   Opposing force found, cannot move further");
                    break;
                }
            }
            else
                // Stop moving if we can't afford to move into the target area
                break;
        }
    }

    /**
     * To determine the “richest province,” divide the VP by the total number of
     * Movement Points it would cost to move there.  Ignore intervening forces.  After computation, the Region with the
     * highest quotient is the “richest province.”
     *
     * If there should be two or more equivalent paths, roll the die for each path; high die roll is used.
     *
     * @param force
     * @param area
     */
    private void assignBarbarianRegularDestinationProvince(Force force, Area area){
        Area target = null;
        double targetQuotient = Double.MAX_VALUE;
        for (Province province: model.getGame().getBoard().getProvinces()) {
            if (province.getController() == Allegiance.ROMAN) {
                for (Area a : province.getAreas()) {
                    double quotient = province.getVictoryPointAllowance() / (double) model.getGame().getBoard().getTotalMovementCostToEnter(area, a);
                    if (quotient < targetQuotient) {
                        targetQuotient = quotient;
                        target = a;
                    }
                }
            }
        }
        view.getGamePanel().getGameSidePanel().appendOutputLn("Assigned " + force + " movement destination to " + target);
        force.setDestinationArea(target);
        force.setPathToDestination(model.getGame().getBoard().getPathToArea(area, force.getDestinationArea()));
    }

    /**
     * Move Raiding Parties
     *
     * @param allegiance
     */
    private void moveRaidingParties(Allegiance allegiance){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                a.getForces().stream().filter(f -> f.getAllegiance() == allegiance && f.getUnitType() == UnitType.RAIDING_PARTY).forEach(f -> {
                    moveRaidingParty(f, a);
                });
            });
        });
    }

    /**
     * TODO If the Raiding Party gains Control of its objective, it must leave a garrison equal to the Victory Point total of
     * the Province and select a new raiding objective.  If it does not have enough Strength to maintain a garrison, it
     * does not leave its target once it arrives there.
     *
     * @param force
     * @param area
     */
    private void moveRaidingParty(Force force, Area area){
        if (force.getDestinationArea() == null){
            assignRaidingPartyDestinationArea(force, area);
        }

        List<AreaConnection> areaConnections = force.getPathToDestination();
        while (!areaConnections.isEmpty()) {
            // Pop the first connection
            AreaConnection areaConnection = areaConnections.remove(0);
            if (areaConnection.getFromArea() != area) {
                logger.severe(force + " at " + area + " path to destination (" + force.getDestinationArea() + ") does not start at their Area (first node: " + areaConnection.getFromArea() + ")!");
                return;
            }

            // Move force to new Area
            if (force.getMovementPoints() >= areaConnection.getMovementCost()) {
                area.getForces().remove(force);
                areaConnection.getToArea().getForces().add(force);
                force.setMovementPoints(force.getMovementPoints() - areaConnection.getMovementCost());
                view.getGamePanel().getGameSidePanel().appendOutputLn("Moved " + force + " from " + area + " to " + areaConnection.getToArea());

                // Stop moving if there's an opposing force
                if (areaConnection.getToArea().hasOpposingForce(force.getAllegiance())){
                    view.getGamePanel().getGameSidePanel().appendOutputLn("   Found opposing force, cannot move further");
                    // TODO Initiate Combat if non-Loyal-Roman
                    break;
                }
            }
            else
                // Stop moving if we can't afford to move into the target area
                break;
        }
    }

    /**
     * Raiding Parties must move toward the nearest Province in the Empire of five or fewer Victory Points by way of the
     * shortest and most direct route.  If more than one of the above regions is equidistant then the Raiding Parties
     * must go to the lowest Victory Point Region of the two.
     *
     * @param force
     * @param area
     */
    private void assignRaidingPartyDestinationArea(Force force, Area area){
        Area target = null;
        int lowestMovementCost = Integer.MAX_VALUE;
        for (Province province: model.getGame().getBoard().getProvinces()){
            if (province.getController() == Allegiance.ROMAN) {
                for (Area toArea: province.getAreas()) {
                    int cost = model.getGame().getBoard().getTotalMovementCostToEnter(area, toArea);
                    if (cost < lowestMovementCost ||
                            (cost == lowestMovementCost && toArea.getProvince().getVictoryPointAllowance() < target.getProvince().getVictoryPointAllowance())){
                        target = toArea;
                        lowestMovementCost = cost;
                    }
                }
            }
        }
        view.getGamePanel().getGameSidePanel().appendOutputLn("Assigned " + force + " movement destination to " + target);
        force.setDestinationArea(target);
        force.setPathToDestination(model.getGame().getBoard().getPathToArea(area, force.getDestinationArea()));
    }

    /**
     * Move the Persians
     */
    private void movePersians(){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                a.getForces().stream().filter(f -> f.getAllegiance() == Allegiance.PERSIAN && f.getUnitType() == UnitType.REGULAR).forEach(f -> {
                    movePersians(f, a);
                });
            });
        });
    }

    /**
     * Move the Persian force
     * @param force
     * @param area
     */
    private void movePersians(Force force, Area area){
        // If destination is null, try to assign a new destination
        if (force.getDestinationArea() == null){
            assignPersianDestinationArea(force, area);
        }

        // If the destination is still null, forget about it, nothing to do
        if (force.getDestinationArea() == null)
            return;

        List<AreaConnection> areaConnections = force.getPathToDestination();
        while (!areaConnections.isEmpty()) {
            // Pop the first connection
            AreaConnection areaConnection = areaConnections.remove(0);
            if (areaConnection.getFromArea() != area) {
                logger.severe(force + " at " + area + " path to destination (" + force.getDestinationArea() + ") does not start at their Area (first node: " + areaConnection.getFromArea() + ")!");
                return;
            }

            // Move force to new Area
            if (force.getMovementPoints() >= areaConnection.getMovementCost()) {
                area.getForces().remove(force);
                areaConnection.getToArea().getForces().add(force);
                force.setMovementPoints(force.getMovementPoints() - areaConnection.getMovementCost());
                view.getGamePanel().getGameSidePanel().appendOutputLn("Moved " + force + " from " + area + " to " + areaConnection.getToArea());

                // Stop moving if there's an opposing force
                if (areaConnection.getToArea().hasOpposingForce(force.getAllegiance())){
                    view.getGamePanel().getGameSidePanel().appendOutputLn("   Found opposing force, cannot move further");
                    // TODO Initiate Combat if non-Loyal-Roman
                    break;
                }
            }
            else
                // Stop moving if we can't afford to move into the target area
                break;
        }
    }

    /**
     * The Persians must direct their forces into Persia, Mesopotamia, Armenia, Syria, and Asia, in that order of
     * priority, and may not leave a Province until they have gained control (see: Control) of that Province.  If their
     * order of priority is upset by losing control of a previously conquered Province, the Persians must attempt to
     * regain control of that Province by moving all Strength Points in Regions of lower priority to the Province
     * (Exception: Movement, cases 5.74 and 5.74).  They may only move as is necessary to conquer a Province and must
     * always attempt to engage the largest contesting individual force in that Area with all Strength Points available.
     *
     * If the Persians manage to Control Persia, Mesopotamia, Armenia, Syria, and Asia, they do nothing but wait and
     * grow rich.  Of course, they must attempt to deal with all enemy incursions into their territory.
     *
     * TODO Persian units may not enter an Area of a Province whose Victory Point value is greater than the number of
     * Strength Points the Persians began that scenario with.
     *
     * The Persians are not allowed to leave any forces behind in Provinces they have already conquered.  Thus, in
     * effect, there may only be Persian Regular units in one Area outside of Persia except for arriving replacements
     * and units unable to move (see 5.22).
     *
     * TODO Persian units may not move through any Province that is not on their priority schedule.  Thus they may only
     * enter Persia, Mesopotamia, Armenia, Syria, and Asia.
     *
     * TODO If there is a choice where Persian Regular units can move, i.e., there is an equal number of contesting Strength
     * Points of two different forces in a given Region, they must avoid Loyal Roman Legion groups, and move toward and
     * attack other forces.  In the case of equal numbers of non-Loyal Roman units, high die roll for contesting force
     * decides which to move toward and attack.  Of course this movement may be interrupted by case 5.25.
     *
     * @param force
     * @param area
     */
    private void assignPersianDestinationArea(Force force, Area area){
        final List<String> ORDER = Arrays.asList(Province.PERSIA, Province.MESOPOTAMIA, Province.ARMENIA, Province.SYRIA, Province.ASIA);

        for (String provinceToCheck: ORDER){
            Province targetProvince = model.getGame().getBoard().getProvince(provinceToCheck);
            if (targetProvince.getController() != Allegiance.PERSIAN){
                // All forces in lower priority non-persian-controlled provinces must move here
                //int forceProvinceOrder = ORDER.indexOf(area.getProvince().getName());
                //int targetProvinceOrder = ORDER.indexOf(provinceToCheck);
                //if (targetProvinceOrder < forceProvinceOrder){
                    // Assign the Area with the largest opposing force
                    Area target = null;
                    int targetMaxStrength = Integer.MIN_VALUE;
                    for (Area provinceArea: targetProvince.getAreas()){
                        OptionalInt maxStrength =
                                provinceArea.getForces().stream()
                                        .filter(f -> f.getAllegiance() != Allegiance.PERSIAN)
                                        .mapToInt(f -> f.getStrength())
                                        .max();
                        if (maxStrength.isPresent() && (target == null || maxStrength.getAsInt() > targetMaxStrength)){
                            target = provinceArea;
                            targetMaxStrength = maxStrength.getAsInt();
                        }
                    }
                    view.getGamePanel().getGameSidePanel().appendOutputLn("Assigned " + force + " movement destination to " + target);
                    force.setDestinationArea(target);
                    force.setPathToDestination(model.getGame().getBoard().getPathToArea(area, force.getDestinationArea()));
                    return;
                //}
            }
        }
    }

    /**
     * Rebellious Roman legions that do not form an Independent State (see: Independent States) must move toward
     * Rome (Italia A) by way of the shortest and most direct route (Exception: Rebellious legions, case 8.3) as
     * measured by Movement Points only.  It may not deviate from this path.  If there are equidistant paths, a die
     * should be rolled for each path, high roll being chosen.
     *
     */
    private void moveRebelliousRomanLegions(){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                a.getForces().stream().filter(f -> f.getAllegiance() == Allegiance.REBELLIOUS_ROMAN).forEach(f -> {
                    moveRebelliousRomanLegions(f, a);
                });
            });
        });
    }

    private void moveRebelliousRomanLegions(Force force, Area area){
        if (force.getDestinationArea() == null){
            assignRebelliousRomanLegionsDestinationArea(force, area);
        }

        List<AreaConnection> areaConnections = force.getPathToDestination();
        while (!areaConnections.isEmpty()) {
            // Pop the first connection
            AreaConnection areaConnection = areaConnections.remove(0);
            if (areaConnection.getFromArea() != area) {
                logger.severe(force + " at " + area + " path to destination (" + force.getDestinationArea() + ") does not start at their Area (first node: " + areaConnection.getFromArea() + ")!");
                return;
            }

            // Move force to new Area
            if (force.getMovementPoints() >= areaConnection.getMovementCost()) {
                area.getForces().remove(force);
                areaConnection.getToArea().getForces().add(force);
                force.setMovementPoints(force.getMovementPoints() - areaConnection.getMovementCost());
                view.getGamePanel().getGameSidePanel().appendOutputLn("Moved " + force + " from " + area + " to " + areaConnection.getToArea());

                // Stop moving if there's an opposing force
                if (areaConnection.getToArea().hasOpposingForce(force.getAllegiance())){
                    view.getGamePanel().getGameSidePanel().appendOutputLn("   Found opposing force, cannot move further");
                    // TODO Initiate Combat
                    break;
                }
            }
            else
                // Stop moving if we can't afford to move into the target area
                break;
        }
    }

    /**
     * Rebellious Roman legions that do not form an Independent State (see: Independent States) must move toward
     * Rome (Italia A) by way of the shortest and most direct route (Exception: Rebellious legions, case 8.3) as
     * measured by Movement Points only.  It may not deviate from this path.  If there are equidistant paths, a die
     * should be rolled for each path, high roll being chosen.
     *
     */
    private void assignRebelliousRomanLegionsDestinationArea(Force force, Area area){
        force.setDestinationArea(model.getGame().getBoard().getArea(Province.ITALIA, 'A'));
        force.setPathToDestination(model.getGame().getBoard().getPathToArea(area, force.getDestinationArea()));
        view.getGamePanel().getGameSidePanel().appendOutputLn("Assigned " + force + " movement destination to " + force.getDestinationArea());
    }

    /**
     * Non-Roman and non-Loyal Roman Combat Phase.
     *
     * Each of the different non-Roman and non-Loyal Roman forces must attack any loyal Roman force in the same Area.
     * If there is more than one type of non-Loyal Roman force and Loyal Romans in the Area, the larger force attacks
     * first, the second largest second, etc.
     */
    private void doNonRomanNonLoyalRomanCombatPhase(){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                if (a.hasForce(Allegiance.ROMAN) && a.hasOpposingForce(Allegiance.ROMAN)){
                    doNonRomanNonLoyalRomanCombatPhase(a);
                }
            });
        });
    }

    /**
     * Each of the different non-Roman and non-Loyal Roman forces must attack any loyal Roman force in the same Area.
     * If there is more than one type of non-Loyal Roman force and Loyal Romans in the Area, the larger force attacks
     * first, the second largest second, etc.
     *
     * @param area
     */
    private void doNonRomanNonLoyalRomanCombatPhase(Area area){
        List<Force> opposingForces =
                area.getForces().stream()
                        .filter(f -> f.getAllegiance() != Allegiance.ROMAN)
                        .sorted(new Comparator<Force>() {
                            @Override
                            public int compare(Force o1, Force o2) {
                                return o1.getStrength() > o2.getStrength()? -1: o1.getStrength() < o2.getStrength()? 1: 0;
                            }
                        })
                        .collect(Collectors.toList());
        List<Force> romanForces =
                area.getForces().stream()
                        .filter(f -> f.getAllegiance() == Allegiance.ROMAN)
                        .collect(Collectors.toList());

        for (Force opposingForce: opposingForces){
            doNonRomanNonLoyalRomanCombat(area, opposingForce, romanForces);
        }
    }

    /**
     * Combat occurs between Roman and non-Roman units during the non-Roman and Roman Combat Phases and also between
     * non-Roman units of different forces during the non-Roman Movement of the moving units.  The currently moving
     * units are considered to be the Attacker and the non-moving units are considered to be the Defender.
     *
     * PROCEDURE:
     * Combat is resolved by comparing the total Combat Strength of the Attacker to that of the Defender.  The
     * comparison is stated as a probability ratio: the Attacker's Combat Strength to the Defender's Combat Strength.
     * The ratio is simplified to conform to the odds given on the Combat Results Table (the ratio is always rounded
     * off in favor of the Defender, it may not conform exactly to the ratios given in the table).  The die is rolled
     * and the die result is cross-indexed with the proper odds column on the Combat Results Table.  The obtained result
     * is applied before proceeding to another battle.
     *
     * [6.1] COMBAT INHIBITIONS AND PROHIBITIONS
     *
     * [6.11] The minimum odds of an attack are one-to-one. Attacks at odds less than one-to-one are not allowed.
     *
     * [6.12] Regular and militia units of the same force (e.g. Persian regulars and militia) that are in the same Area
     * may not be attacked separately.  They must be combined and attacked as a group.
     *
     * TODO [6.13] Combat losses are extracted by order of preference from (1) Regular; (2) Raiding Parties; (3) Militia
     * units in the same Area.  (Exception: 1/2 De Results do not affect loyal Roman legions.  If they are defending
     * with Militia in a 1/2 De Result, the Result is applied normally to the Militia alone).
     *
     * [6.2] LOYAL ROMAN COMBAT
     *
     * [6.21] Combat is voluntary for all loyal Roman units (legions and militia).
     *
     * [6.22] All loyal Roman units (legions and militia) may only attack during the Roman Combat Phase (see Sequence
     * of Play, 4.2).
     *
     * [6.23] Loyal Roman units that are in the same Area must be combined and attacked as a group.
     *
     * [6.24] Whenever there are two or more non-Roman groups of different forces in an Area, the Roman may attack one,
     * or more, or none of them.  They are never required to attack.  However, they may not attack more than once per
     * Roman Combat Phase.  Thus, if more than one different force group is to be attacked, their Strength Points are
     * added together for defensive purposes.
     *
     * [6.3] NON-ROMAN COMBAT
     *
     * [6.31] All non-Roman units must attempt to fight all Roman units in the same Area with them during the non-Roman
     * Combat Phase.  Two or more undestroyed Barbarian forces in the same Area might all attack (in separate attacks)
     * loyal Romans in the Area.  This is done during the Non-Roman and Non-Loyal Roman Combat Phase.
     *
     * [6.32]  All attacks against loyal Roman units by differing groups of non-Roman units in the same Area are rolled
     * for separately, the forces attacking in order of size, largest Strength Point value attacking first.
     *
     * [6.33] When a group of non-Roman units enters an Area containing non-Roman units of a different force they must
     * stop and have combat immediately with smallest group of non-Roman units of a different force already in that
     * Area.
     *
     * It would be possible for one force of Barbarians to attack twice in one Game-Turn; when they halted Movement
     * because of a hostile non-Roman or non-loyal Roman force (they would attack the smallest hostile force in the
     * Area from this group), and then to fight again (with the Loyal Romans) during their Combat Phase.
     *
     * [6.34] Non-Roman units that had combat in their Movement Phase may not attack any non-Roman units in the
     * following non-Roman Combat Phase.
     *
     * Barbarians may only attack Loyal Roman forces during the Non-Roman and Non-Loyal Roman Combat (Phase C of the
     * Sequence-of-Play).  Any fighting among hostile Non-Loyal Roman forces occurs during Movement.
     *
     * TODO [6.35] Whenever a group of non-Roman units entering an Area cannot attack the smallest group of non-Roman units
     * of a different force already in that Area, then the moving units must be attacked by those units.
     *
     * [6.36] Different types of Barbarian units, levies and raiding parties of the same nationality do not attack each
     * other when one enters an Area with the other.
     *
     * TODO [6.37] Any Persian unit eliminated in a Province that is not adjacent to a Persian-controlled Province may not be
     * brought back as a replacement and is lost for the remainder of the game.
     *
     * TODO [6.38] Non-Roman units that begin their Movement Phase in an Area with other non-Roman units of a different force
     * may not attack any of these forces unless those units are in their target “richest province.”  If so, they must
     * attack the smallest number of non-Roman units of a different force, and move no further.
     *
     * Non-Roman units that begin their Movement Phase in an area with other hostile non-Roman units may combine with
     * units of the same nationality which enter that area during the Movement Phase.  This combined force must attack
     * the hostile force sharing the area.  The original units which began in that area may be used to attack the
     * hostile force in such a situation.
     *
     * @param area
     * @param attacker
     * @param defenders
     */
    private void doNonRomanNonLoyalRomanCombat(Area area, Force attacker, List<Force> defenders){
        double defenderStrength = defenders.stream().mapToDouble(f -> f.getStrength()).sum();
        int ratio = (int) (attacker.getStrength() / defenderStrength);
        // Cannot have a ratio > 6
        if (ratio > 6)
            ratio = 6;
        // Don't allow attacks less than 1
        if (ratio == 0)
            return;
        CombatResult combatResult = CombatResultsTable.get(ratio);
        view.getGamePanel().getGameSidePanel().appendOutputLn(
                "Combat at " + area + " between " + attacker + " and " + defenders.get(0).getAllegiance() +
                        ": " + (combatResult != null? combatResult: "No Effect"));
        if (combatResult != null)
            applyCombatResult(area, attacker, defenders, combatResult);
    }

    /**
     * Attacks greater than 6-1 odds are treated as 6-1 odds; attacks at less than 1-1 odds are not permitted.  Combat
     * losses are extracted by order of preference from (1) Regular units; (2) Raiding Parties; (3) Militia units (see:
     * Combat, case 6.13).
     *
     * De = Defender eliminated; all of the defending Strength Points are eliminated, removed from the map
     *
     * 1/2 De = One-Half Defender eliminated; same as De, except only one-half of the defending Strength Points are
     * eliminated.  Note that when non-Roman units are attacking Roman legions, 1/2 De is treated as a no effect.  Roman
     * Militia units are affected by 1/2 De results.
     *
     * Ex = Exchange; all of the defending Strength Points and an equal number of attacking Strength Points
     * are eliminated.
     *
     * 1/2 Ex = One-half Exchange; all of the defending Strength Points, and one-half that number of attacking Strength
     * Points are eliminated.
     *
     * Fractional losses due to combat are rounded up.  For instance, 1/2 De for five Strength Points  means three
     * Points are lost.  1/2 De for one Strength Point means one Point is lost.
     *
     * TODO Persian Regulars, Roman Legions (loyal or rebellious) and Independent State regular units are eligible for
     * replacement as always when eliminated or exchanged (see 12.0).
     *
     * @param defenders
     */
    private void applyCombatResult(Area area, Force attacker, List<Force> defenders, CombatResult combatResult){
        // Combat losses are extracted by order of preference from (1) Regular; (2) Raiding Parties; (3) Militia
        // units in the same Area.  (Exception: 1/2 De Results do not affect loyal Roman legions.  If they are defending
        // with Militia in a 1/2 De Result, the Result is applied normally to the Militia alone).
        defenders.sort(new Comparator<Force>() {
            @Override
            public int compare(Force o1, Force o2) {
                if (o1.getUnitType() == o2.getUnitType())
                    return 0;
                return o1.getUnitType() == UnitType.REGULAR? -1:
                        o2.getUnitType() == UnitType.REGULAR? 1:
                                o1.getUnitType() == UnitType.RAIDING_PARTY? -1:
                                        o2.getUnitType() == UnitType.RAIDING_PARTY? 1: 0;
            }
        });
        switch (combatResult){
            case DEFENDER_ELINIMATED:
                for (Force defender: defenders){
                    if (defender.getAllegiance().isRoman() && defender.getUnitType() == UnitType.REGULAR){
                        model.getGame().addRomanReplacements(defender.getStrength());
                    }
                    else if (defender.getAllegiance() == Allegiance.PERSIAN && defender.getUnitType() == UnitType.REGULAR){
                        /*
                         * [12.24] Persian units eliminated while not adjacent to a Persian controlled Province, are lost permanently and
                         * may not be brought back as replacements.
                         */
                        if (model.getGame().getBoard().isAreaAdjacentToControlledProvince(area, Allegiance.PERSIAN))
                            model.getGame().addPersianReplacements(defender.getStrength());
                    }
                    defender.setStrength(0);
                }
                break;
            case HALF_DEFENDER_ELIMINATED:
                applyHalfDefenderEliminated(area, defenders);
                break;
            case EXCHANGE:
                applyExchange(area, attacker, defenders);
                break;
            case HALF_EXCHANGE:
                applyHalfExchange(area, attacker, defenders);
                break;
        }
    }

    /**
     * 1/2 De = One-Half Defender eliminated; same as De, except only one-half of the defending Strength Points are
     * eliminated.  Note that when non-Roman units are attacking Roman legions, 1/2 De is treated as a no effect.  Roman
     * Militia units are affected by 1/2 De results.
     *
     * @param defenders
     */
    private void applyHalfDefenderEliminated(Area area, List<Force> defenders){
        // Only count strength of non roman legions
        double defenderStrength =
                defenders.stream()
                        .filter(f -> !(f.getAllegiance() == Allegiance.ROMAN && f.getUnitType() == UnitType.REGULAR))
                        .mapToDouble(f -> f.getStrength())
                        .sum();
        int losses = (int) defenderStrength / 2;
        view.getGamePanel().getGameSidePanel().appendOutputLn("   " + defenders.get(0).getAllegiance() + " loses " + losses + " strength!");
        for (Force force: defenders){
            // Skip roman legions
            if (force.getAllegiance() == Allegiance.ROMAN && force.getUnitType() == UnitType.REGULAR){
                continue;
            }

            int strength = force.getStrength();
            int newStrength = strength >= losses? strength - losses: 0;
            force.setStrength(newStrength);
            losses = strength >= losses? 0: losses - strength;

            // If rebellious roman, add replacements
            if (force.getAllegiance().isRoman() && force.getUnitType() == UnitType.REGULAR){
                model.getGame().addRomanReplacements(strength - newStrength);
            }
            else if (force.getAllegiance() == Allegiance.PERSIAN && force.getUnitType() == UnitType.REGULAR){
                /*
                 * [12.24] Persian units eliminated while not adjacent to a Persian controlled Province, are lost permanently and
                 * may not be brought back as replacements.
                 */
                if (model.getGame().getBoard().isAreaAdjacentToControlledProvince(area, Allegiance.PERSIAN))
                    model.getGame().addPersianReplacements(strength - newStrength);
            }

            if (losses == 0)
                break;
        }
    }

    /**
     * Ex = Exchange; all of the defending Strength Points and an equal number of attacking Strength Points
     * are eliminated.
     *
     * @param attacker
     * @param defenders
     */
    private void applyExchange(Area area, Force attacker, List<Force> defenders){
        if (defenders.get(0).getAllegiance().isRoman()) {
            int legionStrength = defenders.stream().filter(f -> f.getUnitType() == UnitType.REGULAR).mapToInt(f -> f.getStrength()).sum();
            model.getGame().addRomanReplacements(legionStrength);
        }
        else if (defenders.get(0).getAllegiance() == Allegiance.PERSIAN){
            if (model.getGame().getBoard().isAreaAdjacentToControlledProvince(area, Allegiance.PERSIAN)){
                int regularStrength = defenders.stream().filter(f -> f.getUnitType() == UnitType.REGULAR).mapToInt(f -> f.getStrength()).sum();
                model.getGame().addPersianReplacements(regularStrength);
            }
        }

        int defenderStrength = defenders.stream().mapToInt(f -> f.getStrength()).sum();
        defenders.stream().forEach(f -> f.setStrength(0));
        attacker.setStrength(attacker.getStrength() - defenderStrength);
        view.getGamePanel().getGameSidePanel().appendOutputLn("   " + defenders.get(0).getAllegiance() + " eliminated; " + attacker + " loses " + defenderStrength + " strength!");

        if (attacker.getAllegiance().isRoman() && attacker.getUnitType() == UnitType.REGULAR){
            model.getGame().addRomanReplacements(defenderStrength);
        }
        else if (attacker.getAllegiance() == Allegiance.PERSIAN && attacker.getUnitType() == UnitType.REGULAR){
            model.getGame().addPersianReplacements(defenderStrength);
        }
    }

    /**
     * 1/2 Ex = One-half Exchange; all of the defending Strength Points, and one-half that number of attacking Strength
     * Points are eliminated.
     *
     * @param attacker
     * @param defenders
     */
    private void applyHalfExchange(Area area, Force attacker, List<Force> defenders){
        if (defenders.get(0).getAllegiance().isRoman()) {
            int legionStrength = defenders.stream().filter(f -> f.getUnitType() == UnitType.REGULAR).mapToInt(f -> f.getStrength()).sum();
            model.getGame().addRomanReplacements(legionStrength);
        }
        else if (defenders.get(0).getAllegiance() == Allegiance.PERSIAN){
            if (model.getGame().getBoard().isAreaAdjacentToControlledProvince(area, Allegiance.PERSIAN)){
                int regularStrength = defenders.stream().filter(f -> f.getUnitType() == UnitType.REGULAR).mapToInt(f -> f.getStrength()).sum();
                model.getGame().addPersianReplacements(regularStrength);
            }
        }

        int defenderStrength = (int) Math.ceil(defenders.stream().mapToInt(f -> f.getStrength()).sum() / 2.0);
        defenders.stream().forEach(f -> f.setStrength(0));
        attacker.setStrength(attacker.getStrength() - defenderStrength);
        view.getGamePanel().getGameSidePanel().appendOutputLn("   " + defenders.get(0).getAllegiance() + " eliminated; " + attacker + " loses " + defenderStrength + " strength!");

        if (attacker.getAllegiance().isRoman() && attacker.getUnitType() == UnitType.REGULAR){
            model.getGame().addRomanReplacements(defenderStrength);
        }
        else if (attacker.getAllegiance() == Allegiance.PERSIAN && attacker.getUnitType() == UnitType.REGULAR){
            model.getGame().addPersianReplacements(defenderStrength);
        }
    }

    /**
     * Barbarian Creation Phase.
     *
     * The Player rolls the die to create Barbarian forces.  These differ in number and nationality.  See non-loyal
     * Roman Force placement procedures (13.0), and the Barbarian Creation Table (16.0).
     */
    private void doBarbarianCreationPhase(){
        int rolls = BarbarianCreationTable.getBarbarianCreationRolls(model.getGame().getGamePeriod(), model.getGame().getTurn());
        for (int i = 0; i < rolls; ++i){
            ForcePlacement forcePlacement = BarbarianCreationTable.getBarbarianCreation();
            if (forcePlacement != null)
                placeNonRomanForce(forcePlacement);
        }
    }

    /**
     * PROCEDURE:
     * When attempting to place created Barbarians, Persian Replacements in Persia, or militia activated by an opposing
     * force or revolution use the following procedure to determine placement.
     *
     * CASES:
     * [13.1] SINGLE AREA PROVINCES
     * In single Area Provinces, there is no problem.  Simply place the units in the Area indicated.
     *
     * [13.2] MULTI-AREA PROVINCES
     * In multi-area Provinces, each letter-coded Area is assigned a digit on the die, e.g., A=1, B=2, C=3, D=4.  In
     * placing any group of non-Roman units, roll the die until a number representing one of the existing Areas results.
     * All the Strength Points are then placed in this Area.
     *
     * @param forcePlacement
     */
    private void placeNonRomanForce(ForcePlacement forcePlacement){
        view.getGamePanel().getGameSidePanel().appendOutputLn("Placing " + forcePlacement);
        Force force =
                new Force(forcePlacement.getAllegiance(), forcePlacement.getUnitType(), forcePlacement.getStrength());
        // Area is defined, add the force and return
        if (forcePlacement.getArea() != null){
            Area area = model.getGame().getBoard().getArea(forcePlacement.getProvince(), forcePlacement.getArea().charAt(0));
            area.addForce(force);
            return;
        }

        Province province = model.getGame().getBoard().getProvince(forcePlacement.getProvince());
        if (province.getAreas().size() == 1){
            province.getAreas().get(0).addForce(force);
            return;
        }

        int die = Util.roll() - 1;
        while(die >= province.getAreas().size()){
            die = Util.roll();
        }
        province.getAreas().get(die).addForce(force);
    }

    /**
     * Loyal Roman Movement Phase.
     *
     * The Player may move his loyal Legions and loyal Militia forces freely.  These units must stop moving if
     * entering an Area with opposing forces.
     */
    private void doLoyalRomanMovementPhase(){
        resetMovement(Allegiance.ROMAN);
        view.refresh();
        PopupUtil.popupNotification(view.getFrame(), "Loyal Roman Movement Phase", "Move Loyal Roman Forces");
    }

    /**
     * Loyal Roman Combat Phase.
     * Loyal Roman units (militia and/or Legions) may attack any one or combinations of opposing forces in the
     * same Area.
     */
    private void doLoyalRomanCombatPhase(){
        PopupUtil.popupNotification(view.getFrame(), "Loyal Roman Combat Phase", "Choose Areas to perform combat");
    }

    /**
     * Legion Rebellion Phase.
     * The Player rolls the die for each Area containing Royal Roman Legions which might rebel, depending on the
     * number of Strength Points.
     *
     * In the Legion Rebellion Phase of each Game-Turn, the Player examines the Areas on the mapsheet.  In each and
     * every Area containing four or more Loyal Roman Legions, he must roll for a possible rebellion.  This die roll is
     * modified for the period in which the Game-Turn falls, and the modified die roll is cross-indexed with the number
     * of Strength Points of Legion in the Area to determine if there is a rebellion, and its size.  A “*” indicates no
     * rebellion, while any number indicates the number of rebelling Strength Points.
     */
    private void doLegionRebellionPhase(){
        model.getGame().getBoard().getProvinces().forEach(p -> {
            p.getAreas().forEach(a -> {
                if (a.hasForce(Allegiance.ROMAN, UnitType.REGULAR, 4)){
                    int totalStrength =
                            a.getForces().stream()
                                    .filter(f -> f.getAllegiance() == Allegiance.ROMAN && f.getUnitType() == UnitType.REGULAR)
                                    .mapToInt(f -> f.getStrength())
                                    .sum();
                    int result = LegionRebellionTable.getNumRebellingLegions(model.getGame().getGamePeriod(), totalStrength);
                    if (result > 0){
                        doLegionRebellion(a, result);
                    }
                }
            });
        });
    }

    private void doLegionRebellion(Area area, int numRebellingLegions){
        view.getGamePanel().getGameSidePanel().appendOutputLn(numRebellingLegions + " Legions rebel in " + area);
        Force loyalRomanForce = area.getForce(Allegiance.ROMAN, UnitType.REGULAR);
        loyalRomanForce.setStrength(loyalRomanForce.getStrength() - numRebellingLegions);
        Force rebellingRomanForce = new Force(Allegiance.REBELLIOUS_ROMAN, UnitType.REGULAR, numRebellingLegions);
        area.addForce(rebellingRomanForce);
        PopupUtil.popupNotification(view.getFrame(), "Legion Rebellion Phase", numRebellingLegions + " legions in " + area + " are rebelling!");
    }

    /**
     * Control Determination Phase.  The Player determines which forces control an Area.
     *
     * The Roman Player must control Provinces on the map to win the game (see: Scenarios, 19.0).  Control is always
     * judged in the Control Determination Phase, never during any other portion of the Game-Turn.  Only one
     * nationality can control a province.
     *
     * PROCEDURE
     * The Romans control a Province at the end of a Game-Turn if :
     *    (1) The Province was originally part of the Empire and no hostile force has units in it, OR
     *    (2) The Roman Player began the game with control of the province and maintains Strength Points equal to
     *        one-third of the single largest opposing force of Strength Points in that Province, OR
     *    (3) They have, in a previous Game-Turn, lost control of the Province and they move in a number of Combat
     *        Strength Points superior to the number of Combat Strength Points of any one individual hostile force that
     *        is currently controlling the Province, OR
     *    (4) they did not originally control the Province and they have eliminated all of the Province's militia and
     *        have a number of Strength Points in the Province superior to any individual hostile force that is
     *        currently in the Province.
     *
     * In effect, the Roman forces must garrison all conquered Provinces, outside of the Provinces he controls at
     * the beginning of the Game, with at least one Combat Strength Point.
     *
     * But, even if a hostile forces moves into a Roman Province with no Legions, if it has Active
     * Militia (undestroyed), then the mere existence of that militia means the Province remains in Roman Control.  If
     * all the militia is eliminated, then when a Barbarian force moves into the Province, the Romans must immediately
     * (on that Game-Turn) move in Strength equal to one-third of the largest hostile force in the Province in order to
     * retain Control.  Once they lose Control, the Romans will have to move in Strength Points greater than the
     * largest hostile (non-Rebellious Legion) force in the Province in order to re-exert Control.
     *
     * CASES:
     *
     * [7.1] HOW NON-ROMAN FORCES CONTROL A PROVINCE
     *
     * [7.11] Non-Roman forces control a Province when
     *    (a) they have eliminated all hostile Militia Strength Points (if any), AND
     *    (b) have obtained a greater than three-to-one Strength Point superiority over Roman forces in the Province, AND
     *    (c) have superiority in Strength Points over any other hostile individual force in a given Province, AND
     *    (d) have Strength Points equal to at least the Victory Point value of the Province (not Persians).
     *
     * For Control, the Barbarians must have superiority of Strength Points over the largest hostile force except
     * Rebellious Roman Legions which do not affect Barbarian Control.
     *
     * Note: Persians are exempt from requirement “D”
     *
     * [7.12] Rebellious Roman legions cannot control a Province although they interfere with Roman control.  They do
     * not interfere with the non-Roman control of a Province.
     *
     * [7.13] A Province's control is disputed if neither side can fulfill its control conditions.
     *
     * [7.14] For any non-Roman force to gain control of an Independent State’s Province that force must eliminate the
     * Independent State's Regular and Militia units first and then fulfill any other necessary control requirements.
     *
     * [7.2] SPECIAL ROMAN LOSS OF CONTROL
     *
     * If rebellious Roman legions, or revolting militia, in any number, are in a Province during the Control
     * Determination Phase, the Roman does not control that Province that Game-Turn.
     */
    private void doControlDeterminationPhase(){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            determineControl(p);
        });
    }

    private void determineControl(Province province){
        if (romanControlled(province)) {
            if (province.getController() != Allegiance.ROMAN)
                view.getGamePanel().getGameSidePanel().appendOutputLn("Romans gain control of " + province);
            province.setController(Allegiance.ROMAN);
        }
        else {
            Allegiance newController = getNonRomanController(province);
            if (province.getController() != newController)
                view.getGamePanel().getGameSidePanel().appendOutputLn(newController + " gain control of " + province);
            province.setController(newController);
        }
    }

    /**
     * The Romans control a Province at the end of a Game-Turn if :
     *    (1) The Province was originally part of the Empire and no hostile force has units in it, OR
     *    (2) The Roman Player began the game with control of the province and maintains Strength Points equal to
     *        one-third of the single largest opposing force of Strength Points in that Province, OR
     *    (3) They have, in a previous Game-Turn, lost control of the Province and they move in a number of Combat
     *        Strength Points superior to the number of Combat Strength Points of any one individual hostile force that
     *        is currently controlling the Province, OR
     *    (4) they did not originally control the Province and they have eliminated all of the Province's militia and
     *        have a number of Strength Points in the Province superior to any individual hostile force that is
     *        currently in the Province.
     *
     * [7.2] SPECIAL ROMAN LOSS OF CONTROL
     * If rebellious Roman legions, or revolting militia, in any number, are in a Province during the Control
     * Determination Phase, the Roman does not control that Province that Game-Turn.
     *
     * @param province
     * @return
     */
    private boolean romanControlled(Province province){
        // Special loss of control
        if (province.hasForce(Allegiance.REBELLIOUS_ROMAN)){
            return false;
        }

        // (1)
        if (province.getOriginalController() == Allegiance.ROMAN && !province.hasOpposingForce(Allegiance.ROMAN))
            return true;

        // (2)
        int totalRomanStrength = province.getTotalForceStrength(Allegiance.ROMAN);
        Force largestOpposingForce = province.getLargestOpposingForce(Allegiance.ROMAN);
        int opposingStrength = largestOpposingForce.getStrength();
        if (province.getOriginalController() == Allegiance.ROMAN &&
                province.getController() == Allegiance.ROMAN &&
                totalRomanStrength >= (opposingStrength / 3))
            return true;

        // (3)
        Force smallestOpposingForce = province.getSmallestForce(province.getController());
        if (province.getOriginalController() == Allegiance.ROMAN &&
                province.getController() != Allegiance.ROMAN &&
                totalRomanStrength > smallestOpposingForce.getStrength())
            return true;

        // (4)
        if (province.getOriginalController() != Allegiance.ROMAN &&
                totalRomanStrength > smallestOpposingForce.getStrength() &&
                province.isMilitiaActivated()){
            List<Force> militia = province.getForces(province.getController(), UnitType.MILITIA);
            if (militia.isEmpty()){
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param province
     * @return
     */
    private Allegiance getNonRomanController(Province province){
        for (Allegiance allegiance: Allegiance.values()){
            if (allegiance == Allegiance.ROMAN || allegiance == Allegiance.REBELLIOUS_ROMAN)
                continue;
            if (hasNonRomanControl(allegiance, province))
                return allegiance;
        }
        return null;
    }

    /**
     * [7.11] Non-Roman forces control a Province when
     *    (a) they have eliminated all hostile Militia Strength Points (if any), AND
     *    (b) have obtained a greater than three-to-one Strength Point superiority over Roman forces in the Province, AND
     *    (c) have superiority in Strength Points over any other hostile individual force in a given Province, AND
     *    (d) have Strength Points equal to at least the Victory Point value of the Province (not Persians).
     *
     * For Control, the Barbarians must have superiority of Strength Points over the largest hostile force except
     * Rebellious Roman Legions which do not affect Barbarian Control.
     *
     * Note: Persians are exempt from requirement “D”
     *
     * [7.12] Rebellious Roman legions cannot control a Province although they interfere with Roman control.  They do
     * not interfere with the non-Roman control of a Province.
     *
     * [7.13] A Province's control is disputed if neither side can fulfill its control conditions.
     *
     * [7.14] For any non-Roman force to gain control of an Independent State’s Province that force must eliminate the
     * Independent State's Regular and Militia units first and then fulfill any other necessary control requirements.
     *
     * @param allegiance
     * @param province
     * @return
     */
    private boolean hasNonRomanControl(Allegiance allegiance, Province province){
        List<Force> forces = province.getForces(allegiance);
        if (forces.isEmpty())
            return false;

        // Independent State
        if (province.getController() == Allegiance.INDEPENDENT_STATE &&
                allegiance != Allegiance.INDEPENDENT_STATE &&
                province.isMilitiaActivated() &&
                province.getTotalForceStrength(province.getController()) > 0){
            return false;
        }

        // (a)
        if (province.getController() != allegiance && province.isMilitiaActive() && province.isMilitiaActivated()){
            List<Force> militia = province.getForces(province.getController(), UnitType.MILITIA);
            if (!militia.isEmpty()) {
                return false;
            }
        }

        // (b)
        int romanStrength = province.getTotalForceStrength(Allegiance.ROMAN);
        int myStrength = province.getTotalForceStrength(allegiance);
        if (myStrength < romanStrength * 3){
            return false;
        }

        // (c)
        Force largestOpposingForce = province.getLargestOpposingForce(allegiance, Allegiance.REBELLIOUS_ROMAN);
        if (myStrength <= largestOpposingForce.getStrength())
            return false;

        // (d)
        // have Strength Points equal to at least the Victory Point value of the Province (not Persians).
        if (myStrength < province.getVictoryPointAllowance() && allegiance != Allegiance.PERSIAN){
            return false;
        }

        return true;
    }

    /**
     * Barbarian Attrition Phase.
     */
    private void doBarbarianAttritionPhase(){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            doBarbarianAttrition(p);
        });
    }

    /**
     * Barbarian Attrition Phase.
     *
     * One-Half of each Barbarian (Germans, Scythians, Huns, Dacians, Tauricans, Picts)  force in a Province not
     * controlled by that force is eliminated (round fractional losses up).
     * Attrition losses are taken first from the Regular units, then from the Raiding Party units.
     * Note: Militia units are never affected by the Barbarian Attrition Phase, nor are Barbarian Regular units if in their Home Province.
     */
    private void doBarbarianAttrition(Province province){
        for (Allegiance allegiance: Allegiance.values()){
            if (!allegiance.isBarbarian()){
                continue;
            }

            if (province.getController() != allegiance &&
                    province.getOriginalController() != allegiance &&
                    province.getTotalForceStrength(allegiance) > 0){
                List<Force> regulars = province.getForces(allegiance, UnitType.REGULAR);
                List<Force> raidingParties = province.getForces(allegiance, UnitType.RAIDING_PARTY);
                int totalStrength = regulars.stream().mapToInt(f -> f.getStrength()).sum() + raidingParties.stream().mapToInt(f -> f.getStrength()).sum();
                int losses = totalStrength / 2;
                logger.info("[Barbarian Attrition Phase] Removing " + losses + " " + allegiance + " strength from " + province.getName());
                view.getGamePanel().getGameSidePanel().appendOutputLn(allegiance + " loses " + losses + " strength in " + province);
                for (Force regular: regulars){
                    if (regular.getStrength() > losses){
                        regular.setStrength(regular.getStrength() - losses);
                        losses = 0;
                    }
                    else {
                        losses -= regular.getStrength();
                        regular.setStrength(0);
                    }
                    if (losses == 0)
                        break;
                }

                if (losses > 0){
                    for (Force raidingParty: raidingParties){
                        if (raidingParty.getStrength() > losses) {
                            raidingParty.setStrength(raidingParty.getStrength() - losses);
                            losses = 0;
                        }
                        else {
                            losses -= raidingParty.getStrength();
                            raidingParty.setStrength(0);
                        }
                        if (losses == 0)
                            break;
                    }
                }
            }
        }
    }

    /**
     * Tax Collection and Disbursement Phase.
     *
     * The Player collects taxes for the Loyal Roman and Persian Provinces and adds them to the Treasury.  Tax Credits
     * are then withdrawn to pay active Strength Points, and purchase available replacements.
     *
     * The Roman and Persian forces must be able to pay their troops and replacements with tax credits at the end of the
     * Game-Turn.  In addition, the Romans have the option to "buy" Barbarians in the Barbarian Bribe Phase.
     *
     * PROCEDURE:
     *
     * Tax credits equal the sum of the total number of Victory Points of the controlled Provinces with no opposing
     * forces in the Province, and one-half the number of Victory Points of controlled Provinces with an opposing force.
     * These tax credits are added to the Treasury in the Tax Collection Phase.  Half-credits for tax purposes are
     * rounded down.
     *
     * CASES
     *
     * [11.1] ROMAN TREASURY
     *
     * [11.11] The Roman Player must pay each of the Loyal Roman Legion Strength Points on the map two Tax Credits per
     * Game-Turn during the Tax Collection Phase.
     *
     * [11.12] To replace an available Legion Strength Point, the Player must pay three Tax Credits during the Tax
     * Collection Phase of the Game-Turn in which the Legion will appear.
     *
     * [11.13] If the Roman Treasury does not have sufficient Tax Credits available in the Treasury to pay all Legion
     * Strength Points two Tax Credits apiece, certain Strength Points must be selected to not be paid.  Those not to be
     * paid must be chosen from the areas with the largest numbers of Strength Points.  For each Strength Point not
     * properly paid, a die is rolled.  A "1," "2," or "3" means the Legion Strength Point rebels; a "4," "5," or "6,"
     * the Legion stays loyal (and tightens its belt).
     *
     * [11.14] Priority must be given to paying all current Loyal Roman Legion Strength Points on the map their two Tax
     * Credits before introducing replacements.
     *
     * [11.15] No Tax Credits accrue the Romans for any Province whose Militia is in revolt, or that contains
     * rebelling legions.
     *
     *
     * [11.3] PERSIAN TREASURY
     *
     * [11.31] The Player must pay each of his Persian Regular Strength Points two Tax Credits each per Game-Turn.
     *
     * [11.32] To receive a unit, the Persian player must pay two Tax Credits per Strength Point on the Game-Turn for replacement.
     *
     * [11.33] Should the Persian not be able to pay any unit(s), the unpaid unit(s) are eliminated at the Player's choice.
     *
     * [11.34] The Persian treasury cannot be captured.
     */
    private void doTaxCollectionDisbursementPhase(){
        // Collect Taxes
        int romanTreasurePrior = model.getGame().getRomanTreasury();
        int persiansTreasuryPrior = model.getGame().getPersianTreasury();
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            if (p.getController() == Allegiance.ROMAN){
                if (p.getTotalForceStrength(Allegiance.REBELLIOUS_ROMAN, UnitType.REGULAR) > 0 || p.isMilitiaRebelling())
                    return;
                if (p.getLargestOpposingForce(Allegiance.ROMAN) != null)
                    model.getGame().adjRomanTreasury(p.getVictoryPointAllowance() / 2);
                else
                    model.getGame().adjRomanTreasury(p.getVictoryPointAllowance());
            }
            else if (p.getController() == Allegiance.PERSIAN){
                if (p.getLargestOpposingForce(Allegiance.PERSIAN) != null)
                    model.getGame().adjPersianTreasury(p.getVictoryPointAllowance() / 2);
                else
                    model.getGame().adjPersianTreasury(p.getVictoryPointAllowance());
            }
        });
        logger.info("[Tax Collection Phase] Roman Treasury is now " + model.getGame().getRomanTreasury());
        logger.info("[Tax Collection Phase] Persian Treasury is now " + model.getGame().getPersianTreasury());
        view.getGamePanel().getGameSidePanel().appendOutputLn("Romans collect " + (model.getGame().getRomanTreasury() - romanTreasurePrior) + " in taxes");
        view.getGamePanel().getGameSidePanel().appendOutputLn("Persians collect " + (model.getGame().getPersianTreasury() - persiansTreasuryPrior) + " in taxes");

        // Get Provinces with Roman Legions and sort by strength
        List<Province> provincesWithLegions =
                model.getGame().getBoard().getProvinces().stream()
                        .filter(p -> p.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR) > 0)
                        .sorted(new Comparator<Province>() {
                            @Override
                            public int compare(Province o1, Province o2) {
                                int o1s = o1.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR);
                                int o2s = o2.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR);
                                return o1s < o2s? -1: o1s > o2s? 1: 0;
                            }
                        })
                        .collect(Collectors.toList());

        // Pay upkeep for Roman Legions
        for (Province province: provincesWithLegions){
            for (Area area: province.getAreas()){
                Force legion = area.getForce(Allegiance.ROMAN, UnitType.REGULAR);
                if (legion == null)
                    continue;
                int upkeep = legion.getStrength() * 2;
                if (model.getGame().getRomanTreasury() >= upkeep){
                    model.getGame().adjRomanTreasury(-upkeep);
                }
                else {
                    // Cannot pay all of the legions, so pay what we can, and check the rest for rebellion
                    int strengthPaid = model.getGame().getRomanTreasury() / 2;
                    model.getGame().adjRomanTreasury(strengthPaid * -2);

                    int strengthNotPaid = legion.getStrength() - strengthPaid;
                    for (int i = 0; i < strengthNotPaid; ++i) {
                        if (Util.roll() <= 3) {
                            // Legion Rebels!
                            Force rebel = new Force(Allegiance.REBELLIOUS_ROMAN, UnitType.REGULAR, 1);
                            area.addForce(rebel);
                            logger.info("[Tax Collection Phase] Legion rebels in " + area + " (you cannot pay them)!");
                            view.getGamePanel().getGameSidePanel().appendOutputLn("Legions in " + area + " rebel (insufficient funds to pay them)!");
                        }
                    }
                }
            }
        }

        // Get Provinces with Persian Regulars and sort by strength
        List<Province> provincesWithPersians =
                model.getGame().getBoard().getProvinces().stream()
                        .filter(p -> p.getTotalForceStrength(Allegiance.PERSIAN, UnitType.REGULAR) > 0)
                        .sorted(new Comparator<Province>() {
                            @Override
                            public int compare(Province o1, Province o2) {
                                int o1s = o1.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR);
                                int o2s = o2.getTotalForceStrength(Allegiance.ROMAN, UnitType.REGULAR);
                                return o1s < o2s? -1: o1s > o2s? 1: 0;
                            }
                        })
                        .collect(Collectors.toList());

        // Pay upkeep for Persian Regulars
        for (Province province: provincesWithLegions){
            for (Area area: province.getAreas()){
                Force force = area.getForce(Allegiance.PERSIAN, UnitType.REGULAR);
                if (force == null)
                    continue;
                int upkeep = force.getStrength() * 2;
                if (model.getGame().getPersianTreasury() >= upkeep){
                    model.getGame().adjPersianTreasury(-upkeep);
                }
                else {
                    // Cannot pay all of the legions, so pay what we can, and check the rest for rebellion
                    int strengthPaid = model.getGame().getPersianTreasury() / 2;
                    model.getGame().adjPersianTreasury(strengthPaid * -2);

                    int strengthNotPaid = force.getStrength() - strengthPaid;
                    force.setStrength(force.getStrength() - strengthNotPaid);
                    logger.info("[Tax Collection Phase] " + strengthNotPaid + " Persian Regulars in " + area + " eliminated (cannot be paid)!");
                    view.getGamePanel().getGameSidePanel().appendOutputLn("Persian Regulars in " + area + " eliminated (insufficient funds to pay them)!");
                }
            }
        }
    }

    /**
     * Roman/Persian Replacement Phase.
     *
     * The Player attempts to replace lost Royal Roman Legions and Persian Regulars if they are due to return this Game
     * Turn, and sufficient tax credits exist in the Treasury.
     *
     * Persian replacement units must always be placed in Persia.  The die must be rolled to determine which Movement
     * Area they are placed in initially, as well as replacements.  Die results of 1,2 and 3 equal Areas A, B, and C
     * respectively.  Die results of 4,5, and 6 mean roll again.
     *
     *
     * The Romans and Persians have the ability to replace regular, non-Militia units lost in combat  Regardless of the
     * size of their respective Treasuries, Roman and Persian forces may never exceed the strength with which they began
     * the Scenario.  The Barbarians, Independent States and Rebellious Legions never receive replacements.
     *
     * PROCEDURE:
     *
     * When a Strength Point, Roman or Persian, is eliminated, it is removed from the map.  Two Game-Turns for the
     * Romans, five Game-Turns for the Persians after it was eliminated, the Strength Point becomes available to be
     * returned to play during the Roman/Persian Replacement Phase.
     *
     * CASES:
     *
     * [12.1] ROMAN REPLACEMENTS
     *
     * [12.11] To replace a legion Strength Point, the Roman must pay three Tax Credits per Strength Point on the
     * Game-Turn that it is replaced.  Thereafter, it is in all ways the same as an original legion.
     *
     * [12.12] Roman replacements arrive in Italia A, Thracia B, or Syria C during the Replacement Phase, at the
     * Player's discretion.  The Province must be Roman-Controlled for the Roman Replacements to arrive.
     *
     * [12.13] Replacements that cannot be paid cannot be placed on the map in that Game-Turn.  It may be returned at
     * some later Replacement Phase when Credits are available.
     */
    private void doRomanPersianReplacementPhase(){
        // Add Persian replacements
        doPersianReplacements();

        // Setup player with Roman replacements
        replacementsAvailable =
                model.getGame().getRomanReplacements().containsKey(model.getGame().getTurn())?
                        model.getGame().getRomanReplacements().get(model.getGame().getTurn()): 0;
        // TODO Enable Italia A, Thracia B, or Syria C (only if province is roman controlled)
        if (replacementsAvailable > 0)
            model.getGame().setPhaseStep(PhaseStep.PLACE_ROMAN_REPLACEMENTS);
        else
            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
    }

    /**
     * [12.2] PERSIAN REPLACEMENTS
     *
     * [12.21] To replace a unit, the Persian must pay two Tax Credits per Strength Point on the Game-Turn that it is
     * replaced.  Thereafter, it is in all ways the same as an original unit.
     *
     * [12.22] Persian Replacements arrive in Persia.
     *
     * [12.23] Replacements that cannot be paid cannot be placed on the map in that Game-Turn.  They must be returned in
     * the first subsequent Replacement Phase that credits are available.
     *
     * [12.24] Persian units eliminated while not adjacent to a Persian controlled Province, are lost permanently and
     * may not be brought back as replacements.
     *
     * [12.25] Persian replacements may not move out of Persia if they cannot reach the farthest advanced Persian troops
     * in one Movement Phase.  They may attack and defend normally in Persia.  They must move out of Persia if their
     * priority is upset by losing control of a previously controlled Province—they may move out of Persia to regain
     * control of a previously Persian controlled Province even if they cannot reach the lost Province in a single
     * Movement Phase.
     *
     * [12.26] If Persian forces do not control Persia, no Persian Replacements may arrive.
     */
    private void doPersianReplacements(){
        if (model.getGame().getBoard().getProvince(Province.PERSIA).getController() != Allegiance.PERSIAN){
            return;
        }
        int persianReplacements =
                model.getGame().getPersianReplacements().containsKey(model.getGame().getTurn())?
                        model.getGame().getPersianReplacements().get(model.getGame().getTurn()): 0;
        int numPlaced = 0;
        for (int i = 0; i < persianReplacements; ++i){
            // Pay for replacement
            if (model.getGame().getPersianTreasury() >= 2){
                model.getGame().adjPersianTreasury(-2);

                // Place replacement
                ForcePlacement forcePlacement = new ForcePlacement(Province.PERSIA, Allegiance.PERSIAN, UnitType.REGULAR, 1);
                placeNonRomanForce(forcePlacement);
                numPlaced += 1;
                view.getGamePanel().getGameSidePanel().appendOutputLn("Placed " + forcePlacement);
            }
        }

        model.getGame().getPersianReplacements().put(model.getGame().getTurn(), persianReplacements - numPlaced);
    }

    private void endRomanPersianReplacementPhase(){
        // Move unpurchased replacements (roman and persian) to next turn
        int leftOver = model.getGame().getPersianReplacements().containsKey(model.getGame().getTurn())?
                model.getGame().getPersianReplacements().get(model.getGame().getTurn()): 0;
        if (leftOver > 0){
            int nextTurn = model.getGame().getPersianReplacements().containsKey(model.getGame().getTurn() + 1)?
                    model.getGame().getPersianReplacements().get(model.getGame().getTurn() + 1): 0;
            model.getGame().getPersianReplacements().put(model.getGame().getTurn() + 1, nextTurn + leftOver);
        }

        leftOver = model.getGame().getRomanReplacements().containsKey(model.getGame().getTurn())?
                model.getGame().getRomanReplacements().get(model.getGame().getTurn()): 0;
        if (leftOver > 0){
            int nextTurn = model.getGame().getRomanReplacements().containsKey(model.getGame().getTurn() + 1)?
                    model.getGame().getRomanReplacements().get(model.getGame().getTurn() + 1): 0;
            model.getGame().getRomanReplacements().put(model.getGame().getTurn() + 1, nextTurn + leftOver);
        }
    }

    /**
     * Barbarian Bribe Phase.  The Player attempts to bribe Barbarian forces.
     *
     * [11.21] The Roman Treasury may "buy" Barbarians at the cost of one Tax Credit for each Barbarian Strength Point
     * bought.
     *
     * [11.22] "Bought" Barbarians may not move or attack loyal Roman legions in the Game-Turn following their purchase.
     * They may, however, defend against Roman attacks normally.
     *
     * [11.23] The Roman may "buy" any, or as many, Barbarians as he wishes, assuming he can afford it.
     *
     * [11.24] Barbarians of a particular type and force in the same Area must be bought as an integral whole.  The
     * Roman may not "buy" only part of any Barbarian group.
     */
    private void doBarbarianBribePhase(){
        if (model.getGame().getRomanTreasury() <= 0){
            model.getGame().setPhase(Phase.GAME_TURN_RECORD_PHASE);
            return;
        }

        model.getGame().setPhaseStep(PhaseStep.BRIBE_BARBARIANS);
    }

    /**
     * Game-Turn Record Phase.  The Player moves the Game-Turn Record Marker forward one grade on the Game-Turn Chart.
     */
    private void doGameTurnRecordPhase(){
        model.getGame().endTurn();
        model.getGame().setPhase(Phase.INTERNAL_REVOLUTION_PHASE);
    }
}
