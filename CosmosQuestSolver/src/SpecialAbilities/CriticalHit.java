/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Pokerface
public class CriticalHit extends SpecialAbility{
    
    private double multiplier;
    private int turn = 0;

    public CriticalHit(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new CriticalHit(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation){//damage boost from other heroes stacks with crit
        if (true/*Math.abs(thisFormation.getTurnSeed(enemyFormation,turn)) % 2 == 1*/){
            //test this more with data?
            return (multiplier-1) * (owner.getCurrentAtt()+owner.getAttBoost()-(enemyFormation.getFrontCreature().getArmor()/owner.elementDamageMultiplier(enemyFormation.getFrontCreature().getElement())));
        }
        else{
            return 0;
        }
    }
    
    //double damage = currentAtt + attBoost + specialAbility.extraDamage(enemyFormation,thisFormation);//change to currentAttack*** current att obsolete?
    //damage = damageFromElement(damage,target.element) - target.getArmor();
    /*
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {
        if (Math.abs(thisFormation.getTurnSeed(enemyFormation,turn)) % 2 == 1){
            long originalAtt = owner.getCurrentAtt();
            owner.setCurrentAtt((long)(Math.ceil(originalAtt * multiplier)));
            super.attack(thisFormation,enemyFormation);
            owner.setCurrentAtt(originalAtt);
            //System.out.println("crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
        else{
            super.attack(thisFormation,enemyFormation);
            //System.out.println(" no crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
    }
    */
    
    //https://github.com/GizmoMar/C-Hero-Calc/commit/b9e0ecd3c9860d9dd0016ca6910b5f237c37affd
    
    @Override
    public String getDescription() {
        if ((int)multiplier == multiplier){
            return "Has a 50% chance to deal x" + (int)multiplier + " damage";
        }
        return "Has a 50% chance to deal x" + multiplier + " damage";
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * multiplier * 0.75);// more than average of extra damage damage because calc can find when he crits
    }
    
}
