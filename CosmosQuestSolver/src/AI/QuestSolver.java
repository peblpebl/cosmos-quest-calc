/*

 */
package AI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import GUI.QuestSolverFrame;
import cosmosquestsolver.OtherThings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//contatins algorithm to search for a solution to beat a given formation.
//runs on a seperate thread
public class QuestSolver extends AISolver{
    
    protected QuestSolverFrame frame;//if a solution is found, this feild lets the frame know and sends the solution to it
    protected Formation enemyFormation;
    protected Hero[] prioritizedHeroes;
    
    
    public QuestSolver(QuestSolverFrame frame){
        this.frame = frame;
        triedCombinations = new HashSet<>();
    }
    
    //get parameters for the problem and performs search algorithm.
    //will not start a new search if one is already going
    @Override
    public void search(){
        obtainProblem();
        if (prioritizedHeroes.length > maxCreatures){
            tooManyPrioritizedCreaturesMessage();
            return;
        }
        
        bestComboPermu();
        if (searching){
            finished();
            searching = false;
        }
    }
    
    //asks the frame for the parameters of the problem
    protected void obtainProblem(){
        followers = frame.getFollowers();
        maxCreatures = frame.getMaxCreatures();
        heroes = frame.getHeroesWithoutPrioritization();
        prioritizedHeroes = frame.getPrioritizedHeroes();
        enemyFormation = frame.getEnemyFormation();
    }

    //gets a list of all creatures available, sorts them by viability, and
    //tests each possible combination until list is exhausted or solution is found.
    //tests formations most likeley to win (by heuristic) first
    protected void bestComboPermu(){
        LinkedList<Creature> creatureList = getCreatureList();
        sortCreatureList(creatureList);
        sendCreatureList(creatureList);
        tryCombinations(creatureList);
    }
    
    //sorts the list of creatures so that the combination iterator can select the combinations most
    //likely to produce a solution first. If hero viability outweighs monster viability, sorts by viability.
    //if monster viability is better, gives less priority to the stronger monsters since 6 weaker monsters
    //are usually better than one strong monster
    protected void sortCreatureList(LinkedList<Creature> creatureList){//debug for not all heroes being first over monsters*******
        int numStrongerHeroes = numHeroesStronger();//than average monster? 
        if (mindFollowers()){
            long averageFollowers = followers/(maxCreatures-numStrongerHeroes);
            Collections.sort(creatureList, (Creature c1, Creature c2) -> followerMinderViability(c2,averageFollowers)-followerMinderViability(c1,averageFollowers));
        }
        else{
            Collections.sort(creatureList, (Creature c1, Creature c2) -> strengthViability(c2)-strengthViability(c1));
        }
    }
    
    
    
    // for every combination of creatures provided by the combination iterator, test all permutations of that combination.
    // skip the combination if you don't have enough followers to use all the monsters, or that combination has been tried
    // before. Also checks if the user canceled the search each combination and stops if this is the case
    // this algorithm is designed to find a solution the user can afford, not nessesarily the most efficient solution.
    protected void tryCombinations(LinkedList<Creature> creatureList){
        int numCombinationCreatures = maxCreatures - prioritizedHeroes.length;
        long i = 0;//debug, for seeing what step of the list it's on
        int listNum = 1;//debug
        //calculates the number of combinations tried when it's time to include a new creature
        //int the search
        long nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);//debug
        
        boolean strengthMode = !mindFollowers();//don't need to check for duplicates if on strength mode
        
        Iterator<LinkedList<Creature>> combinations = new CombinationIterator(creatureList,numCombinationCreatures);
        while(combinations.hasNext() && searching){
            i ++;//debug
            if (i > nCrNum){//debug
                progressReport(listNum,creatureList);
                
                listNum ++;//debug
                nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);
            }//debug
            
            
            LinkedList<Creature> combo = combinations.next();
            for (Hero hero : prioritizedHeroes){
                combo.add(hero);
            }
            
            if (proceedWithPermutations(combo)){
                tryPermutationsAndMindDuplicates(strengthMode,combo);
            }
            
        }
        
    }
    
    protected void tryPermutationsAndMindDuplicates(boolean strengthMode, LinkedList<Creature> combo){
        if (canAffordMonsters(combo)){// did not notice a performance boost (memory saving?)
            if (strengthMode){
                tryPermutations(combo);
            }
            else if (!isDuplicateCombination(combo)){
                tryPermutations(combo);
                addToTriedCombos(combo);
            }
        }
    }

    //constructs a formation for every possible permutation of creatures in the given list
    //and see if any win against the enemy formation. If one is found, send the solution to the 
    //frame and stop
    protected void tryPermutations(LinkedList<Creature> list) {
        PermutationIterator<Creature> permutations = new PermutationIterator(list);//no linkedList<creature>?
        while(permutations.hasNext()){
            Formation currentFormation = new Formation(permutations.next());
            if (Formation.passed(currentFormation.getCopy(), enemyFormation.getCopy())){
                if (!Formation.passed(frame.getSolution().getCopy(), enemyFormation.getCopy())){//if there is already a solution from another thread, don't bother.
                    frame.recieveSolution(currentFormation);
                    searching = false;
                }
                
                return;//stops the solver from overwriting the solution so it is different than in the solution in the replay
                
            }
            
        }
    }
    
    

    protected void tooManyPrioritizedCreaturesMessage() {
        frame.recieveProgressString("Too many prioritized creatures");
    }

    protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
        frame.recieveProgressString("Search now including creature " + (listNum+1) + ": " + creatureList.get(listNum).getName());
    }

    protected boolean proceedWithPermutations(LinkedList<Creature> combo) {//overritten by sub class
        return true;
    }

    protected void finished() {
        frame.recieveDone();
    }

    protected void sendCreatureList(LinkedList<Creature> creatureList) {
        frame.recieveCreatureList(creatureList);
    }

    /*
    //for debugging, prints parameters
    private void test(){
        System.out.println("Followers: " + followers);
        System.out.println("Max Creatures: " + maxCreatures);
        System.out.println("Heroes: ");
        for (Hero hero: heroes){
            System.out.println("\t" + hero);
        }
        System.out.println("Enemy Formation: " + enemyFormation);
    }
    
    //for debugging. prints sorted creature list
    private void testList(List<Creature> creatureList) {
        for (int i = 0; i < creatureList.size(); i++){
            System.out.println(i + " " + creatureList.get(i));
        }
    }
    
*/
}
