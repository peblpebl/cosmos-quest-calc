/*

 */
package AI;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import GUI.QuestSolverFrame;
import java.util.LinkedList;

//used by WeirdHeroQuestSolver
public class SpecialQuestSolver extends QuestSolver{
        
        private int originalMaxCreatures;
        private Hero weirdHero;
        private WeirdHeroQuestSolver parentSolver;
        
        public SpecialQuestSolver(QuestSolverFrame frame, WeirdHeroQuestSolver parentSolver, int maxCreatures, Hero weirdHero) {
            super(frame);
            this.parentSolver = parentSolver;
            this.maxCreatures = maxCreatures;
            originalMaxCreatures = frame.getMaxCreatures();
            this.weirdHero = weirdHero;
        }
        
        protected void obtainProblem(){
            followers = frame.getFollowers();
            // maxCreatures already known
            originalMaxCreatures = frame.getMaxCreatures();
            heroes = frame.getHeroesWithoutPrioritization();
            prioritizedHeroes = frame.getPrioritizedHeroes();
            enemyFormation = frame.getEnemyFormation();
        }
        /*
        @Override
        protected void bestComboPermu(){
            if (maxCreatures != Formation.MAX_MEMBERS && maxCreatures >= enemyFormation.size() ){
                return;
            }
            super.bestComboPermu();
    }
*/
        
        @Override
        protected void tooManyPrioritizedCreaturesMessage() {
            //nothing
        }
        
        @Override
        protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
            if (originalMaxCreatures == maxCreatures){
                frame.recieveProgressString("Biggest search now including " + (listNum+1) + ": " + creatureList.get(listNum).getName());
            }
        }
        
        @Override
        protected boolean proceedWithPermutations(LinkedList<Creature> combo) {
            if (combo.size() == originalMaxCreatures){
                return true;
            }
            else{
                for (Creature c : combo){
                    if (c.getName().equals(weirdHero.getName())){
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        protected void tryPermutations(LinkedList<Creature> list) {
            super.tryPermutations(list);
            if (!searching){
                parentSolver.stopSearching();
            }
        }
        
        @Override
        protected void finished() {
            if (originalMaxCreatures == maxCreatures){
                super.finished();
            }
        }
        
        @Override
        protected void sendCreatureList(LinkedList<Creature> creatureList) {
            if (originalMaxCreatures == maxCreatures){
                frame.recieveCreatureList(creatureList);
            }
        }
        
    }
