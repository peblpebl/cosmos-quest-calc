/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import Formations.Levelable;

//increaces the owner's stats by a pseudo-random amount at the start of the fight.
//used by Dicemaster
public class RandomStatBoost extends SpecialAbility{//*activates before leprechaun*
    
    private int maxBoost;

    public RandomStatBoost(Creature owner, int maxBoost) {
        super(owner);
        this.maxBoost = maxBoost;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RandomStatBoost(newOwner,maxBoost);
    }
    
    
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        long boost = enemyFormation.getTurnSeed(0) % (maxBoost+1);
        //System.out.println(enemyFormation.getSeed() + " " + boost);
        owner.setBaseAtt((int)(owner.getBaseAtt()+boost));
        owner.setBaseHP((int)(owner.getBaseHP()+boost));
        owner.setCurrentAtt(owner.getBaseAtt());
        owner.setCurrentHP(owner.getBaseHP());
        
    }

    
    
    @Override
    public String getDescription() {
        return "Increace Att/HP by +0-" + maxBoost + " at start";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + maxBoost;
    }
    
    @Override
    public int viability() {// stats are not incremented until the fight begins, so take an average
        return (owner.getBaseHP() + maxBoost/2) * (owner.getBaseAtt() + maxBoost/2);
    }
    
    
    /* comment on chat room
    Yeah, uku. All the new skills work like that.
    Take the number ID of the monsters/heroes in the enemy lineup (Absolute value if theiy're negative),
    add one, then multiply it all for a Seed number.
    
    Gambit's "random" also includes which round it is (That's the factor it tests, 100 - current round to see if it will deal triple that round).
    
    seed = 1; for each slot in the row starting from the back and moving forward, seed = seed * abs(unit id) + 1
    empty slots have id of -1
    dice bonus is the seed % 21
    
    https://game276144.konggames.com/gamez/0027/6144/live/js/CosmosQuest.min.js?v=3.0.6.1
    */
}
