/*

 */
package cosmosquestsolver;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Elements;
import Formations.Formation;
import Formations.Hero;
import GUI.MenuFrame;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

// starts the program. Also has some test code for debugging
public class CosmosQuestSolver {


    public static void main(String[] args) throws FileNotFoundException {
        //test();
        new MenuFrame();
        //testFactory();
        //testFormation();
        //testSameMonsters();
        //testTournamentWinRate();
    }
    
    public static void test(){
        Hero[] heroes = CreatureFactory.getHeroes();
        for (Hero h : heroes){
            try {
                FileWriter f = new FileWriter("creature_data/heroes/" + h.getName() + ".txt");
                f.write("id " + h.getRawID() + "\n");
                switch (h.getRarity()){
                    case COMMON: f.write("rarity Common\n"); break;
                    case RARE: f.write("rarity Rare\n"); break;
                    case LEGENDARY: f.write("rarity Legendary\n"); break;
                    case ASCENDED: f.write("rarity Ascended\n"); break;
                    default:
                }
                
                f.write(Elements.getString(h.getElement()) + "\n");
                
                
                
                f.write("baseAtt " + h.getBaseAtt() + "\n");
                f.write("baseHP " + h.getBaseHP() + "\n");
                f.write("skill " + h.getSpecialAbility().getParseString() + "\n");
                f.write("tier1Att 0\n");
                f.write("tier2HP 0\n");
                f.write("tier4Stats 0\n");
                f.write("tier5Skill " + h.getSpecialAbility().getParseString());
                
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(CosmosQuestSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static Formation getThisFormation(){
        LinkedList<Creature> list = new LinkedList<>();
        
        
        list.add(CreatureFactory.getHero("Ascended Geum", 65));
        list.add(CreatureFactory.getHero("Thert", 99));
        list.add(CreatureFactory.getHero("Petry", 99));
        list.add(CreatureFactory.getHero("Werewolf", 99));
        list.add(CreatureFactory.getHero("Auri", 13));
        list.add(CreatureFactory.getHero("Christmas Elf", 27));
        
        
        
        
        //list.add(CreatureFactory.getMonster(Creature.Element.WATER, 19));
        //list.add(CreatureFactory.getHero("Liu Cheng", 1));
        //list.add(CreatureFactory.getHero("Dorth", 1));
        //list.add(CreatureFactory.getHero("Ascended Athos", 99));
        
        //list.add(CreatureFactory.getHero("Thert", 99));
        
        
        //list.add(CreatureFactory.getHero("Werewolf", 99));
        
        
        //list.add(CreatureFactory.getMonster(Creature.Element.FIRE, 10));
        
        

        return new Formation(list);
    }
    
    public static Formation getEnemyFormation(){
        LinkedList<Creature> list = new LinkedList<>();
        
        list.add(CreatureFactory.getWorldBoss("Lord of Chaos"));
        //list.add(CreatureFactory.getWorldBoss("Mother of all Kodamas"));
        //list.add(CreatureFactory.getMonster(Creature.Element.EARTH, 15));
        //list.add(CreatureFactory.getMonster(Creature.Element.EARTH, 15));
        //list.add(CreatureFactory.getMonster(Creature.Element.FIRE, 26));
        //list.add(CreatureFactory.getMonster(Creature.Element.AIR, 26));
        //list.add(CreatureFactory.getMonster(Creature.Element.WATER, 26));
        //list.add(CreatureFactory.getMonster(Creature.Element.WATER, 25));
        //list.add(CreatureFactory.getMonster(Creature.Element.EARTH, 27));
        
        //list.add(CreatureFactory.getMonster(Creature.Element.EARTH, 10));
        //list.add(CreatureFactory.getMonster(Creature.Element.FIRE, 1));
        //list.add(CreatureFactory.getMonster(Creature.Element.EARTH, 14));
        //list.add(CreatureFactory.getHero("Ascended Alpha", 99));
        //list.add(CreatureFactory.getHero("Bubbles", 99));
        
        return new Formation(list);
    }
    
    
    
    
    /*
    public static void testFormation(){
        LinkedList<Creature> list = new LinkedList<>();
        list.add(CreatureFactory.getMonster(Creature.Element.AIR, 1));
        list.add(CreatureFactory.getMonster(Creature.Element.AIR, 2));
        list.add(CreatureFactory.getHero("Hunter", 1));
        list.add(CreatureFactory.getHero("Hunter", 10));
        list.add(CreatureFactory.getHero("Geum", 3));
        list.add(CreatureFactory.getHero("Jet", 99));
        Formation f = new Formation(list);
        
        System.out.println(f);
    }
*/
    /*
    public static void testSameMonsters(){
        LinkedList<Creature> list1 = new LinkedList<>();
        LinkedList<Creature> list2 = new LinkedList<>();
        
        //list1.add(CreatureFactory.getHero("Geum", 1));
        list1.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        list1.add(CreatureFactory.getMonster(Creature.Element.EARTH, 1));
        //list1.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        //list1.add(CreatureFactory.getHero("Athos", 1));
        
        //list2.add(CreatureFactory.getHero("Hunter", 1));
        list2.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        list2.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        
        //list2.add(CreatureFactory.getHero("Geum", 1));
        //list2.add(CreatureFactory.getHero("Thert", 1));
        
        Formation f1 = new Formation(list1);
        Formation f2 = new Formation(list2);
        
        System.out.println("Formation 1:\n" + f1);
        System.out.println("Formation 2:\n" + f2);
        System.out.println("Same: " + f1.containsSameCreatures(f2));
    }

    private static void testTournamentWinRate() {
        LinkedList<Creature> list1 = new LinkedList<>();
        list1.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        list1.add(CreatureFactory.getMonster(Creature.Element.EARTH, 1));
        
        LinkedList<Creature> list2 = new LinkedList<>();
        //list2.add(CreatureFactory.getMonster(Creature.Element.AIR, 1));
        //list2.add(CreatureFactory.getMonster(Creature.Element.FIRE, 1));
        
        LinkedList<Creature> list5 = new LinkedList<>();
        list5.add(CreatureFactory.getMonster(Creature.Element.AIR, 1));
        list5.add(CreatureFactory.getMonster(Creature.Element.FIRE, 1));
        
        Formation f1 = new Formation(list1);
        Formation f2 = new Formation(list2);
        Formation f5 = new Formation(list5);
        
        LinkedList<Formation> g1 = new LinkedList<>();
        g1.add(f1);
        g1.add(f2);
        g1.add(f5);
        
        TournamentGrid grid1 = new TournamentGrid(g1);
        
        
        
        
        LinkedList<Creature> list3 = new LinkedList<>();
        list3.add(CreatureFactory.getMonster(Creature.Element.WATER, 1));
        list3.add(CreatureFactory.getMonster(Creature.Element.EARTH, 1));
        
        LinkedList<Creature> list4 = new LinkedList<>();
        list4.add(CreatureFactory.getMonster(Creature.Element.AIR, 1));
        list4.add(CreatureFactory.getMonster(Creature.Element.FIRE, 1));
        
        Formation f3 = new Formation(list3);
        Formation f4 = new Formation(list4);
        
        LinkedList<Formation> g2 = new LinkedList<>();
        g2.add(f3);
        g2.add(f4);
        
        TournamentGrid grid2 = new TournamentGrid(g2);
        
        System.out.println("Grid 1\n" + grid1);
        System.out.println("Grid 2\n" + grid2);
        System.out.println(grid1.numWins(grid2));
        
    }
    */
    
}
