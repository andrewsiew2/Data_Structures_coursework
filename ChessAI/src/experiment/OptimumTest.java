package experiment;

import java.util.Map;
import java.util.TreeMap;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.JamboreeSearcher;
import chess.bots.LazySearcher;
import chess.bots.ParallelSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

public class OptimumTest {
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static ArrayMove getBestMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0);
    }
    
    public static void printMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        String botName = searcher.getClass().toString().split(" ")[1].replace("chess.bots.", "");
        System.out.println(botName + " returned " + getBestMove(fen, searcher, depth, cutoff));
    }
    public static void main(String[] args) {
        //Depth 5 vary seq cutoff from 1-4
        String[] fens = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", "r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -","2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -"};
		ParallelSearcherCopy <ArrayMove, ArrayBoard> parallelBot = new ParallelSearcherCopy<ArrayMove,ArrayBoard>();
		SimpleSearcherCopy<ArrayMove, ArrayBoard> simpleBot = new SimpleSearcherCopy<ArrayMove, ArrayBoard>();
		JamboreeSearcherCopy<ArrayMove, ArrayBoard> jamboreeBot = new JamboreeSearcherCopy<ArrayMove,ArrayBoard>();
		AlphaBetaSearcherCopy<ArrayMove, ArrayBoard> alphaBetaBot = new AlphaBetaSearcherCopy<ArrayMove, ArrayBoard>();
		Map<String, Searcher<ArrayMove, ArrayBoard>> nameToBotsMap = new TreeMap<String, Searcher<ArrayMove, ArrayBoard>>();
		Map<String, Integer> cutOffMap = new TreeMap<String, Integer>();
		cutOffMap.put("Simple", 2);
		cutOffMap.put("Parallel", 2);
		cutOffMap.put("AlphaBeta", 2);
		cutOffMap.put("Jamboree", 2);
		//REMMEBER TO SET CUT OFF AND PROCESSOR NUM!
		parallelBot.setProcessorCount(32);
		jamboreeBot.setProcessorCount(25);
		nameToBotsMap.put("Simple", simpleBot);
		nameToBotsMap.put("Parallel", parallelBot);
		nameToBotsMap.put("AlphaBeta", alphaBetaBot);
		nameToBotsMap.put("Jamboree", jamboreeBot);
        int NUM_TESTS = 25;
        int NUM_WARMUP = 5;
		for (String fen: fens) {
        		System.out.println("Experiment  begins with fen: " + fen);
        		for (String botName: nameToBotsMap.keySet()) {
        			Searcher<ArrayMove, ArrayBoard> bot = nameToBotsMap.get(botName);
        			System.out.println("Current bot in test: " + botName);
	    	    		double totalTime = 0;
	    	    		for (int j = 0; j < NUM_TESTS; j++) {
	    	    			long startTime = System.currentTimeMillis();
	    	    			getBestMove(fen, bot, 5, cutOffMap.get(botName));
	    	    			long endTime = System.currentTimeMillis();
	    	    			if (NUM_WARMUP <= j) {
	    	    				totalTime += (endTime - startTime);
	    	    			}
	    	    		}
	    	    		System.out.println("Bot: " + botName + " Average Runtime: " + totalTime);
	    	    		
        		}
        } 
    }

}
