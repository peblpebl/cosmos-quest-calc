/*

 */
package GUI;

import AI.AISolver;
import Formations.Formation;
import AI.QuestSolver;
import Formations.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// GUI for the panel that contains all of the user's assets, incuding heroes, number of 
// followers, and max creatures per row
public class AssetPanel extends JPanel implements ActionListener, DocumentListener{
    
    private JFrame frame;
    
    
    //private JLabel solutionLabel;
    //private SolutionFormationPanel solutionFormationPanel;
    private JLabel followersLabel;
    private JTextField followersTextField;
    private JLabel maxCreaturesLabel;
    private JTextField maxCreaturesTextField;
    private HeroesCustomizationPanel heroesCustomizationPanel;
    private JScrollPane heroesCustomizationScrollPane;
    private JPanel assetTitlePanel;// for making the two hero panels start at the same y
    private JLabel assetLabel;
    private JPanel optionsPanel;
    private JButton disableAllButton;
    private JButton deprioritizeAllButton;
    private JButton setLevelButton;
    private JButton saveButton;
    private JButton loadButton;
    
    private long followers = 0;
    private int maxCreatures = Formation.MAX_MEMBERS;
    
    private JPanel followersPanel;
    private JPanel maxCreaturesPanel;
    
    public static final int CREATURE_PICTURE_SIZE = 100;
    public static final int TEXTBOX_HEIGHT = 25;
    public static final int HERO_SELECTION_COLUMNS = 7;
    public static final int HERO_SELECTION_PANEL_WIDTH = CREATURE_PICTURE_SIZE * HERO_SELECTION_COLUMNS + (Integer)UIManager.get("ScrollBar.width");
    public static final int HERO_SELECTION_PANEL_HEIGHT = (HeroCustomizationPanel.CHANGE_PANEL_SIZE + CREATURE_PICTURE_SIZE) * 6;
    public static final int ASSET_PANEL_HEIGHT = 33;
    public static final int SCROLL_BAR_SPEED = 16;
    public static final Font TITLE_FONT = new Font("Courier", Font.PLAIN, 22);
    
    
    
    public AssetPanel(JFrame frame,boolean includePrioritize){
        this.frame = frame;
        
        //solutionLabel = new JLabel("Solution");
        //solutionFormationPanel = new SolutionFormationPanel(solver);
        followersLabel = new JLabel("Followers");
        followersTextField = new JTextField("0");
        maxCreaturesLabel = new JLabel("Creatures in row");
        maxCreaturesTextField = new JTextField(Integer.toString(Formation.MAX_MEMBERS));
        heroesCustomizationPanel = new HeroesCustomizationPanel(frame,HERO_SELECTION_COLUMNS,true,includePrioritize);
        heroesCustomizationScrollPane = new JScrollPane(heroesCustomizationPanel);
        assetTitlePanel = new JPanel();
        assetLabel = new JLabel("Your assets");
        optionsPanel = new JPanel();
        disableAllButton = new JButton("Disable All");
        deprioritizeAllButton = new JButton("De-prioritize All");
        setLevelButton = new JButton("Set Level All");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        
        disableAllButton.addActionListener(this);
        deprioritizeAllButton.addActionListener(this);
        setLevelButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        disableAllButton.setActionCommand("disable all");
        deprioritizeAllButton.setActionCommand("deprioritize all");
        setLevelButton.setActionCommand("set level all");
        saveButton.setActionCommand("save");
        loadButton.setActionCommand("load");
        
        
        followersPanel = new JPanel();
        maxCreaturesPanel = new JPanel();
        
        followersPanel.setLayout(new BoxLayout(followersPanel,BoxLayout.X_AXIS));
        maxCreaturesPanel.setLayout(new BoxLayout(maxCreaturesPanel,BoxLayout.X_AXIS));
        
        followersPanel.add(followersLabel);
        followersPanel.add(followersTextField);
        maxCreaturesPanel.add(maxCreaturesLabel);
        maxCreaturesPanel.add(maxCreaturesTextField);
        assetTitlePanel.add(assetLabel);
        optionsPanel.add(disableAllButton);
        if (includePrioritize){
            optionsPanel.add(deprioritizeAllButton);
        }
        optionsPanel.add(setLevelButton);
        optionsPanel.add(saveButton);
        optionsPanel.add(loadButton);
        
        //add(solutionLabel);
        //add(solutionFormationPanel);
        add(assetTitlePanel);
        add(followersPanel);
        add(maxCreaturesPanel);
        add(optionsPanel);
        add(heroesCustomizationScrollPane);
        
        //solutionFormationPanel.setPreferredSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        //solutionFormationPanel.setMaximumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        //solutionFormationPanel.setMinimumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,CREATURE_PICTURE_SIZE));
        followersTextField.setMaximumSize(new Dimension(90,TEXTBOX_HEIGHT));
        maxCreaturesTextField.setMaximumSize(new Dimension(15,TEXTBOX_HEIGHT));
        //maxCreaturesTextField.setColumns(1);
        heroesCustomizationScrollPane.setPreferredSize(new Dimension(HERO_SELECTION_PANEL_WIDTH + 200,HERO_SELECTION_PANEL_HEIGHT));
        heroesCustomizationScrollPane.setMaximumSize(new Dimension(HERO_SELECTION_PANEL_WIDTH + 200,HERO_SELECTION_PANEL_HEIGHT));
        heroesCustomizationScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_SPEED);
        
