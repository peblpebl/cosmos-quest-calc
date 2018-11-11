/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//deal a set amount damage at start of fight that scales with level. Used by Dr Hawking
public class ScaleableStartingDamage extends StartingDamage{
    
    private int levelMilestone;
    
    public ScaleableStartingDamage(Creature owner, int amount, int levelMilestone){
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableStartingDamage(newOwner,amount,levelMilestone);
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        Levelable levelable = (Levelable) owner;
        enemyFormation.takeAOEDamage((amount * (int)(levelable.getLevel() / levelMilestone)));
    }
    
    @Override
    public String getDescription() {
        if (!(owner instanceof Levelable)){
            return "";
        }
        if (levelMilestone == 1){
            return "At start, deal " + amount + " aoe per level " + roundedScaleMilestoneStr((Levelable)owner,amount,levelMilestone);
        }
        else{
            return "At start, deal " + amount + " aoe every " + levelMilestone + " levels " + roundedScaleMilestoneStr((Levelable)owner,amount,levelMilestone);
        }
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            return owner.getBaseHP() * (owner.getBaseAtt() + Formation.MAX_MEMBERS * (amount * (int)(levelable.getLevel() / levelMilestone)));
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }
    
}
