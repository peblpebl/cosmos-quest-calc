/*

 */
package Formations;

import GUI.CreatureDrawer;
import SpecialAbilities.SpecialAbility;
import java.awt.Graphics;


public class Hero extends Creature implements Levelable{
    
    private int lvl1Att;//final?
    private int lvl1HP;

    private Rarity rarity;
    private int level = 1;
    
    public static enum Rarity{COMMON,RARE,LEGENDARY,ASCENDED};
    
    protected Hero(){
        //used for copying
    }

    protected Hero(Element element, int baseAtt, int baseHP, Rarity rarity, int ID, SpecialAbility specialAbility){
        super(element,baseAtt,baseHP);
        this.specialAbility = specialAbility;
        this.rarity = rarity;
        lvl1Att = baseAtt;
        lvl1HP = baseHP;
        this.ID = ID;
        //hero is leveled initially in creature factory, others leveled in getCopy()
    }
    
    public void attatchSpecialAbility() {
        specialAbility.setOwner(this);
    }
    
    @Override
    public Creature getCopy() {
        Hero hero = new Hero();
        
        hero.element = element;
        hero.specialAbility = specialAbility.getCopyForNewOwner(hero);
        hero.rarity = rarity;
        hero.lvl1Att = lvl1Att;
        hero.lvl1HP = lvl1HP;
        hero.levelUp(level);
        hero.currentAtt = currentAtt;//for sandbox
        hero.currentHP = currentHP;
        hero.baseHP = baseHP;
        hero.baseAtt = baseAtt;
        
        hero.ID = ID;
        
        return hero;
    }
    
    @Override
    public int getID(){
        return -ID - 2;//hero IDs are <= -2
    }
    
    
    @Override
    public void levelUp(int level) {
        this.level = level;
        this.baseHP = HPForLevel(level,rarity,lvl1Att,lvl1HP);
        this.baseAtt = attForLevel(level,rarity,lvl1Att,lvl1HP);
        currentHP = baseHP;//heroes will never level up mid-battle
        currentAtt = baseAtt;
        
    }
    
    //possible levels in the game currently
    public static boolean validHeroLevel(int level) {
        return ((level > 0 && level < 100) || level == 1000);
    }
    
    @Override
    public Rarity getRarity() {
        return rarity;
    }
    
    @Override
    public int getLevel() {
        return level;
    }
    
    public static int HPForLevel(int level, Rarity rarity, int baseAtt, int baseHP){
        return levelStat(level,rarity,baseHP,baseAtt);
    }
    
    public static int attForLevel(int level, Rarity rarity, int baseAtt, int baseHP){
        return levelStat(level,rarity,baseAtt,baseHP);
    }
    
    public static int levelStat(int level, Rarity rarity, int statWanted, int otherStat){
        double boost = (rarityStatBoost(rarity)*statWanted*(level-1)) / (double)(otherStat+statWanted);
        int intBoost = (int) (boost + 0.5);
        return statWanted + intBoost;
    }
    
    public static int rarityStatBoost(Rarity rarity){
        switch(rarity){
            case COMMON: return 1;
            case RARE: return 2;
            case LEGENDARY: return 6;
            case ASCENDED: return 12;
            default: return 0;
        }
    }
    
    public void setID(int ID){
        this.ID = ID;
    }
    
    @Override
    public int getLvl1Att() {
        return lvl1Att;
    }

    @Override
    public int getLvl1HP() {
        return lvl1HP;
    }
    
    @Override
    public long getFollowers() {
        return 0;
    }
    
    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }
    
    @Override
    public String getImageAddress() {
        return "Creatures/Heroes/" + getName();
    }
    
    @Override
    public String toolTipText() {
        return "<html>" + getName() + "<br>" + getSpecialAbility().getDescription() + "</html>";
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\tAtt: ");
        sb.append(currentAtt).append("\tHP: ");
        sb.append(currentHP).append("\tElement: ");
        sb.append(element).append("\tLevel: ");
        sb.append(level).append("\tID: ");
        sb.append(ID);
        
        return sb.toString();
    }
    
}