        assetTitlePanel.setPreferredSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        assetTitlePanel.setMaximumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        assetTitlePanel.setMinimumSize(new Dimension(CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,ASSET_PANEL_HEIGHT));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        //solutionFormationPanel.setBackground(Color.CYAN);
        //heroesCustomizationPanel.setBackground(Color.MAGENTA);
        heroesCustomizationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        //followersPanel.setBackground(Color.red);
        //maxCreaturesPanel.setBackground(Color.ORANGE);
        setOpaque(false);
        assetTitlePanel.setOpaque(false);
        optionsPanel.setOpaque(false);
        followersPanel.setOpaque(false);
        maxCreaturesPanel.setOpaque(false);
        
        //solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        assetLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        followersLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        maxCreaturesLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        //assetLabel.setHorizontalAlignment(JLabel.CENTER);
        //followersLabel.setHorizontalAlignment(JLabel.CENTER);
        //maxCreaturesLabel.setHorizontalAlignment(JLabel.CENTER);
        
        followersTextField.getDocument().addDocumentListener(this);
        maxCreaturesTextField.getDocument().addDocumentListener(this);
        
        load();
        
    }
    
    public long getFollowers() {
        return followers;
    }

    public int getMaxCreatures() {
        return maxCreatures;
    }
    
    public Hero getHero(String name) {
        return heroesCustomizationPanel.getHero(name);
    }

    public Hero[] getHeroes() {
        return heroesCustomizationPanel.getHeroes();
    }
    
    public Hero[] getHeroesWithoutPrioritization() {
        return heroesCustomizationPanel.getHeroesWithoutPrioritization();
    }
    
    public Hero[] getPrioritizedHeroes() {
        return heroesCustomizationPanel.getPrioritizedHeroes();
    }

    public boolean heroEnabled(String heroName) {
        return heroesCustomizationPanel.heroEnabled(heroName);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }
    
    public void textFieldChanged(DocumentEvent e){
        if (e.getDocument() == followersTextField.getDocument()){
            followersTextFieldChanged();
        }
        else if (e.getDocument() == maxCreaturesTextField.getDocument()){
            maxCreaturesTextFieldChanged();
        }
        else{
            System.out.println("Unknown Document in assetPanel!");
        }
        
    }
    
    private void followersTextFieldChanged(){
        try{
            long tempFollowers = Long.parseLong(followersTextField.getText());
            if (tempFollowers >= 0){
                followersTextField.setForeground(Color.BLACK);
                this.followers = tempFollowers;
                
            }
            else{
                throw new Exception();
            }
        }
        catch (Exception ex){
            followersTextField.setForeground(Color.RED);
        }
        try{
            ISolverFrame f = (ISolverFrame) frame;//have ISolverFrame be an abstract class instead?
            f.parametersChanged();
        }
        catch(Exception ex){
            
        }
    }
    
    private void maxCreaturesTextFieldChanged(){
        try{
            int tempMaxCreatures = Integer.parseInt(maxCreaturesTextField.getText());
            if (tempMaxCreatures > 0 && tempMaxCreatures <= Formation.MAX_MEMBERS){
                maxCreaturesTextField.setForeground(Color.BLACK);
                this.maxCreatures = tempMaxCreatures;
                
            }
            else{
                throw new Exception();
            }
        }
        catch (Exception ex){
            maxCreaturesTextField.setForeground(Color.RED);
        }
        try{
            ISolverFrame f = (ISolverFrame) frame;//have ISolverFrame be an abstract class instead?
            f.parametersChanged();
        }
        catch(Exception ex){
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "disable all":
                heroesCustomizationPanel.disableAll();
            break;
            case "deprioritize all":
                heroesCustomizationPanel.deprioritizeAll();
            break;
            case "set level all":
                int level = 1;
                try{
                    String input = JOptionPane.showInputDialog(frame, "Enter level", 1);
                    if (input.equals("1k") || input.equals("1K")){
                        level = 1000;
                    }
                    else{
                        level = Integer.parseInt(input);
                    }
                    if (Hero.validHeroLevel(level)){
                        heroesCustomizationPanel.setLevelAll(level);
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Invalid level", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Level needs to be an integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            break;
            case "save":
                save();
            break;
            case "load":
                load();
            break;
            default: System.out.println("AssetPanel actionCommand is different");
        }
        ISolverFrame f = (ISolverFrame) frame;
        f.parametersChanged();
        frame.requestFocusInWindow();
    }

    private void save() {
        if (JOptionPane.showConfirmDialog(frame,"Save current heroes?","",JOptionPane.YES_NO_OPTION) != 0){
            return;
        }
        
        try{
            PrintWriter file = new PrintWriter("save data.txt");
            file.println(followers);
            file.println(maxCreatures);
            heroesCustomizationPanel.writeSaveString(file);
            file.close();
        }
        catch(Exception e){
            System.out.println("failed to save");
        }
        
    }
    
    private void load(){
        try{
            Scanner sc = new Scanner(new File("save data.txt"));
            followers = Long.parseLong(sc.nextLine());
            followersTextField.setText(Long.toString(followers));
            maxCreatures = Integer.parseInt(sc.nextLine());
            maxCreaturesTextField.setText(Integer.toString(maxCreatures));
            
            String[] tokens;
            while (sc.hasNext()){
                tokens = sc.nextLine().split(",");
                heroesCustomizationPanel.setHeroStats(tokens[0],Integer.parseInt(tokens[1]),Boolean.parseBoolean(tokens[2]),Boolean.parseBoolean(tokens[3]));
            }
        }
        catch(Exception e){
            System.out.println("failed to load");
        }
    }

    boolean heroPrioritized(String hName) {
        return heroesCustomizationPanel.heroPrioritized(hName);
    }

    

    
}
