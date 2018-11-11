/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a percentage of the direct damage back at attacker.
//used by season 5 heroes
public class Reflect extends SpecialAbility{
    
    private double multiplier;
    private long damageTakenThisRound;

    public Reflect(Creature owner, double multiplier) {
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
            enemyFormation.getFrontCreature().changeHP(-damageTakenThisRound*multiplier,enemyFormation);//elemental damage boost and defence not considered.
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Reflect(newOwner,multiplier);
    }
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Returns " + percent + "% of damage recieved";
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+multiplier));
    }
    
}
