/*

 */
package GUI;

import Formations.Creature;
import Formations.Monster;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//displays creature and lets you change it
public class EnemyFormationSinglePanel extends JPanel implements MouseListener{
    
    private EnemySelectFrame frame;
    private EnemyFormationMakerPanel enemyFormationMakerPanel;
    private EnemyFormationPanel enemyFormationPanel;
    private boolean facingRight;
    
    private CreaturePicturePanel picPanel;

    public EnemyFormationSinglePanel(EnemySelectFrame frame, EnemyFormationMakerPanel enemyFormationMakerPanel, EnemyFormationPanel parent, Creature creature, boolean facingRight) {
        this.frame = frame;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        this.enemyFormationPanel = parent;
        this.facingRight = facingRight;
        
        picPanel = new CreaturePicturePanel(creature);
        add(picPanel);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        picPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        //setOpaque(false);
        addMouseListener(this);
        picPanel.removeMouseListener(picPanel);//replaces mouse actions with this classe's actions. problems with multiple mouseListeners
        //setBackground(Color.BLUE);
        
    }
    
    public Creature getCreature(){
        return picPanel.getCreature();
    }
    
    public void setCreature(Creature c){
        if (c == null || c.isFacingRight() == facingRight){
            picPanel.setCreature(c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2){//middle mouse to "copy"
            //System.out.println("hi");
            frame.setMouseCreature(getCreature());
        }
        else{//pick up creature
            if (frame.getMouseCreature() == null){
                frame.setMouseCreature(getCreature());
                setCreature(null);
            }
            else{//drop off creature
                Creature c = frame.getMouseCreature();
                if (!(c instanceof Monster) && enemyFormationPanel.containsCreature(c.getName())){
                    enemyFormationPanel.removeCreature(c.getName());
                }
                setCreature(c);
                frame.setMouseCreature(null);
            }
        }
        frame.parametersChanged();
        repaint();
    }
    
    

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Creature creature = picPanel.getCreature();
        if (creature != null){
            setToolTipText(creature.toolTipText());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setToolTipText("");
    }
    
}
