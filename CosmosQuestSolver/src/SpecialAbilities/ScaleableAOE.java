/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//deals a specified amount of AOE damage every turn, increasing linearly as
//level increaces. Used by Reindeer and Ascended Alpha
public class ScaleableAOE extends AOE{
        
    private double levelMilestone;
        
    public ScaleableAOE(Creature owner, int damage, double levelMilestone) {
        super(owner,damage);
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart && owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            enemyFormation.takeAOEDamage(roundedScaleMilestone(levelable,damage,levelMilestone));
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAOE(newOwner,damage,levelMilestone);
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
            return "After every turn, Deal " + damage + " aoe every level " + roundedScaleMilestoneStr((Levelable)owner,damage,levelMilestone);
        }
        else{
            return "After every turn, Deal " + damage + " aoe every " + milestoneStr + " levels " + roundedScaleMilestoneStr((Levelable)owner,damage,levelMilestone);
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * (damage * (int)(levelable.getLevel() / levelMilestone)) * Formation.MAX_MEMBERS);
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }
    
}
