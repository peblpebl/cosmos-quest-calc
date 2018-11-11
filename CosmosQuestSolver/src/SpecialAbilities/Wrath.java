/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals damage to enemy on death.
//Used by Billy
public class Wrath extends SpecialAbility{
    
    private int damage;
    private boolean active = false;

    public Wrath(Creature owner, int damage) {
        super(owner);
        this.damage = damage;
    }
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Wrath(newOwner,damage);
    }
    
    

    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {
        super.takeHit(attacker, thisFormation, enemyFormation, hit);
        if ( owner.isDead() && thisFormation.getFrontCreature() == owner){
            active = true;
        }
    }
    
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){//have 2 post actions? AOE then heal
        if (active){
            activateAbility(enemyFormation);
        }
    }
    
    private void activateAbility(Formation enemyFormation) {
        enemyFormation.getFrontCreature().changeHP(-damage,enemyFormation);
    }
    
    @Override
    public String getDescription() {
        return "Deals " + damage + " damage after dying"; //amount?
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt()+damage/2);
    }
    
}
