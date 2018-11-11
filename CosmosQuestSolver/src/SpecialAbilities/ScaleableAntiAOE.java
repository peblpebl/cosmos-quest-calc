/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//decreaces AOE ability damage on formation by a specified percentage based on
//level. Used by Bubbles
public class ScaleableAntiAOE extends AntiAOE{
    
    
    public ScaleableAntiAOE(Creature owner, double amount) {
        super(owner,amount);
    }
    
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAntiAOE(newOwner,percent);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            double AOEResistance = percent * levelable.getLevel();
            if (AOEResistance > 1){
                AOEResistance = 1;
            }
            thisFormation.setAOEResistance(AOEResistance);
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            thisFormation.setAOEResistance(thisFormation.getAOEResistance() - percent * levelable.getLevel());
        }
    }

    
    
    @Override
    public String getDescription() {
        if (!(owner instanceof Levelable)){
            return "";
        }
        String percentStr = "";
        double times100 = percent * 100;
        if (times100 > 100){
                times100 = 100;
            }
        if (times100 % 1 == 0){
            percentStr = Integer.toString((int) times100);
        }
        else{
            percentStr = Double.toString(times100);
        }
        
        String percentLevelStr = "";
        Levelable l = (Levelable)owner;
        double times100Level = percent * 100 * l.getLevel();
        if (times100Level > 100){
                times100Level = 100;
            }
        if (times100Level % 1 == 0){
            percentLevelStr = Integer.toString((int) times100Level);
        }
        else{
            percentLevelStr = Double.toString(times100Level);
        }
        
        
        return "-" + percentStr + "% per level to area skills (" + percentLevelStr + "%)";
        
    }
    
    
    
    @Override
    public int viability() {
        Levelable l = (Levelable)owner;
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(percent*l.getLevel()*0.01)));
    }
    
}
