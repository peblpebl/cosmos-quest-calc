/*

 */
package AI;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import Formations.TournamentGrid;
import GUI.TournamentOptimizerFrame;
import cosmosquestsolver.OtherThings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

//tries to generate a grid that will beat everyone if they use the same strategy as me.
//runs on a seperate thread
public class TournamentOptimizer extends AISolver{//if (leprechaun) delete monster in line?
    
    private TournamentOptimizerFrame frame;
    private int numRows;
    private LinkedList<Hero> sortedHeroes;
    private TournamentRankings rankings;
    private boolean winnerTakeAll;
    private boolean buildingStage;
    private boolean containsLep;//for heroes that benifit from a non-full grid
    
    public TournamentOptimizer(TournamentOptimizerFrame frame){
        this.frame = frame;
        
    }
    
    //for every iteration, generate a new procedually generated grid and add it to a list (rankings object)
    //whenever a grid with a higher win rate against all other grids is found, notify frame.
    //repeats until user stops the calculation
    @Override
    protected void search() {
        obtainProblem();
        sortHeroes();
        sendListInfoToFrame();
        
        while(searching){
            searchTick();
        }
        
        frame.recieveDone();
        
    }
    
    //returns the number of grids to generate until switching to the refining stage
    private int refineStartPoint(){
        return (int)(rankings.getMaxSize() * 2.2);
    }
    
    private void searchTick(){
        if (buildingStage){
            buildCompetition();
            //start the reminement stage after a certain amount of grid generations
            if (rankings.getGridsGenerated() >= refineStartPoint()){
                recieveRefine();
            }
        }
        else{
            refineTopGrid();
        }
    }
    
    private void buildCompetition(){
        //create a random grid or alter one of the top grids
        if (rankings.size() >= 1 && Math.random() < 0.12){//experiment with percentage?
            if (containsLep && Math.random() < 0.17){
                TournamentGrid lepGrid = TournamentGridGenerator.deleteLepMonsters(rankings.getRandomTopGrid(0.10), followers, maxCreatures);
                rankings.addGrid(TournamentGridGenerator.newAlteredGrid(lepGrid,followers,maxCreatures,(int)(Math.random()*1.5 + 1)));
            }
            else{
                rankings.addGrid(TournamentGridGenerator.newAlteredGrid(rankings.getRandomTopGrid(0.10),followers,maxCreatures,(int)(Math.random()*1.5 + 1)));//returns a random grid from the top 10%
            }
        }
        else{
            rankings.addGrid(TournamentGridGenerator.createRandomGrid(sortedHeroes,followers,numRows,maxCreatures));
        }
            
        
        frame.recieveSolution(rankings.getTopGrid());
        //System.out.println("Grids in pool: " + rankings.size());
        //frame.recieveProgressString("Grids in pool: " + rankings.size() + "  total grids generated: " + rankings.getGridsGenerated());
        if (rankings.getGridsGenerated() < rankings.getMaxSize()){
            frame.recieveProgressString("Populating grid pool: " + rankings.getGridsGenerated() + " / " + rankings.getMaxSize());
        }
        else{
            frame.recieveProgressString("Refining grid pool: " + (rankings.getGridsGenerated() - rankings.getMaxSize()) + " / " + (refineStartPoint() - rankings.getMaxSize()));
        }
    }
    
    
    
    //gets problem parameters from frame and sets up the rankings
    private void obtainProblem(){
        topGridForRefinement = null;
        topGridWinsEver = 0;
        followers = frame.getFollowers();
        maxCreatures = frame.getMaxCreatures();
        heroes = frame.getHeroes();
        numRows = frame.getRows();
        winnerTakeAll = frame.isWinnerTakeAll();
        buildingStage = true;
        
        for (Hero h : heroes){
            if (h.getName().equals("Leprechaun")){
                containsLep = true;//what if lep is too low to put in?
            }
        }
        
        rankings = new TournamentRankings(determineRankingMaxSize(),frame.isWinnerTakeAll());
    }
    
    private void sendListInfoToFrame(){
        LinkedList<Creature> creatureList = new LinkedList<>();
        for (Hero h : sortedHeroes){
            creatureList.add(h);
        }
        frame.recieveCreatureList(creatureList);
    }
    
    private int determineRankingMaxSize(){
        if (winnerTakeAll){
            switch(numRows){
                case 6: return 900;
                case 5: return 1300;
                case 4: return 1600;
                case 3: return 1900;
                case 2: return 2200;
                case 1: return 3000;
                default: return 1200;
            }
        }
        else{
            switch(numRows){
                case 6: return 300;
                case 5: return 400;
                case 4: return 500;
                case 3: return 750;
                case 2: return 900;
                case 1: return 1800;
                default: return 500;
            }
        }
    }
    
