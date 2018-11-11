/*

 */

package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attacking the creature at the head of the formation will also damage a specified
//number of creatures behind the head. Damage done to each hero is reduced by a
//specified percentage every time it hits a creature, so those near the back
//of the formation will recieve significantly reduced damage. Used by season 3 and 4
//heroes, Taint, Sexy Santa, James, and Ascented TR1N0X
public class Ricochet extends SpecialAbility{
    
    private double multiplier;
    private int numBounces;
    private boolean deadOnStart;//fringe case where lep and/or Hawking kills them at the start

    public Ricochet(Creature owner, double multiplier, int numBounces) {
        super(owner);
        this.multiplier = multiplier;
        this.numBounces = numBounces;
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        deadOnStart = owner.isDead();
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        return 0;
    }

    
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {
        super.attack(thisFormation,enemyFormation);
        if (!deadOnStart){
            int bouncesLeft = numBounces;

            for (int i = 1; i < enemyFormation.size(); i ++){//front creature was already attacked
                Creature creature = enemyFormation.getCreature(i);
                
                if (bouncesLeft > 0){
                    double rictDamage = Math.floor(owner.determineDamage(creature, thisFormation, enemyFormation)
                                                    * (1 - enemyFormation.getAOEResistance())
                                                    * Math.pow(multiplier, i));//round down?
                    creature.changeHP(-rictDamage,enemyFormation);
                    bouncesLeft --;
                }
                else{
                    break;
                }
            }
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Ricochet(newOwner,multiplier,numBounces);
    }
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        if (numBounces == 1){
            return "Ricochet " + percent + "% damage to next unit";
        }
        else{
            return "Ricochet " + percent + "% damage to next " + numBounces + " units";
        }
        
    }
    
    @Override
    public int viability() {
        int att = owner.getBaseAtt();
        for (int i = 0; i < numBounces; i++){
            att += 2 * owner.getBaseAtt() * (int)Math.pow(multiplier, i+1);//2: richochet damage is less likeley to overflow and be wasted
        }
        return owner.getBaseHP() * att;
    }
    
}








/*
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

attacking the creature at the head of the formation will also damage a specified
number of creatures behind the head. Damage done to each hero is reduced by a
specified percentage every time it hits a creature, so those near the back
of the formation will recieve significantly reduced damage. Used by season 3 and 4
heroes, Taint, Sexy Santa, James, and Ascented TR1N0X
public class Ricochet extends SpecialAbility{
    
    private double multiplier;
    private int numBounces;
    private long damageDealtThisRound;
    private boolean deadOnStart;fringe case where lep and/or Hawking kills them at the start

    public Ricochet(Creature owner, double multiplier, int numBounces) {
        super(owner);
        this.multiplier = multiplier;
        this.numBounces = numBounces;
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageDealtThisRound = 0;
        deadOnStart = owner.isDead();
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        return 0;
    }

    @Override
    public void recordDamageDealt(long damage){
        damageDealtThisRound = damage;
    }
    
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {
        super.attack(thisFormation,enemyFormation);
        if (!deadOnStart){
            int bouncesLeft = numBounces;
            double damage = (double) Math.floor(damageDealtThisRound * (1-enemyFormation.getAOEResistance()));rounding accurate?

            for (Creature creature : enemyFormation){
                if(creature != enemyFormation.getFrontCreature()){front creature was already attacked
                    if (bouncesLeft > 0){
                        damage *= multiplier;
                        creature.changeHP(-damage,enemyFormation);
                        bouncesLeft --;
                    }
                    else{
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Ricochet(newOwner,multiplier,numBounces);
    }
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        if (numBounces == 1){
            return "Ricochet " + percent + "% damage to next unit";
        }
        else{
            return "Ricochet " + percent + "% damage to next " + numBounces + " units";
        }
        
    }
    
    @Override
    public int viability() {
        int att = owner.getBaseAtt();
        for (int i = 0; i < numBounces; i++){
            att += 2 * owner.getBaseAtt() * (int)Math.pow(multiplier, i+1);2: richochet damage is less likeley to overflow and be wasted
        }
        return owner.getBaseHP() * att;
    }
    
}
*/