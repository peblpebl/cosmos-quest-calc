/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//all heroes possess a special ability that affects them and/or their teammates
//in combat. Most special abilities have parameters, and many heroes share the same
//abilities, so these abilities are modular
public abstract class SpecialAbility {
    
    protected Creature owner;
    
    public SpecialAbility(Creature owner){
        this.owner = owner;
    }
    
    public void setOwner(Creature creature) {
        this.owner = creature;
    }

    //the following methods define what to do at each stage of a formation's attack cycle.
    //they are empty, but overwritten when needed
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        
    }
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation){
        
    }
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation){
        
    }
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        
    }
    public double extraDamage(Formation thisFormation, Formation enemyFormation){
        return 0;
    }
    public double getElementDamageBoost() {
        return 0;
    }
    public void attack(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeHit(owner,thisFormation);
    }
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {//future special ability?
        long longHit = (long)Math.ceil(hit);
        recordDamageTaken(longHit);
        attacker.getSpecialAbility().recordDamageDealt(longHit);
        owner.changeHP(-hit,thisFormation);
    }
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        return hit;
    }
    public void recordDamageDealt(long damage){//needed?
        
    }
    public void recordDamageTaken(long damage){
        
    }
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){//have 2 post actions? AOE then heal
    
    }
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation){//healing goes here
        
    }
    public void deathAction(Formation thisFormation, Formation enemyFormation){
        
    }
    
    public static int roundedScaleMilestone(Levelable owner, double amount, double milestone){
        return (int)((amount * (int)(owner.getLevel() / milestone)));
    }
    
    public static String roundedScaleMilestoneStr(Levelable owner, double amount, double milestone){
        return "(" + roundedScaleMilestone(owner,amount,milestone) + ")";
    }
    
    //heroes and special abilities have references to each other. This makes sure
    //there aren't any null pointers
    public abstract SpecialAbility getCopyForNewOwner(Creature newOwner);
    
    //currently not used because the tooltips blockes mouseListeners
    public abstract String getDescription();
    
    //special abilities can sometimes drastically increace a hero's effectiveness
    //in combat. This method provides a heuristic measurement for that effectiveness.
    //the base viability is attack * health
    public abstract int viability();

    

    
//Common - Earth - Billy (Zombie) - 40 Attack / 30 HP @ 1 - Deals 1 per 1 damage to first enemy on death.
//Rare - Water - Sanqueen (Female vampire) - 22 Attack / 88 HP @ 1 - Heals for 100% of damage dealt.

//Legendary - Air - Cliodahn (Banshee) - 60 Attack / 150 HP @ 1 - Gains attack equal to 100% of direct damage taken before armor.
    
    
}
