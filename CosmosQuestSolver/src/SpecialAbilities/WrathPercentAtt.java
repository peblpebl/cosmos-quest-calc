/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals AOE damage to enemy formation equal to a specified percentage of the
//user's base attack upon death. Used by season 2 heroes
public class WrathPercentAtt extends SpecialAbility{
    
    private double multiplier;

    public WrathPercentAtt(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new WrathPercentAtt(newOwner,multiplier);
    }
    

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {//what if AOE killed him?***
        if (thisFormation.getFrontCreature() == owner){
            activateAbility(enemyFormation);
        }
    }
    
    private void activateAbility(Formation enemyFormation) {
        enemyFormation.takeAOEDamage(Math.round(owner.getBaseAtt() * multiplier));
    }
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Deals " + percent + "% attack as aoe after dying (" + Integer.toString((int)(Math.round(owner.getBaseAtt() * multiplier))) + ")"; //amount?
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (int)(owner.getBaseAtt() * multiplier * Formation.MAX_MEMBERS);
    }
    
}
