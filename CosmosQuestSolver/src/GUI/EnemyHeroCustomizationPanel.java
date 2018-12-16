/*

 */
package GUI;

import Formations.Hero;
import AI.QuestSolver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.LayerUI;

//lets you select the enemy hero and set their level
public class EnemyHeroCustomizationPanel extends JPanel implements DocumentListener{

    private EnemySelectFrame frame;
    private Hero hero;
    EnemyFormationMakerPanel enemyFormationMakerPanel;
    
    private CreaturePicturePanel picturePanel;
    private JPanel editPanel;
    
    //private JLabel levelLabel;
    private JTextField levelTextField;
    
    public static final int CHANGE_PANEL_SIZE = 30;
    
    public EnemyHeroCustomizationPanel(EnemySelectFrame frame, EnemyFormationMakerPanel enemyFormationMakerPanel, Hero hero){//load heroes?
        this.frame = frame;
        this.hero = hero;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        
        
        
        picturePanel = new CreaturePictureSelectionPanel(frame,enemyFormationMakerPanel,hero);
        editPanel = new JPanel();
        
        //levelLabel = new JLabel("Lvl");
        levelTextField = new JTextField("1");
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.X_AXIS));
        
        
        //editPanel.add(levelLabel);
        editPanel.add(levelTextField);
        
        add(picturePanel);
        add(editPanel);
        
        picturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        //addMouseMotionListener(enemyFormationMakerPanel);
        //this.setCursor(new Cursor());
        //setBackground(Color.BLUE);
        levelTextField.getDocument().addDocumentListener(this);
        setOpaque(false);
        
    }
    
    public JTextField getLevelTextField(){
        return levelTextField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        levelHero();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        levelHero();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        levelHero();
    }
    
    public void levelHero(){//repeat code***
        
        //level 1000 exception
        if (levelTextField.getText().equals("1k") || levelTextField.getText().equals("1K")){
            hero.levelUp(1000);

            enemyFormationMakerPanel.repaint();
            levelTextField.setForeground(Color.BLACK);
            if (enemyFormationMakerPanel.getEnemyFormation().contains(hero)){
                frame.parametersChanged();
            }
            return;
        }
        
        try{
            int level = Integer.parseInt(levelTextField.getText());
            
            if (!Hero.validHeroLevel(level)){
                throw new Exception();
            }
            
            hero.levelUp(level);
            
            enemyFormationMakerPanel.repaint();
            levelTextField.setForeground(Color.BLACK);
        }
        catch(Exception e){
            levelTextField.setForeground(Color.RED);
        }
        
        if (enemyFormationMakerPanel.getEnemyFormation().contains(hero)){
            frame.parametersChanged();
        }
        
    }

    public void setLevel(int level) {
        levelTextField.setText(Integer.toString(level));
        levelHero();
    }

    public Hero getHero() {
        return hero;
    }
    
    
    
}
