/*

 */
package Formations;

import cosmosquestsolver.OtherThings;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;


public class BattleLog {
    
    private Formation leftFormation;
    private Formation rightFormation;
    private LinkedList<BattleState> states;
    
    public BattleLog(Formation thisFormation, Formation enemyFormation) {//duplicate code. should be in Formation*
        this.leftFormation = thisFormation.getCopy();
        this.rightFormation = enemyFormation.getCopy();
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
    
    
//Like here input this: eyJzZXR1cCI6Wy0xMDMsLTY3LC0xMDIsLTEyMiwtNzEsLTEyMF0sInNoZXJvIjp7IjEyMCI6ODgsIjExOCI6OTksIjEwMSI6OTksIjEwMCI6OTksIjY5Ijo5OSwiNjUiOjk5fSwicGxheWVyIjpbLTI4LDExOSwxMTcsMTE3LDExOF0sInBoZXJvIjp7IjI2IjoxMDAwfX0

//Translates to {"setup":[-103,-67,-102,-122,-71,-120],"shero":{"120":88,"118":99,"101":99,"100":99,"69":99,"65":99},"player":[-28,119,117,117,118],"phero":{"26":1000}}

    public String getBattleCode() throws UnsupportedEncodingException {
        String battleStr = getBattleString();
        return OtherThings.encodeBase64(battleStr);
    }
    
    
    public String getBattleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"left\":\"Left\",\"right\":\"Right\",\"title\":\"Simulation\",");
        sb.append("\"setup\":").append(leftFormation.idStr()).append(",");
        sb.append("\"shero\":").append(leftFormation.heroLevelStr()).append(",");
        sb.append("\"player\":").append(rightFormation.idStr()).append(",");
        sb.append("\"phero\":").append(rightFormation.heroLevelStr()).append("}");
        return sb.toString();
    }

    //String battleCode = "eyJsZWZ0IjoiTWUiLCJyaWdodCI6IkVuZW1pZ28iLCJ0aXRsZSI6IkZpZ2h0Iiwic2V0dXAiOls0NywtMTExLDQ3LDQ1LDQ2LC0xMDRdLCJzaGVybyI6eyIxMDkiOjEsIjEwMiI6NTd9LCJwbGF5ZXIiOlstNTksLTYwLC0yMSwtMzEsNDQsNDZdLCJwaGVybyI6eyI1NyI6OTksIjU4Ijo5OSwiMTkiOjk5LCIyOSI6OTl9fQo=";
    //{"left":"Me","right":"Enemigo","title":"Fight","setup":[47,-111,47,45,46,-104],"shero":{"109":1,"102":57},"player":[-59,-60,-21,-31,44,46],"phero":{"57":99,"58":99,"19":99,"29":99}}
    
}
