/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;

//multiplies the damage done to a creature by a specified amount if the creature
//and the owner have the same element. Damage is multiplied before attack boosts
//are added to it. Used by Pontus, Atzar, Rigr, Dagda, and their ascended counterparts
public class Purity extends SpecialAbility{
    
    private double multiplier;

    public Purity(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        if (enemyFormation.getFrontCreature().getElement() == owner.getElement()){
            return (owner.getBaseAtt()+owner.getAttBoost()) * (multiplier - 1);
        }
        else{
            return 0;
        }
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Purity(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        String numStr = "";
        if (multiplier == 2){
            numStr = "Double";
        }
        else if (multiplier == 3){
            numStr = "Triple";
        }
        else{
            numStr = "x " + multiplier;
        }
        return numStr + " attack against same element";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return owner.getBaseHP() * owner.getBaseAtt() * (int)(1 + (multiplier-1)/Elements.numElements());
    }
    
}