    //sort heroes by viability
    private void sortHeroes(){
        sortedHeroes = new LinkedList<>();
        for (Hero hero : heroes){
            sortedHeroes.add(hero);
        }
        Collections.sort(sortedHeroes, (Hero h1, Hero h2) -> h2.getSpecialAbility().viability()-h1.getSpecialAbility().viability());
    }
    
    //tests the rankings class
    /*
    private void test(){
        double totalWins = rankings.rankings.getFirst().getTotalWins()/(2.0*numRows*numRows);
        int size = rankings.size();
        System.out.println("Current grid win rate: " + totalWins + "/" + size + " = " + totalWins/size);
    }
    */
    
    
    
    
    private int giveUpPointStart = 0;
    private int giveUpPoint = giveUpPointStart;
    private int giveUpPointIncreace = 0;
    private int refinementsMade = 0;//total refinements, what gets displayed on screen
    private TournamentGrid topGridForRefinement;
    private int topGridWinsEver = 0;
    private int failedRefinements = 0;
    private int localGridWins = 0;
    private int topGridIndex = 0;
    
    @Override
    public void recieveRefine() {
        buildingStage = false;
        giveUpPointStart = winnerTakeAll ? 3200 : 500;//take number of rows into account?
        giveUpPoint = giveUpPointStart;
        giveUpPointIncreace = (int)(giveUpPoint/150);
        //topGridForRefinement = rankings.getTopGrid().getCopy();
        //topGridWins = rankings.totalWinPoints(topGridForRefinement);//this caused ConcurrentModificationException
    }
    
    private void refineTopGrid(){
        
        //for first time method is called
        if (topGridForRefinement == null){
            topGridForRefinement = rankings.getTopGrid().getCopy();
            topGridWinsEver = rankings.totalWinPoints(topGridForRefinement);
            localGridWins = topGridWinsEver;
        }
        
        TournamentGrid mutatedGrid = mutateGridForRefinement(topGridForRefinement,containsLep);
        if (containsLep && Math.random() < 0.15){
            mutatedGrid = TournamentGridGenerator.deleteLepMonsters(mutatedGrid,followers,maxCreatures);
        }
        
        
        //mutate the top grid once or mutate the original top grids mulltiple times (suggestion: to try to avoid local maximums. doing so might slow the process down, though)
        
        int mutatedWinPoints = rankings.totalWinPoints(mutatedGrid);//determine score of the new grid
        
        if (mutatedWinPoints > topGridWinsEver){//new record, send to frame
            topGridForRefinement = mutatedGrid;
            topGridWinsEver = mutatedWinPoints;
            failedRefinements = (int)(failedRefinements * 0.66);//try a little longer to see if anything more can come of the new grid.
            localGridWins = mutatedWinPoints;
            giveUpPoint += giveUpPointIncreace;
            
            frame.recieveSolution(mutatedGrid);
        }
        else if (mutatedWinPoints > localGridWins){//record for this steepest ascent search
            topGridForRefinement = mutatedGrid;
            localGridWins = mutatedWinPoints;
            failedRefinements = (int)(failedRefinements * 0.7);
            giveUpPoint += giveUpPointIncreace;
        }
        else{//no record of any kind
            failedRefinements ++;
            if (failedRefinements >= giveUpPoint){//switch to a different base grid to try to escape a local maximum in the search space
                if (topGridIndex + 1 < rankings.size()){
                    topGridIndex ++;
                }
                else{
                    topGridIndex = 0;//restart at top
                }
                
                topGridForRefinement = rankings.getGrid(topGridIndex);
                failedRefinements = 0;
                giveUpPoint = giveUpPointStart;
                localGridWins = 0;//replenished next function call
            }
        }
        refinementsMade ++;
        frame.recieveProgressString("Top grid refinements attempted: " + 
                refinementsMade + 
                "   Win rate: " + 
                OtherThings.twoDecimalFormat(100.0*topGridWinsEver/rankings.maxWinsTotal()) + 
                "%" + "  failed attempts percieved: " + 
                failedRefinements + "/" + giveUpPoint + "  root grid: " + (topGridIndex+1) + "  win rate: " + 
                OtherThings.twoDecimalFormat(100.0*localGridWins/rankings.maxWinsTotal()) + "%");
        
    }
    
    private TournamentGrid mutateGridForRefinement(TournamentGrid grid, boolean containsLep){
        if (Math.random() < 0.95){
            return TournamentGridGenerator.newAlteredGrid(grid,followers,maxCreatures,(int)(Math.random()*2.1 + 1));//sometimes 3
        }
        else{//get a grid from the top 5% and alter it several times
            return TournamentGridGenerator.newAlteredGrid(rankings.getRandomTopGrid(0.05).getCopy(),followers,maxCreatures,(int)(Math.random() * 10 + 5));
        }
    }
    
