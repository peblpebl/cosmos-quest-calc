/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Luxurius Maximus
public class RandomTarget extends SpecialAbility{

    private int turn = 0;
    
    public RandomTarget(Creature owner) {
        super(owner);
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RandomTarget(newOwner);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
    }
    
    //Bubbles currently dampens lux damage if not targeting first according to game code, interaction should be added if this doesn't change
    //does element advantage apply to target or front monster? I think front monster
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {//attacks a "random" enemy
        int position = (int)(thisFormation.getTurnSeed(enemyFormation, turn) % enemyFormation.size());
        if (position < 0 || position >= enemyFormation.size()){
            System.out.println("out of bounds!");
        }
        
        enemyFormation.takeHit(owner,thisFormation,position);
    }
    
    @Override
    public String getDescription() {
        return "Attacks a random unit";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
    //turnSeed = (opposingCondition.seed + (101 - turncounter)*(101 - turncounter)*(101 - turncounter)) % (int64_t)round((double)opposingCondition.seed / (101 - turncounter) + (101 - turncounter)*(101 - turncounter));
    
}
