/*

 */
package Formations;

import SpecialAbilities.SpecialAbility;
import java.awt.Graphics;

//has data and functions shared by all fighters. including monsters, heroes,
//and world bosses.
public abstract class Creature implements Comparable<Creature>{
    
    protected Element element;
    protected int baseHP;//hp the creatue starts off with
    protected int baseAtt;//attack the creature starts off with
    protected int attBoost = 0;
    protected int armor = 0;
    protected int currentHP;
    protected long currentAtt;
    protected SpecialAbility specialAbility;
    protected boolean facingRight = true;//for GUI. put in GUI class instead?
    protected int ID;//for quicker copying. Copying names of heroes was not nessesary for fights. Names can be accesed through a method in CreatureFactory.
    //also used for rng skills
    
    protected boolean performedDeathAction = false;//put in specialAbility?

    
    public static enum Element {AIR,WATER,EARTH,FIRE}//should this and ELEMENT_DAMAGE_BOOST be in its own class?
    public static final double ELEMENT_DAMAGE_BOOST = 1.5;
    
    protected Creature(){
        //used for copying
    }
    
    protected Creature(Element element, int baseAtt, int baseHP){
        this.element = element;
        this.baseHP = baseHP;
        this.currentHP = baseHP;
        this.baseAtt = baseAtt;
        this.currentAtt = baseAtt;
    }
    
    public abstract Creature getCopy();
    public abstract Hero.Rarity getRarity();// only used by heroes, defensive programming: monsters might have abilities in the future
    public abstract long getFollowers();
    public abstract void draw(Graphics g);
    public abstract String getImageAddress();
    public abstract String toolTipText();
    
    public String getName() {
        return CreatureFactory.getCreatureName(getID());
    }
    
    public int getID(){
        return ID;
    }
    
    public void setID(int ID){
        this.ID = ID;
    }
    
    public SpecialAbility getSpecialAbility(){
        return specialAbility;
    }
    
    public int getAttBoost(){
        return attBoost;
    }
    
    public int getArmor(){
        return armor;
    }
    
    public boolean isFacingRight(){
        return facingRight;
    }
    
    public void setFacingRight(boolean facingRight){
        this.facingRight = facingRight;
    }
    
    public int getBaseHP() {
        return baseHP;
    }
    
    public int getCurrentHP() {
        return currentHP;
    }
    
    public Element getElement() {
        return element;
    }
    
    public long getCurrentAtt() {
        return currentAtt;
    }
    
    public int getBaseAtt() {
        return baseAtt;
    }
    
    public void setBaseAtt(int att){
        baseAtt = att;
    }
    
    public void setBaseHP(int hp){
        baseHP = hp;
    }
    
    public void setCurrentAtt(long att) {
        currentAtt = att;
    }

    public void setCurrentHP(int HP) {
        currentHP = HP;
    }
    
    public void addAttBoost(int a){
        attBoost += a;
    }
    
    public void addArmorBoost(int a){
        armor += a;
    }
    
    public void resetBoosts(){
        attBoost = 0;
        armor = 0;
    }
    
    public char getElementChar(){
        switch (element){
            case AIR: return 'A';
            case WATER: return 'W';
            case EARTH: return 'E';
            case FIRE: return 'F';
            default: return 'A';
        }
    }
    
    public void attack(Formation thisFormation,Formation enemyFormation){
        specialAbility.attack(thisFormation,enemyFormation);
    }
    
    public boolean isDead(){
        return currentHP <= 0;
    }
    
    // the game's interpretation of creature strength. does not take into account
    // special abilities
    public int strength(){
        return (int) Math.ceil(Math.pow((baseAtt*baseHP), 1.5));
    }
    
    // a somewhat more accurate measurement of a creature's viability in battle
    public int viability(){
        return specialAbility.viability();
    }
    

    
    
    //actually changes HP directly
    public void changeHP(double damage, Formation thisFormation){
        if (currentHP == 0){
            return;//once dead, cannot be revived
        }
        
        int num;//Geum?
        if (damage < 0){//round away from 0
            num = (int)Math.floor(damage);
            thisFormation.addDamageTaken(-(long)Math.floor(damage));
        }
        else{
            num = (int) Math.ceil(damage);
        }
        
        currentHP += num;
        if (currentHP < 0){
            currentHP = 0;
        }
        if (currentHP > baseHP){
            currentHP = baseHP;
        }
    }
    
    
    public void takeAOEDamage(double damage, Formation thisFormation) {
        changeHP(-damage,thisFormation);
    }
    
    public double determineDamage(Creature target, Formation thisFormation, Formation enemyFormation){
        double damage = currentAtt + attBoost + specialAbility.extraDamage(enemyFormation,thisFormation);//change to currentAttack*** current att obsolete?
        damage = damageFromElement(damage,target.element) - target.getArmor();
        
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }
    
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        specialAbility.prepareForFight(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        specialAbility.startOfFightAction(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        specialAbility.startOfFightAction2(thisFormation, enemyFormation);
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {//reseting buffs done elsewhere
        specialAbility.preRoundAction(thisFormation,enemyFormation);
    }
    
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {//future special ability?
        specialAbility.takeHit(attacker, thisFormation, enemyFormation, hit);
    }

    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//AOE takes effect even when dead
        specialAbility.postRoundAction(thisFormation,enemyFormation);
    }
    
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        specialAbility.postRoundAction2(thisFormation,enemyFormation);
    }
    
    public void actionOnDeath(Formation thisFormation, Formation enemyFormation) {//is this needed? put in each specialAbility class?
        if (!performedDeathAction){
            specialAbility.deathAction(thisFormation, enemyFormation);
            performedDeathAction = true;
        }
    }
    
    public double damageFromElement(double baseDamage,Element elementAttacked){
        if (elementWeakness(elementAttacked) == element){
            return baseDamage * (ELEMENT_DAMAGE_BOOST + specialAbility.getElementDamageBoost());
        }
        else{
            return baseDamage;
        }
    }
    /*
    public static double damageFromElement(double baseDamage, Element elementAttacked, Element thisElement){
        if (elementWeakness(elementAttacked) == thisElement){
            return baseDamage * (ELEMENT_DAMAGE_BOOST + specialAbility.getElementDamageBoost());
        }
        else{
            return baseDamage;
        }
    }
    */
    @Override
    public int compareTo(Creature c) {
        return c.getID() - getID();
    }
    
    public static Element elementWeakness(Element e){
        switch(e){
            case AIR: return Element.EARTH;
            case WATER: return Element.AIR;
            case EARTH: return Element.FIRE;
            case FIRE: return Element.WATER;
            default: return null;
        }
    }
    
    public double elementDamageMultiplier(Element elementAttacked){
        if (elementWeakness(elementAttacked) == element){
            return ELEMENT_DAMAGE_BOOST + specialAbility.getElementDamageBoost();
        }
        else{
            return 1;
        }
    }
    
    public boolean isSameCreature(Creature c){
        return (c.getClass() == getClass() && c.getID() == getID());
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\tAtt: ");
        sb.append(currentAtt).append("\tHP: ");
        sb.append(currentHP).append("\tElement: ");
        sb.append(element);
        
        return sb.toString();
    }
    
    
    
}