    //stores and maintains which grid beat who.
    private class TournamentRankings{
        private LinkedList<GridPlusWins> rankings;//maintained so that the best grid is first
        private int maxSize;
        private int gridsGenerated = 0;
        //private int maxWinsPerFight;
        private boolean winnerTakeAll;
        
        //makes initial grids
        public TournamentRankings(int maxSize, boolean winnerTakeAll){
            rankings = new LinkedList<>();
            this.maxSize = maxSize;
            this.winnerTakeAll = winnerTakeAll;
        }
        
        public TournamentGrid getTopGrid(){//timer from main class calls this? no. ConcurrentModification errors
            return rankings.getFirst().getGrid();
        }
        
        public TournamentGrid getGrid(int i){
            return rankings.get(i).getGrid();
        }
        
        public TournamentGrid getRandomTopGrid(double topPercent){
            if (topPercent > 1 || topPercent < 0){
                return getRandomTopGrid(1);
            }
            else if (topPercent < 0){
                return rankings.get(0).getGrid();
            }
            else{
                return rankings.get((int)(rankings.size()*Math.random()*topPercent)).getGrid();
            }
        }
        
        public int getGridsGenerated(){
            return gridsGenerated;
        }
        
        public void addGrid(TournamentGrid grid){
            GridPlusWins gpw = new GridPlusWins(grid);
            
            for (GridPlusWins otherGridWins : rankings){
                //int thisGridWin = grid.numWins(otherGridWins.getGrid());
                int thisGridWin = winPoints(grid,otherGridWins.getGrid());
                gpw.addGrid(otherGridWins.getGrid(), thisGridWin);
                otherGridWins.addGrid(grid, maxWinsPerFight() - thisGridWin);
            }
            rankings.add(gpw);
            
            Collections.sort(rankings, (GridPlusWins w1, GridPlusWins w2) -> w2.getTotalWins()-w1.getTotalWins());
            
            if (rankings.size() > maxSize){
                removeGrid();
            }
            gridsGenerated ++;
        }
        
        private void removeGrid(){//removes the grid with the lowest win rate
            GridPlusWins loser = rankings.pollLast();
            for (GridPlusWins otherGridWins : rankings){
                
                otherGridWins.removeGrid(loser.getGrid());
                
            }
        }
        
        private int maxWinsPerFight(){
            if (winnerTakeAll){
                return 1;
            }
            else{
                return 2 * numRows * numRows;
            }
        }
        
        //returns the number of wins a perfect grid will recieve
        private int maxWinsTotal(){
            return maxWinsPerFight() * size();
        }
        
        private int winPoints(TournamentGrid grid, TournamentGrid otherGrid){
            if (winnerTakeAll){
                return grid.bestOfWin(otherGrid);
            }
            else{
                return grid.numWins(otherGrid);
            }
        }
        
        //sees how a given grid outside the rankings performs against the grids in the rankings
        private int totalWinPoints(TournamentGrid grid){
            int wins = 0;
            for (GridPlusWins gpw : rankings){
                TournamentGrid gridInRankings = gpw.getGrid();
                wins += winPoints(grid,gridInRankings);
            }
            return wins;
        }
        
        public int size(){
            return rankings.size();
        }
        
        public int getMaxSize(){
            return maxSize;
        }
        
        public void print(){
            int gridFights = rankings.size()-1;
            if (gridFights == 0){
                return;
            }
            System.out.println("__________________________________");
            for (GridPlusWins gpw : rankings){
                System.out.println("total wins: " + gpw.totalWins + " grid fights: " + gridFights + " win rate: " + gpw.totalWins/(1.0*gridFights*maxWinsPerFight()));
            }
            System.out.println("__________________________________");
        }
        
        
    }
    
    
    
    //keeps track of how a single fares against all other grids
    private class GridPlusWins{
        private TournamentGrid grid;
        private HashMap<TournamentGrid,Integer> wins;
        private int totalWins;//int? 2 for win, 1 for draw, 0 for lose
        
        public GridPlusWins(TournamentGrid grid){
            this.grid = grid;
            wins = new HashMap<>();
            totalWins = 0;
        }
        
        public TournamentGrid getGrid(){
            return grid;
        }
        
        public HashMap<TournamentGrid,Integer> getWins(){
            return wins;
        }
        
        public int getTotalWins(){
            return totalWins;
        }
        
        public void addGrid(TournamentGrid g, int win){
            wins.put(g, win);
            totalWins += win;
        }
        
        public void removeGrid(TournamentGrid g){
            totalWins -= wins.remove(g);
        }
        
        
    }
    
    
}
