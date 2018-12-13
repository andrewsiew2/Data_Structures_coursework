package experiment;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
//import chess.bots.JamboreeSearcher;
import chess.bots.LazySearcher;
//import chess.bots.ParallelSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;


public class TestProcessorNum {
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
        String[] fens = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", "r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -","2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -"};
        int cutOffSearcher = 3;
        int cutOffJamboree = 4;
        
        for (String fen: fens) {
	    		System.out.println("Experiment begins with fen: " + fen);
	    		System.out.println("Starting with ParallelSearcher");
	    		ParallelSearcherCopy <ArrayMove, ArrayBoard> parallelBot = new ParallelSearcherCopy<ArrayMove,ArrayBoard>();
	    		JamboreeSearcherCopy <ArrayMove, ArrayBoard> jamboreeBot = new JamboreeSearcherCopy<ArrayMove,ArrayBoard>();

            int NUM_TESTS = 25;
            int NUM_WARMUP = 5;

            int lo = 10;
            int hi = 32;
            
	    		double totalTime = 0;
	    		parallelBot.setProcessorCount(lo);
	    		for (int j = 0; j < NUM_TESTS; j++) {
	    			long startTime = System.currentTimeMillis();
	    			getBestMove(fen, parallelBot, 5, cutOffSearcher);
	    			long endTime = System.currentTimeMillis();
	    			if (NUM_WARMUP <= j) {
	    				totalTime += (endTime - startTime);
	    			}
	    		}
	    		double oldRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
	    		
	            
            while (lo != hi) {
	            	int middleNumProc = (lo + hi) / 2;
	            	//System.out.println("Testing with current processor count: " + middleNumProc);
	            	parallelBot.setProcessorCount(middleNumProc);
	            	totalTime = 0;
	    	    		for (int j = 0; j < NUM_TESTS; j++) {
	    	    			long startTime = System.currentTimeMillis();
	    	    			getBestMove(fen, parallelBot, 5, cutOffSearcher);
	    	    			long endTime = System.currentTimeMillis();
	    	    			if (NUM_WARMUP <= j) {
	    	    				totalTime += (endTime - startTime);
	    	    			}
	    	    		}
	    	    		
	    	    		double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
	    	    		System.out.println("Runtime: " + averageRuntime + " ProcessorCount: " + middleNumProc);
	    	    		if (averageRuntime < oldRuntime) {
	    	    			lo = middleNumProc;
	    	    		} else if (averageRuntime > oldRuntime) {
	    	    			hi = middleNumProc;
	    	    		} else {
	    	    			System.out.println("Same runtime for average and old. Runtime is " + averageRuntime);
	    	    			return;
	    	    		}
	    	    		
    	    			oldRuntime = averageRuntime;
    	    			if (lo == hi) {
    	    				System.out.println("Lo == hi. We will stop now");
    	    			} else if (lo == 32) {
    	    				System.out.println("We have maxed out processors! Stopping now...");
    	    			}
            }  
	        System.out.println("Bot: ParallelSearcher The optimal number of processor: " + lo + " Runtime is: " + oldRuntime);

            // JAMBOREE SECTION!!###################################################################################3
    			System.out.println("Starting with Jamboree");

            lo = 10;
            hi = 32;
	    		totalTime = 0;
	    		jamboreeBot.setProcessorCount(lo);
	    		
	    		for (int j = 0; j < NUM_TESTS; j++) {
	    			long startTime = System.currentTimeMillis();
	    			getBestMove(fen, jamboreeBot, 5, cutOffJamboree);
	    			long endTime = System.currentTimeMillis();
	    			if (NUM_WARMUP <= j) {
	    				totalTime += (endTime - startTime);
	    			}
	    		}
	    		oldRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
	    		
	            
	        while (lo != hi) {
	            	int middleNumProc = (lo + hi) / 2;
	            	//System.out.println("Testing with current processor count: " + middleNumProc);
	            	jamboreeBot.setProcessorCount(middleNumProc);
	            	totalTime = 0;
	    	    		for (int j = 0; j < NUM_TESTS; j++) {
	    	    			long startTime = System.currentTimeMillis();
	    	    			getBestMove(fen, jamboreeBot, 5, cutOffJamboree);
	    	    			long endTime = System.currentTimeMillis();
	    	    			if (NUM_WARMUP <= j) {
	    	    				totalTime += (endTime - startTime);
	    	    			}
	    	    		}
	    	    		
	    	    		double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
	    	    		System.out.println("Runtime: " + averageRuntime + " ProcessorCount: " + middleNumProc);

	    	    		if (averageRuntime < oldRuntime) {
	    	    			lo = middleNumProc;
	    	    		} else if (averageRuntime > oldRuntime) {
	    	    			hi = middleNumProc;
	    	    		} else {
	    	    			System.out.println("Same runtime for average and old. Runtime is " + averageRuntime);
	    	    			return;
	    	    		}
	    	    		
	    			oldRuntime = averageRuntime;
	    			if (lo == hi) {
	    				System.out.println("Lo == hi. We will stop now");
	    			} else if (lo == 32) {
	    				System.out.println("We have maxed out processors! Stopping now...");
	    			}
	        }  
	        System.out.println("Bot: Jamboree The optimal number of processor: " + lo + " Runtime is: " + oldRuntime);
        }        
        
    }
    
}
