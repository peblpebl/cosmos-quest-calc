/*

 */
package Formations;

import Formations.Elements.Element;
import GUI.CreatureDrawer;
import SpecialAbilities.Nothing;
import java.awt.Graphics;


public class Monster extends Creature {

    public static final int TOTAL_UNIQUE_TIERS = 15;//have all monster names in arrays in CreatureFactory?
    public static final String[] REPEAT_TIER_STRING = new String[]{"","Furious "};
    public static final int TOTAL_TIERS = TOTAL_UNIQUE_TIERS * REPEAT_TIER_STRING.length;
    
    private long followers;
    private int tier;
    
    protected Monster(Element element, int baseAtt, int baseHP, long followers, int tier){
        super(element,baseAtt,baseHP);
        this.followers = followers;
        specialAbility = new Nothing(this);
        this.tier = tier;
    }

    @Override
    public Monster getCopy() {
        Monster m = new Monster(element,baseAtt,baseHP,followers,tier);
        m.setID(ID);
        m.currentAtt = currentAtt;
        m.currentHP = currentHP;
        m.specialAbility = specialAbility.getCopyForNewOwner(m);
        return m;
    }
    
    public int getTier(){
        return tier;
    }
    
    public static int getNumTimesRepeat(){
        return REPEAT_TIER_STRING.length;
    }
    
    @Override
    public int getID(){
        int elementNum = 0;
        switch(element){
            case AIR: elementNum = 0;break;
            case EARTH: elementNum = 1;break;
            case FIRE: elementNum = 2;break;
            case WATER: elementNum = 3;break;
        }
        return (tier-1) * Elements.MONSTER_ELEMENTS.length + elementNum;
    }
    
    @Override
    public long getFollowers(){
        return followers;
    }
    
    @Override
    public String getImageAddress() {
        int strIndex = (tier-1)/TOTAL_UNIQUE_TIERS;
        
        return "Creatures/Monsters/" + getName().substring(REPEAT_TIER_STRING[strIndex].length());//remove prefix
        
    }
    
    @Override
    public String toolTipText() {
        return "<html>" + getName() + "<br>Tier: " + tier + "</html>";
    }
    
    public int sortingValue(){
        return element.ordinal()*TOTAL_TIERS + tier;
    }
    
    @Override
    public Hero.Rarity getRarity() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }

    
    
}
