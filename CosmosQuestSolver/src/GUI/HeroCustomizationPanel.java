/*

 */
package GUI;

import AI.AISolver;
import Formations.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.LayerUI;

public class HeroCustomizationPanel extends JPanel implements ActionListener, DocumentListener{
    
    private ISolverFrame frame;
    private Hero hero;
    
    private CreaturePicturePanel picturePanel;
    private JPanel editPanel;
    private JCheckBox includeCheckBox;
    private JCheckBox prioritizeCheckBox;
    //private JLabel levelLabel;
    private JTextField levelTextField;
    
    public static final int CHANGE_PANEL_SIZE = 30;
    
    public HeroCustomizationPanel(ISolverFrame frame, Hero hero, boolean includePrioritize){
        this.frame = frame;
        this.hero = hero;
        
        picturePanel = new CreaturePicturePanel(hero);
        editPanel = new JPanel();
        includeCheckBox = new JCheckBox();
        prioritizeCheckBox = new JCheckBox();
        //levelLabel = new JLabel("Lvl");
        levelTextField = new JTextField("1");
        
        LayerUI<JComponent> layerUI = new DisableUI();
        JLayer<JComponent> jlayer = new JLayer<JComponent>(picturePanel, layerUI);
    
        add (jlayer);
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.X_AXIS));
        
        editPanel.add(includeCheckBox);
        if (includePrioritize){
            editPanel.add(prioritizeCheckBox);
        }
        editPanel.add(levelTextField);
        
        //add(picturePanel);
        add(editPanel);
        
        picturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        includeCheckBox.setToolTipText("Include this hero in calculations");
        prioritizeCheckBox.setToolTipText("Always have hero in formations");
        includeCheckBox.addActionListener(this);
        includeCheckBox.setActionCommand("include");
        prioritizeCheckBox.addActionListener(this);
        prioritizeCheckBox.setActionCommand("prioritize");
        levelTextField.getDocument().addDocumentListener(this);
        setOpaque(false);
        //setBackground(Color.BLUE);
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
    
    private void levelHero(){
        
        //level 1000 exception
        if (levelTextField.getText().equals("1k") || levelTextField.getText().equals("1K")){
            hero.levelUp(1000);

            frame.repaint();
            levelTextField.setForeground(Color.BLACK);
            if (heroEnabled()){
                ISolverFrame i = (ISolverFrame) frame;
                i.parametersChanged();
            }
            return;
        }
        
        try{
            int level = Integer.parseInt(levelTextField.getText());
            
            if (!Hero.validHeroLevel(level)){
                throw new Exception();
            }
            
            
            hero.levelUp(level);
            frame.repaint();
            
            
            
            levelTextField.setForeground(Color.BLACK);
            if (heroEnabled()){
                ISolverFrame i = (ISolverFrame) frame;
                i.parametersChanged();
            }
        }
        catch(Exception e){
            levelTextField.setForeground(Color.RED);
        }
    }
/*
    public void disableCreature() {
        includeCheckBox.setSelected(false);
        if (prioritizeCheckBox.isSelected()){
            prioritizeCheckBox.setSelected(false);
        }
        frame.repaint();// too many repaints?
    }
    */
    
    public void setHeroEnabled(boolean b) {
        includeCheckBox.setSelected(b);
        if (!b && prioritizeCheckBox.isSelected()){
            prioritizeCheckBox.setSelected(false);
        }
        repaint();
    }
    
    public void setPrioritizeHero(boolean b){
        prioritizeCheckBox.setSelected(b);
        if (b && !includeCheckBox.isSelected()){
            includeCheckBox.setSelected(true);
        }
        repaint();
    }

    public void setLevel(int level) {
        hero.levelUp(level);
        levelTextField.setText(Integer.toString(level));
        levelTextField.setForeground(Color.BLACK);
        
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        //ensure that if a hero is disabled, deprioritize it. If prioritized, enable it
        if (e.getActionCommand().equals("include") && !includeCheckBox.isSelected() && prioritizeCheckBox.isSelected()){
            prioritizeCheckBox.doClick();
        }
        else if (e.getActionCommand().equals("prioritize") && !includeCheckBox.isSelected() && prioritizeCheckBox.isSelected()){
            includeCheckBox.setSelected(true);
        }
        
        
        try{
            ISolverFrame i = (ISolverFrame) frame;
            i.parametersChanged();
        }
        catch(Exception ex){
            
        }
        frame.repaint();
        //repaint();
    }

    public void writeHeroLevelString(PrintWriter file) {
        file.print(hero.getName() + "," + Integer.toString(hero.getLevel()));
    }

    public void writeHeroSelectString(PrintWriter file) {
        file.print(hero.getName() + "," + Boolean.toString(includeCheckBox.isSelected()) + "," + Boolean.toString(prioritizeCheckBox.isSelected()));
    }

    public boolean heroEnabled() {
        return includeCheckBox.isSelected();
    }
    
    public boolean heroPrioritized() {
        return prioritizeCheckBox.isSelected();
    }

    public Hero getHero() {
        return hero;
    }

    
    
    private class DisableUI extends LayerUI<JComponent>{
        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            if (!includeCheckBox.isSelected()){
                g.setColor(new Color(128,128,128,128));
                g.fillRect(0, 0, AssetPanel.CREATURE_PICTURE_SIZE, AssetPanel.CREATURE_PICTURE_SIZE);
            }
        }
    }
    
}
