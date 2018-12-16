/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import Formations.Levelable;

//at the beggining of the battle, adjests the stats of the owner as if its level
// was multiplied by a specified amount. Used by the Easter event heroes
public class StatLevelBoost extends SpecialAbility{
    
    private double multiplier;//edit stat gain on rarity type function instead?

    public StatLevelBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new StatLevelBoost(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        if (owner instanceof Levelable){
            int[] newStats = getNewStats((Levelable) owner);
            //owner.setBaseAtt(newStats[0]);
            owner.setMaxHP(newStats[1]);
            owner.setCurrentAtt(newStats[0]);
            owner.setCurrentHP(owner.getMaxHP());
        }
    }
    
    private int[] getNewStats(Levelable l){
        int newLevel = (int)(((l.getLevel() - 1) * multiplier) + 1);//rounding for non-integers?
        int att = l.getLvl1Att();
        int HP = l.getLvl1HP();
        return new int[]{Hero.attForLevel(newLevel, owner.getRarity(),att,HP),Hero.HPForLevel(newLevel, owner.getRarity(),att,HP)};
        
    }
    
    @Override
    public String getDescription() {
        String multString = "";
        if (multiplier % 1 == 0){
            multString = Integer.toString((int)multiplier);
        }
        else{
            multString = Double.toString(multiplier);
        }
        
        if (owner instanceof Levelable){
            int[] newStats = getNewStats((Levelable) owner);
            return "Stats gained per level x" + multString + " (" + newStats[0] + "," + newStats[1] + ")";
        }
        else{
            return "Stats gained per level x" + multString;
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//returns viability at the owner's effective level
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            int newLevel = (int)(((levelable.getLevel() - 1) * multiplier) + 1);
            int baseAtt = levelable.getLvl1Att();
            int baseHP = levelable.getLvl1HP();

            int att = Hero.attForLevel(newLevel, owner.getRarity(), baseAtt, baseHP);
            int HP = Hero.HPForLevel(newLevel, owner.getRarity(), baseAtt, baseHP);

            return att * HP;
        }
        return owner.getBaseAtt() * owner.getBaseAtt();
    }
    
}
