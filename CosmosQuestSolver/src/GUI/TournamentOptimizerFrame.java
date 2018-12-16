/*

 */
package GUI;

import AI.AISolver;
import AI.TournamentOptimizer;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.TournamentGrid;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// gui for tournament optimization
public class TournamentOptimizerFrame extends JFrame implements ISolverFrame, DocumentListener, ActionListener{
    private JPanel backgroundPanel;
    private JPanel topPanel;
    private AssetPanel assetPanel;
    private JPanel tournamentPanel;
    private JPanel tournamentTopPanel;
    private TournamentGridPanel tournamentGridPanel;
    private JButton refineBestGridButton;
    private CalculationPanel calculationPanel;
    
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel tournamentSettingsPanel;
    private JCheckBox winnerTakeAllCheckBox;
    private JPanel rowPanel;
    private JPanel solutionPanel;
    private JLabel solutionLabel;
    
    private JLabel rowLabel;
    private JTextField rowTextField;
    
    public int rows;
    
    private static final int CALCULATION_PANEL_HEIGHT = 100;
    public static final int MAX_ROWS = 6;
    public static final int DEFAULT_ROWS = 5;
    
    
    public TournamentOptimizerFrame(){
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(CreatureFactory.getPicture("Backgrounds/CQ Background"), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        
        rows = DEFAULT_ROWS;
        
        topPanel = new JPanel();
        assetPanel = new AssetPanel(this,false);
        tournamentPanel = new JPanel();
        tournamentTopPanel = new JPanel();
        tournamentGridPanel = new TournamentGridPanel(this,DEFAULT_ROWS);
        refineBestGridButton = new JButton("Refine Top Grid");
        refineBestGridButton.addActionListener(this);
        refineBestGridButton.setActionCommand("refine");
        refineBestGridButton.setEnabled(false);
        calculationPanel = new CalculationPanel(this);
        tournamentSettingsPanel = new JPanel();
        winnerTakeAllCheckBox = new JCheckBox("Winner take all", true);
        winnerTakeAllCheckBox.addActionListener(this);
        winnerTakeAllCheckBox.setActionCommand("winnerTakeAll");
        titlePanel = new JPanel();
        titleLabel = new JLabel("Tournament");
        rowPanel = new JPanel();
        //maxCreaturesPanel = new JPanel();
        solutionPanel = new JPanel();
        solutionLabel = new JLabel("Best Grid Generated");
        rowLabel = new JLabel("Number of Rows: ");
        rowTextField = new JTextField(Integer.toString(DEFAULT_ROWS));
        //maxCreaturesLabel = new JLabel("Max Creatures");
        //maxCreaturesTextField = new JTextField(Integer.toString(Formation.MAX_MEMBERS));
        
        rowPanel.add(rowLabel);
        rowPanel.add(rowTextField);
        //maxCreaturesPanel.add(maxCreaturesLabel);
        //maxCreaturesPanel.add(maxCreaturesTextField);
        titlePanel.add(titleLabel);
        solutionPanel.add(solutionLabel);
        
        tournamentSettingsPanel.add(rowPanel);
        tournamentSettingsPanel.add(winnerTakeAllCheckBox);
        
        //tournamentSettingsPanel.add(maxCreaturesPanel);
        
        tournamentTopPanel.add(titlePanel);
        tournamentTopPanel.add(tournamentSettingsPanel);
        //tournamentTopPanel.add(solutionPanel);
        
        
        tournamentPanel.add(tournamentTopPanel);
        tournamentPanel.add(solutionPanel);
        tournamentPanel.add(tournamentGridPanel);
        //tournamentPanel.add(refineBestGridButton); have this trigger automatically
        
        topPanel.add(assetPanel);
        topPanel.add(tournamentPanel);
        
        backgroundPanel.add(topPanel);
        backgroundPanel.add(calculationPanel);
        add(backgroundPanel);
        
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        tournamentPanel.setLayout(new BoxLayout(tournamentPanel,BoxLayout.Y_AXIS));
        //tournamentPanel.setAlignmentY(TOP_ALIGNMENT);
        tournamentSettingsPanel.setLayout(new BoxLayout(tournamentSettingsPanel,BoxLayout.Y_AXIS));
        rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.X_AXIS));
        //maxCreaturesPanel.setLayout(new BoxLayout(maxCreaturesPanel,BoxLayout.X_AXIS));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        
        
        topPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        topPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        backgroundPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT + CALCULATION_PANEL_HEIGHT));
        tournamentPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        tournamentPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        tournamentTopPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,100));
        solutionPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,30));
        assetPanel.setPreferredSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        assetPanel.setMaximumSize(new Dimension(QuestSolverFrame.ASSET_PANEL_WIDTH,QuestSolverFrame.ASSET_PANEL_HEIGHT));
        rowTextField.setMaximumSize(new Dimension(20,AssetPanel.TEXTBOX_HEIGHT));
        //maxCreaturesTextField.setMaximumSize(new Dimension(20,AssetPanel.TEXTBOX_HEIGHT));
        tournamentSettingsPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2,150));
        calculationPanel.setPreferredSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        calculationPanel.setMinimumSize(new Dimension(QuestSolverFrame.CALCULATION_PANEL_WIDTH,QuestSolverFrame.CALCULATION_PANEL_HEIGHT));
        
        tournamentGridPanel.setOpaque(false);
        tournamentTopPanel.setOpaque(false);
        
        rowTextField.getDocument().addDocumentListener(this);
        //maxCreaturesTextField.getDocument().addDocumentListener(this);
        
        //maxCreaturesLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        rowLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        titleLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        
        //setBackground(Color.RED);
        //topPanel.setBackground(Color.YELLOW);
        //tournamentPanel.setBackground(Color.GREEN);
        getContentPane().setBackground(Color.YELLOW);
        tournamentPanel.setOpaque(false);
        titlePanel.setOpaque(false);
        tournamentSettingsPanel.setOpaque(false);
        solutionPanel.setOpaque(false);
        topPanel.setOpaque(false);
        rowPanel.setOpaque(false);
        winnerTakeAllCheckBox.setOpaque(false);
        winnerTakeAllCheckBox.setToolTipText("<html>"
                              + "For tournaments, each row fights against the enemy grid's"
                              +"<br>corresponding row, and the one who gets the most wins wins"
                              + "<br>the entire encounter. Turn off for hourly fights"
                              + "</html>");
        
        setTitle("Tournament Optimizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
        
    }
    
    public boolean isWinnerTakeAll(){
        return winnerTakeAllCheckBox.isSelected();
    }
    
    public int getRows(){
        return rows;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateGridTextField();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateGridTextField();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateGridTextField();
    }
    
    private void updateGridTextField(){
        try{
            int rows = Integer.parseInt(rowTextField.getText());
            if (rows > 0 && rows <= MAX_ROWS){
                rowTextField.setForeground(Color.BLACK);
                this.rows = rows;
                tournamentGridPanel.setRows(rows);
                parametersChanged();
                revalidate();
                repaint();
            }
            else{
                throw new Exception();
            }
        }
        catch (Exception e){
            rowTextField.setForeground(Color.RED);
        }
        /*
        try{
            int maxCreatures = Integer.parseInt(maxCreaturesTextField.getText());
            if (maxCreatures > 0 && maxCreatures <= Formation.MAX_MEMBERS){
                maxCreaturesTextField.setForeground(Color.BLACK);
                
            }
            else{
                throw new Exception();
            }
            
        }
        catch (Exception e){
            maxCreaturesTextField.setForeground(Color.RED);
        }
*/
    }
    
    public void recieveSolution(LinkedList<Formation> list){
        //nothing
    }
    
    public void recieveSolution(TournamentGrid g){
        tournamentGridPanel.recieveGrid(g);
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

    public int getNumRows() {
        return rows;
    }
    
    @Override
    public AISolver makeSolver() {
        return new TournamentOptimizer(this);
    }

    @Override
    public void recieveSolution(Formation f) {
        tournamentGridPanel.recieveGrid(new TournamentGrid());
    }
    

    @Override
    public void recieveDone() {
        calculationPanel.recieveStopSearching();
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
    public String getSolutionMessage() {//not used*
        return "Solution updated";
    }
    
    @Override
    public void recieveDamageOfBattle(long damage) {
        //nothing
    }
    
    @Override
    public void backToMenuAction() {
        //nothing
    }
    
    @Override
    public void parametersChanged() {
        tournamentGridPanel.resetGrid();
        calculationPanel.recieveStopSearching();
        calculationPanel.parametersChanged();
    }
    
    @Override
    public void recieveProgressString(String text) {
        calculationPanel.recieveProgressString(text);
    }
    
    @Override
    public void recieveMessageString(String text) {
        if (calculationPanel != null){
            calculationPanel.setMessageString(text);
        }
    }
    
    @Override
    public void recieveCreatureList(LinkedList<Creature> list) {
        calculationPanel.recieveCreatureList(list);
    }
    
    @Override
    public void recieveStart() {
        refineBestGridButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {//checkbox for winnerTakeAll
        if (e.getActionCommand().equals("winnerTakeAll")){
            calculationPanel.recieveStopSearching();
            parametersChanged();
        }
        else if (e.getActionCommand().equals("refine")){
            calculationPanel.recieveRefine();
            refineBestGridButton.setEnabled(false);
        }
    }

    @Override
    public boolean showViewButton() {
        return false;
    }

    @Override
    public String getSelectSource() {
        return "save data/hero tournament select data.txt";
    }
    
}
