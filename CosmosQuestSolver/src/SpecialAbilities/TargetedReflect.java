/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a percentage of the direct damage back at creature with most HP.
//used by season Guy
public class TargetedReflect extends SpecialAbility{
    
    private double multiplier;
    private long damageTakenThisRound;

    public TargetedReflect(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    

    @Override
    public void recordDamageTaken(long damage){//is this skill asymetric? (which side you're on matters)
        damageTakenThisRound = damage;
        
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            enemyFormation.getCreature(mostHPPosition(enemyFormation)).changeHP(-damageTakenThisRound*multiplier,enemyFormation);//elemental damage boost and defence not considered.
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new TargetedReflect(newOwner,multiplier);
    }
    
    private int mostHPPosition(Formation enemyFormation){
        int mostHPIndex = 0;
        long greatestHP = 0;
        
        for (int i = 0; i < enemyFormation.size(); i++){
            Creature c = enemyFormation.getCreature(i);
            long cHP = c.getCurrentHP();//currentHP, what if two units have the same HP?
            
            if (cHP > greatestHP){
                greatestHP = cHP;
                mostHPIndex = i;
            }
        }
        return mostHPIndex;
    }
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Returns " + percent + "% of damage recieved to enemy with most HP";
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1.5+multiplier)) ;
    }
    
}
