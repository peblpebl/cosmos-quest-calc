/*

 */
package GUI;

import AI.AISolver;
import AI.WorldBossOptimizer;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.WorldBoss;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

// gui for world boss damage optimization
public class WorldBossOptimizerFrame extends JFrame implements ISolverFrame{
    private JPanel backgroundPanel;
    private JPanel topPanel;
    private AssetPanel assetPanel;
    private WorldBossSelectionPanel worldBossSelectionPanel;
    private CalculationPanel calculationPanel;
    
    private static final int CALCULATION_PANEL_HEIGHT = 100;
    
    public WorldBossOptimizerFrame(){
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(CreatureFactory.getPicture("Backgrounds/CQ Background"), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        
        topPanel = new JPanel();
        assetPanel = new AssetPanel(this,true);
        worldBossSelectionPanel = new WorldBossSelectionPanel(this);
        calculationPanel = new CalculationPanel(this);
        
        topPanel.add(assetPanel);
        topPanel.add(worldBossSelectionPanel);
        backgroundPanel.add(topPanel);
        backgroundPanel.add(calculationPanel);
        add(backgroundPanel);
        
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        
        topPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        topPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        backgroundPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        worldBossSelectionPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        assetPanel.setPreferredSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        assetPanel.setMaximumSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        calculationPanel.setPreferredSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        calculationPanel.setMinimumSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        
        getContentPane().setBackground(Color.YELLOW);
        //topPanel.setBackground(Color.YELLOW);
        //worldBossSelectionPanel.setBackground(Color.GREEN);
        //calculationPanel.setBackground(Color.BLUE);
        topPanel.setOpaque(false);
        worldBossSelectionPanel.setOpaque(false);
        
        setTitle("World Boss Optimizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
        
    }
    
    @Override
    public void recieveSolution(Formation f){
        worldBossSelectionPanel.recieveSolution(f);
    }

    @Override
    public long getFollowers() {
        return assetPanel.getFollowers();
    }

    @Override
    public int getMaxCreatures() {
        return assetPanel.getMaxCreatures();
    }

    @Override
    public Hero[] getHeroes() {
        return assetPanel.getHeroes();
    }
    
    public Hero[] getHeroesWithoutPrioritization() {
        return assetPanel.getHeroesWithoutPrioritization();
    }
    
    public Hero[] getPrioritizedHeroes() {
        return assetPanel.getPrioritizedHeroes();
    }

    public WorldBoss getBoss() {
        return (WorldBoss) worldBossSelectionPanel.getBoss().getCopy();
    }

    @Override
    public void recieveDone() {
        
    }

    @Override
    public AISolver makeSolver() {
        return new WorldBossOptimizer(this);
    }

    @Override
    public void setVisible(Boolean b) {
        super.setVisible(b);
    }
    
    @Override
    public String getDoneMessage() {
        return "Done";
    }
    
    @Override
    public String getSolutionMessage() {
        return "Solution updated";
    }
    
    @Override
    public void recieveDamageOfBattle(long damage) {
        worldBossSelectionPanel.setDamage(damage);
    }
    
    @Override
    public void backToMenuAction() {
        //nothing
    }
    
    @Override
    public void parametersChanged() {
        calculationPanel.recieveStopSearching();
        worldBossSelectionPanel.parametersChanged();
    }
    
    @Override
    public void recieveProgressString(String text) {
        calculationPanel.recieveProgressString(text);
    }
    
    @Override
    public void recieveCreatureList(LinkedList<Creature> list) {
        calculationPanel.recieveCreatureList(list);
    }
    
    @Override
    public void recieveStart() {
        
    }
    
}
