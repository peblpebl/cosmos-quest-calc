/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Hero;
import Formations.WorldBoss;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JPanel;

public class EnemyHeroesCustomizationPanel extends JPanel{
    
    private EnemySelectFrame frame;
    private EnemyFormationMakerPanel parent;
    
    private EnemyHeroCustomizationPanel[] heroPanelArray;
    private EnemyBossCustomizationPanel[] bossPanelArray;
    
    private HashMap<String,EnemyHeroCustomizationPanel> map;//for finding the right hero panel when loading

    public EnemyHeroesCustomizationPanel(EnemySelectFrame frame, EnemyFormationMakerPanel parent, int numColumns, boolean facingRight, boolean includeBosses, boolean load) {//include boolean for world bosses?
        this.frame = frame;
        this.parent = parent;
        
        setLayout(new GridLayout(0,numColumns));
        
        Hero[] heroes = CreatureFactory.getHeroes();
        heroPanelArray = new EnemyHeroCustomizationPanel[heroes.length];
        map = new HashMap<>();
        
        for (int i = 0; i < heroPanelArray.length; i++){
            heroes[i].setFacingRight(facingRight);
            heroPanelArray[i] = new EnemyHeroCustomizationPanel(frame,parent,heroes[i]);
            add(heroPanelArray[i]);
            map.put(heroes[i].getName(), heroPanelArray[i]);
        }
        if (includeBosses){
            WorldBoss[] bosses = CreatureFactory.getWorldBosses();
            bossPanelArray = new EnemyBossCustomizationPanel[bosses.length];
            for (int i = 0; i < bossPanelArray.length; i++){
                bosses[i].setFacingRight(facingRight);
                bossPanelArray[i] = new EnemyBossCustomizationPanel(frame,parent,bosses[i]);
                add(bossPanelArray[i]);
            }
        }
        
        int height = (AssetPanel.CREATURE_PICTURE_SIZE + HeroCustomizationPanel.CHANGE_PANEL_SIZE) * (int)(Math.ceil((heroPanelArray.length - 1) / numColumns));
        setPreferredSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        setMaximumSize(new Dimension(AssetPanel.HERO_SELECTION_PANEL_WIDTH,height));
        //setOpaque(false);
        
        if (load){
            load();
        }
        
    }
    
    private void load(){
        try{
            
            Scanner sc = new Scanner(new File("save data.txt"));
            sc.nextLine();//followers
            sc.nextLine();//max creatures
            
            String[] tokens;
            while (sc.hasNext()){
                
                tokens = sc.nextLine().split(",");
                map.get(tokens[0]).getLevelTextField().setText(tokens[1]);
                
                map.get(tokens[0]).levelHero();
            }
        }
        catch(Exception e){
            System.out.println("failed to load");
        }
    }
    
}
