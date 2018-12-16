/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//deals a specified amount of AOE damage to enemy formation and heals own
//formation by the same amount turn, increasing linearly as level increaces.
//Used by Santa Claus and Tiny
public class ScaleableLifeSteal extends LifeSteal{
        
    private double levelMilestone;
    
    public ScaleableLifeSteal(Creature owner, int amount, double levelMilestone) {
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart && owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            enemyFormation.takeAOEDamage(roundedScaleMilestone(levelable,amount,levelMilestone));
        }
        
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableLifeSteal(newOwner,amount,levelMilestone);
    }
    
    

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart && owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            thisFormation.AOEHeal(roundedScaleMilestone(levelable,amount,levelMilestone), enemyFormation);
        }
    }
    
    @Override
    public String getDescription() {
        if (!(owner instanceof Levelable)){
            return "";
        }
        String milestoneStr = "";
        if (levelMilestone % 1 == 0){
            milestoneStr = Integer.toString((int)levelMilestone);
        }
        else{
            milestoneStr = Double.toString(levelMilestone);
        }
        
        return "+" + amount + " aoe & heal every " + milestoneStr + " lvl" + " " + roundedScaleMilestoneStr((Levelable)owner,amount,levelMilestone);
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * (amount * (int)(levelable.getLevel() / levelMilestone)) * Formation.MAX_MEMBERS * 2);
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }

}
