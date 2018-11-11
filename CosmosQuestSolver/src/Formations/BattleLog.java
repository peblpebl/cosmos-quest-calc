/*

 */
package Formations;

import java.util.LinkedList;


public class BattleLog {
    
    private LinkedList<BattleState> states;
    
    public BattleLog(Formation thisFormation, Formation enemyFormation) {//duplicate code. should be in Formation*
        states = Formation.getBattleSim(thisFormation, enemyFormation);
    }
    
    public BattleLog(){
        this(new Formation(),new Formation());
    }
    
    public BattleState getState(int i){
        return states.get(i);
    }

    public int length() {
        return states.size();
    }
    
    
}
