package unsw.gloriaromanus;

<<<<<<< HEAD
public class StandardAI implements AI{
=======
public class StandardAI implements AI {
>>>>>>> b2a88603864968f5dad4faee9c5ac36d22207ed7
    /* buys the cheapest building possible - if multiple buildings of the same price then it goes in order of
    Ports (if on a sea province)
    Markets
    Farms
    Mines
    Roads
    Troop production buildings
    Smiths 
    Walls

    */
    public void buildInfrastructure(Faction f) {

    }

    /* 
    not sure how to choose which faction to recruit from
    recruits a unit
    first chooses a category:
    prioritises the category with the fewest units across all provinces
    if this is equal, the follow priority is applied:

    Spearmen
    Heavy infantry
    Missile infantry
    Cavalry
    Horse archers
    Artillery

    once a category is chosen, it prioritises the unit with the highest cost
    if this is equal, it will randomly choose a unit

    */
    public boolean recruitUnit(Faction f) {
        return true;
    }

    /* moves all troops not currently at a border province to the border province with the fewest soldiers
    if there are multiple, the province with the most enemy soldiers in the adjacent regions is prioritised
    if this is equal, AI moves troops towards a randomly chosen province

    the AI moves ALL soldiers in a province to the destination once it is chosen
    the province the AI moves units from is chosen to be the closest (fewest adjacent regions to cross in the shortest path) to the destination
    (remember destination may change during a turn depending on what moves are made)
    if there are multiple closest origins, the origin is randomly chosen

    Once the AI has picked a destination border province for some units, they will move to this province 
    (potentially across several turns) until reaching the destination.



    */
    public void moveUnits(Faction f) {

    }

    /*
    attacks a neigbouring province with the fewest units
    when this is equal or it can attack with multiple armies, it will attempt to attack using the army with most units
    if this is equal, it will attack from a randomly chosen province
    */

    public void attack(Faction f) {

    }
}
