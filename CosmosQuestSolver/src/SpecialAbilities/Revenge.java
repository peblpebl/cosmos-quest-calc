/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;

//increaces attack by a percent of damage taken (before armor).
//used by Cliodhna
public class Revenge extends SpecialAbility{//how does it work with Niel? Pokerface crit extra damage not counted?
    
    private double percent;
    //private long damageTakenThisRound;
    //private int armorThisRound;

    public Revenge(Creature owner, double multiplier) {
        super(owner);
        this.percent = multiplier;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Revenge(newOwner,percent);
    }
    
    private double damageGained(Creature attacker){
        double damage = attacker.getCurrentAtt() + attacker.getAttBoost();// + specialAbility.extraDamage(enemyFormation,thisFormation);//change to currentAttack*** current att obsolete?
        damage = Elements.damageFromElement(attacker,damage,owner.getElement());// - target.getArmor();
        
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }
    /*
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    */
/*
    @Override
    public void recordDamageTaken(long damage){//is this skill asymetric? (which side you're on matters)
        damageTakenThisRound = damage;
        armorThisRound = owner.getArmor();
    }
*/

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only increace damage by direct hits
            //owner.setCurrentAtt((int)(owner.getCurrentAtt() + (damageTakenThisRound + armorThisRound)*percent));//armor?
            owner.setCurrentAtt((int)(owner.getCurrentAtt() + damageGained(enemyFormation.getFrontCreature())));
        }
    }
    
    
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(this.percent * 100));
        return "Attack increaces by " + percent + "% of damage recieved";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * 2;
    }
    
}
