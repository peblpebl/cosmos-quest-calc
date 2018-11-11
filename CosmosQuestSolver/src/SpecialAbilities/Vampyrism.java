/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//heals a percentage of damage dealt.
//used by Sanqueen
public class Vampyrism extends SpecialAbility{ //rounding?
    
    private double percent;
    private long damageDealtThisRound;
    protected boolean deadOnStart;

    public Vampyrism(Creature owner, double multiplier) {
        super(owner);
        this.percent = multiplier;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Vampyrism(newOwner,percent);
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageDealtThisRound = 0;
        deadOnStart = owner.isDead();
    }
    
    @Override
    public void recordDamageDealt(long damage){
        damageDealtThisRound = damage;
    }

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            owner.changeHP(damageDealtThisRound * percent, enemyFormation);
        }
    }
    
    
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(this.percent * 100));
        return "Heals for " + percent + "% of damage dealt";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * 2;
    }
    
}
