/*

 */
package AI;

import Formations.Creature;
import Formations.Elements.Element;
import Formations.CreatureFactory;
import Formations.Elements;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import Formations.TournamentGrid;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class TournamentGridGenerator {
    
    //creates a semi-random grid based on my usual strategy for tournaments
    public static TournamentGrid createRandomGrid(LinkedList<Hero> sortedHeroes, long followers, int numRows, int maxCreatures){
        int numSuicideLanes = randNumSuicideLanes(numRows);
        int totalSpots = getNonSuicideSpots(numSuicideLanes,numRows,maxCreatures);
        
        int numHeroesToUse = numHeroesToUse(totalSpots,sortedHeroes,followers);
        LinkedList<Hero>[] heroSplit = pruneHeroes(numHeroesToUse,sortedHeroes);
        LinkedList<Hero> usedHeroes = heroSplit[0];//size = numHeroesToUse
        LinkedList<Hero> spareHeroes = heroSplit[1];
        //not all heroes used?
        
        LinkedList<Monster> monsterList = getMonstersToUse(totalSpots - numHeroesToUse,followers);
        
        LinkedList<Creature> creatureList = combineLists(usedHeroes,monsterList);
        
        Collections.shuffle(creatureList);
        
        TournamentGrid grid = gridFromList(creatureList,spareHeroes,numSuicideLanes,followers,numRows,maxCreatures);
        
        grid.shuffleRows();
        return grid;
        
    }
    
    //sacrificing some lanes to make the other lanes stronger is a common strategy in tournaments
    private static int randNumSuicideLanes(int numRows){
        
        if (numRows <= 2){
            return 0;
        }
        
        return (int)(Math.random() * numRows * 0.65);
    }
    
    //determines how many heroes to use out of the givin list. Some heroes may be too weak compared to follower monsters
    private static int numHeroesToUse(int totalSpots, LinkedList<Hero> heroList, long followersAvailable){//vary a little, random?
        
        if (heroList.isEmpty()){
            return 0;
        }
        int spotsAfterHeroes = totalSpots;//spots remaining after heroes are placed
        int numUsed = 0;
        
        Iterator<Hero> it = heroList.iterator();
        
        while(it.hasNext() && spotsAfterHeroes > 0){//more efficient mentod? dependent on strength formula
            Hero hero = it.next();
            int heroViability = hero.getSpecialAbility().viability();
            double monsterViability = Math.pow(followersAvailable/spotsAfterHeroes,0.66);
            //limit monster viability in case the number of followers is too high to use them all
            Monster strongestMonster = CreatureFactory.getStrongestMonster();
            if (monsterViability > strongestMonster.viability()){
                monsterViability = strongestMonster.viability();
            }
            
            if (heroViability > monsterViability){
                spotsAfterHeroes --;
                numUsed ++;
                
            }
            
        }
        
        if(numUsed == heroList.size()){
            return numUsed;
        }
        
        int randomOffsetNumUsed = numUsed + randomDeviation();
        if (randomOffsetNumUsed >= 0 && randomOffsetNumUsed <= heroList.size()){
            return randomOffsetNumUsed;
        }
        else{
            return numUsed;
        }
        
            
    }
    
    //gives some randomness in how many heroes are used
    private static int randomDeviation(){
        int num = 0;
        while (Math.random() < 0.4){
            if (Math.random() < 0.5){
                num ++;
            }
            else{
                num --;
            }
        }
        
        return num;
    }
    
    //returns a list of monsters to use in a randomly generated grid
    private static LinkedList<Monster> getMonstersToUse(int spotsToFill, long followersAvailable){
        if (spotsToFill == 0){//when follower count is abysmal and heroes are excessive
            return new LinkedList<Monster>();
        }
        
        LinkedList<Monster> monsterList = CreatureFactory.getAvailableMonsters(followersAvailable/spotsToFill);//make a field to draw from?*
        
        if (monsterList.isEmpty()){//too few follows to fill the grid even with lowest tier monsters
            for (Element e : Elements.MONSTER_ELEMENTS){
                monsterList.add(CreatureFactory.getMonster(e,1));//generate lowest tier monsters
            }
        }
        
        Collections.sort(monsterList, (Monster m1, Monster m2) -> (int)m2.getFollowers()-(int)m1.getFollowers());
        
        LinkedList<Monster> list = getWeightedRandomMonsters(monsterList, spotsToFill,followersAvailable);
        
        LinkedList<Monster> upgradedList = upgradeMonsters(list,followersAvailable,0);
        
        
        return upgradedList;
    }
    
    private static LinkedList<Monster> getWeightedRandomMonsters(LinkedList<Monster> monsterList, int spotsToFill, long followersAvailable){//index, if out of bounds, choose cheapest monster
        long followersLeft = followersAvailable;
        LinkedList<Monster> list = new LinkedList<>();
        //selects a random monster from the list and adds it if you have enough followers
        for(int i = 0; i < spotsToFill; i++){
            
            int index = getRandomMonsterIndex(monsterList.size());
            Monster m = monsterList.get(index);
            
            //check if you can afford the monster, if not end
            followersLeft -= m.getFollowers();
            if (followersLeft <= 0){
                return list;
            }
            
            list.add(m);
            
        }
        return list;
    }
    
    private static int getRandomMonsterIndex(int num){
        //if monster pool is small, return random number
        int cutoffPoint = 2*Elements.MONSTER_ELEMENTS.length + 1;
        if (num <= cutoffPoint){
            return (int)(Math.random()*num);
        }
        
        
        if (Math.random() < 0.9){//usually, pick monsters at the front of the list (strongest)
            return (int)(Math.random()*cutoffPoint);
        }
        else{//rarely, pick other weaker monsters
            return cutoffPoint + getRandomMonsterIndex(num - cutoffPoint);
        }
        
    }
    
    //upgrades as many monsters as possible with a given list. Also tries to add monsters
    //to fill any empty spaces.
    private static LinkedList<Monster> upgradeMonsters(LinkedList<Monster> list, long followersAvailable, int blankSpaces){//method alters input parameters*
        
        //find how many followers you have left after using all the monsters on the list
        long currentFollowers = followersAvailable;
        for (Monster m : list){
            currentFollowers -= m.getFollowers();
        }
        
        //fill in any blank spaces with tier 1 monsters
        while(blankSpaces > 0 && currentFollowers >= CreatureFactory.getMinFollowersForTier1Monsters()){
            Element element = Element.values()[(int) (Math.random() * Element.values().length)];
            Monster m = CreatureFactory.getMonster(element, 1);
            list.add(m);
            currentFollowers -= m.getFollowers();
            blankSpaces --;
        }//add kodamas and whisps?
        while(blankSpaces > 0 && currentFollowers >= CreatureFactory.getCheapestMonster().getFollowers()){
            Monster m = CreatureFactory.getCheapestMonster();
            list.add(m);
            currentFollowers -= m.getFollowers();
            blankSpaces --;
        }
        
        //convert to array
        Monster[] monsterArray = new Monster[list.size()];
        int index = 0;
        for (Monster m : list){
            monsterArray[index] = m;
            index ++;
        }
        
        //upgrade monsters as much as possible with remaining followers
        boolean upgradedSomething = true;
        while(upgradedSomething){
            upgradedSomething = false;
            
            for (int i = 0; i < monsterArray.length; i++){
                if (monsterArray[i].getTier() == Monster.TOTAL_TIERS){
                    continue;
                }
                Monster nextTierMonster = CreatureFactory.getMonster(monsterArray[i].getElement(), monsterArray[i].getTier() + 1);
                if (nextTierMonster.getFollowers() - monsterArray[i].getFollowers() < currentFollowers){
                    currentFollowers -= (nextTierMonster.getFollowers() - monsterArray[i].getFollowers());
                    monsterArray[i] = nextTierMonster;
                    upgradedSomething = true;
                }
            }
            
            
        }
        
        //convert back to linked list
        LinkedList<Monster> upgradedList = new LinkedList();
        for (Monster m : monsterArray){
            upgradedList.add(m);
        }
        return upgradedList;
        
    }
    
    //returns all monsters from a list of creatures
    public static LinkedList<Monster> extractMonsters(List<Creature> creatures){
        LinkedList<Monster> list = new LinkedList<>();
        for (Creature c : creatures){
            if (c instanceof Monster)
            list.add((Monster) c);
        }
        return list;
    }
    
    // upgrades monsters given a list that has creatures in it. repeat code here?
    private static void upgradeCreatures(LinkedList<Creature> creatures, long followersAvailable) {
        long currentFollowers = followersAvailable;
        for (Creature c : creatures){
            if (c instanceof Monster)
            currentFollowers -= c.getFollowers();
        }
        
        LinkedList<Monster> list = extractMonsters(creatures);
        
        //convert to array
        Monster[] monsterArray = new Monster[list.size()];
        int index = 0;
        for (Monster m : list){
            monsterArray[index] = m;
            index ++;
        }
        
        boolean upgradedSomething = true;
        while(upgradedSomething){
            upgradedSomething = false;
            
            for (int i = 0; i < monsterArray.length; i++){
                if (monsterArray[i].getTier() == Monster.TOTAL_TIERS){
                    continue;
                }
                Monster nextTierMonster = CreatureFactory.getMonster(monsterArray[i].getElement(), monsterArray[i].getTier() + 1);
                if (nextTierMonster.getFollowers() - monsterArray[i].getFollowers() < currentFollowers){
                    currentFollowers -= (nextTierMonster.getFollowers() - monsterArray[i].getFollowers());
                    monsterArray[i] = nextTierMonster;
                    upgradedSomething = true;
                }
            }
            
            
        }
        
        //convert back to linked list
        LinkedList<Monster> upgradedList = new LinkedList();
        for (Monster m : monsterArray){
            upgradedList.add(m);
        }
        
        //merge creatre list with new Monster list
        int creatureIndex = 0;
        int newMonsterIndex = 0;
        while(creatureIndex < creatures.size() && newMonsterIndex < upgradedList.size()){
            if (creatures.get(creatureIndex) instanceof Monster){
                creatures.set(creatureIndex, upgradedList.get(newMonsterIndex));
                newMonsterIndex ++;
            }
            creatureIndex ++;
        }
        
        
    }

    
    //returns a heroes that should and shouldn't be included in the grid
    private static LinkedList<Hero>[] pruneHeroes(int numHeroesToUse, LinkedList<Hero> heroList) {
        LinkedList<Hero>[] list = new LinkedList[2];
        list[0] = new LinkedList<>();
        list[1] = new LinkedList<>();
        int heroesLeft = numHeroesToUse;
        for (Hero hero : heroList){
            if (heroesLeft > 0){
                list[0].add(hero);
                heroesLeft --;
            }
            else{
                list[1].add(hero);
            }
            
        }
        return list;
    }
    
    private static LinkedList<Creature> combineLists(LinkedList<Hero> prunedHeroes, LinkedList<Monster> monsterList) {
        LinkedList<Creature> list = new LinkedList<>();
        for (Hero hero : prunedHeroes){
            list.add(hero);
        }
        for (Monster monster : monsterList){
            list.add(monster);
        }
        return list;
    }
    
    private static int getNonSuicideSpots(int numSuicideLanes, int numRows, int maxCreatures){
        return (numRows-numSuicideLanes) * maxCreatures;
    }
    
    //makes a grid from a list of creatures
    private static TournamentGrid gridFromList(LinkedList<Creature> creatureList, LinkedList<Hero> spareHeroes, int numSuicideLanes, long followers, int numRows, int maxCreatures) {//keep track of followers used, dump other heroes and spare followers in suicide lane
        
        
        TournamentGrid grid = new TournamentGrid();
        long currentFollowers = followers;
        int numLanesNotSuicided = numRows - numSuicideLanes;
        
        //adds the list of heroes to the non-suicide grids in order. the list was randomized earlier.
        for (int i = 0; i < numLanesNotSuicided; i++){//even distribution of heroes?
            LinkedList<Creature> list = new LinkedList<>();
            for (int j = 0; j < maxCreatures; j++){
                if (creatureList.isEmpty()){
                    break;//no more creatures, so skip. more efficient way? this won't happen often. sometimes, a suicide lane will have one creature
                }
                Creature c = creatureList.poll();
                list.add(c);
                currentFollowers -= c.getFollowers();
            }
            grid.addFormation(new Formation(list));
        }
        
        //fill in suicideLanes with remainder (recursive)
        if (numSuicideLanes != 0){
            TournamentGrid sGrid = createRandomGrid(spareHeroes,currentFollowers,numSuicideLanes,maxCreatures);
            for (Formation f : sGrid.getFormations()){
                grid.addFormation(f);
            }
        }
        return grid;
    }

    public static TournamentGrid newAlteredGrid(TournamentGrid grid, long totalFollowers, int maxCreatures, int numChanges) {
        TournamentGrid newGrid;
        
        if (Math.random() < 0.5){
            newGrid = swapPlaces(grid);
        }
        else{//change monsters
            if (Math.random() < 0.5){
                newGrid = degradeMonster(grid,totalFollowers,maxCreatures);
            }
            else{
                newGrid = changeMonsterElement(grid,totalFollowers,maxCreatures);
            }
        }
        
        //recursive: change multiple times
        if (numChanges <= 1){
            return newGrid;
        }
        else{
            return newAlteredGrid(newGrid,totalFollowers,maxCreatures,numChanges-1);
        }
    }
    
    private static TournamentGrid swapPlaces(TournamentGrid grid){
        //convert grid into list
        LinkedList<Creature> list = listFromGrid(grid);
        
        if (list.size() < 2){//needs at least 2 to swap places
            return grid;
        }
        
        //pick 2 random indexes to swap
        int randomIndex1 = (int) (Math.random() * list.size());
        int randomIndex2;
        do {
             randomIndex2 = (int) (Math.random() * list.size());
        }
        while(randomIndex1 == randomIndex2);
        
        Creature c1 = list.get(randomIndex1);
        Creature c2 = list.get(randomIndex2);
        list.set(randomIndex2, c1);
        list.set(randomIndex1, c2);
        
        TournamentGrid newGrid = gridFromList(list,null,0,0,grid.getFormations().size(),grid.getFormations().get(0).size());
        newGrid.shuffleRows();
        return newGrid;
        
    }
    
    private static LinkedList<Creature> listFromGrid(TournamentGrid grid){
        LinkedList<Creature> list = new LinkedList<>();
        for (Formation f : grid.getFormations()){
            for (Creature c : f){
                list.add(c);
            }
        }
        return list;
    }
    
    private static TournamentGrid changeMonsterElement(TournamentGrid grid, long totalFollowers, int maxCreatures){
        
        //get list of monsters
        LinkedList<Creature> creatures = listFromGrid(grid);
        if (!hasMonsters(creatures)){
            return newAlteredGrid(grid,totalFollowers,maxCreatures,1);//try something else
        }
        
        //select index of monster to change
        int randomIndex;
        do {
            randomIndex = (int)(Math.random()*creatures.size());
        }
        while (!(creatures.get(randomIndex) instanceof Monster));
        
        //change element of random mosnter
        Monster selectedMonster = (Monster)creatures.get(randomIndex);
        Element element = randomElementReplace(selectedMonster);
        //get a monster of the different element, same tier
        Monster newMonster = CreatureFactory.getMonster(element, selectedMonster.getTier());
        //fringe case: if you can't afford to change a monster, try again or keep as is
        if (newMonster.getTier() == 1 && selectedMonster.getFollowers() < newMonster.getFollowers()){
            return newAlteredGrid(grid,totalFollowers,maxCreatures,1);//try again
        }
        //downgrade monster until it has less followers than the selected moster
        while (selectedMonster.getFollowers() < newMonster.getFollowers()){
            newMonster = CreatureFactory.getMonster(element, newMonster.getTier()-1);
        }
        
        creatures.set(randomIndex, newMonster);
        
        upgradeCreatures(creatures,totalFollowers);
        
        TournamentGrid newGrid = gridFromList(creatures,null,0,0,grid.getFormations().size(),grid.getFormations().get(0).size());
        newGrid.shuffleRows();
        return newGrid;
    }
    
    private static TournamentGrid degradeMonster(TournamentGrid grid, long totalFollowers, int maxCreatures){
        
        //get list of monsters
        LinkedList<Creature> creatures = listFromGrid(grid);
        if (!hasMonsters(creatures)){
            return newAlteredGrid(grid,totalFollowers,maxCreatures,1);//try something else
        }
        
        //select index of monster to change
        int randomIndex;
        do {
            randomIndex = (int)(Math.random()*creatures.size());
        }
        while (!(creatures.get(randomIndex) instanceof Monster));
        
        int numberOfTiersLower = (int) (Math.random()*2.2) + 1;
        
        Monster selectedMonster = (Monster)creatures.get(randomIndex);
        if (selectedMonster.getTier() <= numberOfTiersLower){
            return newAlteredGrid(grid,totalFollowers,maxCreatures,1);//try something else
        }
        
        
        //get a lower tier monster
        Monster newMonster = CreatureFactory.getMonster(selectedMonster.getElement(), selectedMonster.getTier()-numberOfTiersLower);
        //System.out.println("degraded monster from " + selectedMonster.getName() + " to " + newMonster.getName());
        
        creatures.set(randomIndex, newMonster);
        
        upgradeCreatures(creatures,totalFollowers);
        
        TournamentGrid newGrid = gridFromList(creatures,null,0,0,grid.getFormations().size(),grid.getFormations().get(0).size());
        newGrid.shuffleRows();
        
        return newGrid;
    }
    
    private static boolean hasMonsters(LinkedList<Creature> creatures){
        for (Creature c : creatures){
            if (c instanceof Monster){
                return true;
            }
        }
        return false;
    }
    
    //return a random element that is not the same element as the given monster
    private static Element randomElementReplace(Monster m){
        Element element;
        do {
            element = Element.values()[(int)(Math.random()*Element.values().length)];
        }
        while(element == m.getElement());
        return element;
    }

    public static TournamentGrid deleteLepMonsters(TournamentGrid grid, long totalFollowers, int maxCreatures) {
        TournamentGrid copy = grid.getCopy();
        for (Formation f : copy.getFormations()){
            if (f.contains(CreatureFactory.getHero("Leprechaun", 1)) && f.containsLepHeroes()){
                f.removeMonsters();
                break;//only one leprechaun exists, so stop after finding it
            }
        }
        LinkedList<Monster> monsters = extractMonsters(listFromGrid(copy));
        //testList(monsters);
        monsters = upgradeMonsters(monsters,totalFollowers,0);//does this change the mosnters or the copy?******
        //testList(monsters);
        //System.out.println("*****************************************");
        return copy;
    }
    
    private static void testList(LinkedList<Monster> l){
        for (Monster m : l){
            System.out.println(m.getName());
        }
        long totalFollowers = 0;
        for (Monster m : l){
            totalFollowers += m.getFollowers();
        }
        System.out.println("Followers:" + totalFollowers);
        System.out.println("___________");
    }
    
    
}
