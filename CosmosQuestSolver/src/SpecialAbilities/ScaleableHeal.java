/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//heals a specified amount of hp every turn, increasing linearly as
//level increaces. Used by Christmas Elf and Ascended Auri
public class ScaleableHeal extends Heal{
        
    private double levelMilestone;
    
    public ScaleableHeal(Creature owner, int amount, double levelMilestone) {
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableHeal(newOwner,amount,levelMilestone);
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
        
        if (levelMilestone == 1){
            return "Heals formation " + amount + " HP every level, every turn " + roundedScaleMilestoneStr((Levelable)owner,amount,levelMilestone);
        }
        else{
            return "Heals formation " + amount + " HP every " + milestoneStr + " levels every turn " + roundedScaleMilestoneStr((Levelable)owner,amount,levelMilestone);
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * (amount * (int)(levelable.getLevel() / levelMilestone)) * Formation.MAX_MEMBERS/2);
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }

}
