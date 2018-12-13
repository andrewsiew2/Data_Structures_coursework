package experiment;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.JamboreeSearcher;
import chess.bots.LazySearcher;
import chess.bots.ParallelSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

public class sequentialCutoffTest {
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
        //Searcher<ArrayMove, ArrayBoard> dumb = new LazySearcher<>();
        //printMove(STARTING_POSITION, dumb, 3, 0);
        
        // Figure out Jamboree and ParallelSearcher sequentialCutt off for 3 game sets 
        // BEGIN—MID—END
        //
        
        //BEGIN - rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -
        //MID - r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -
        //END - 2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -
        

        //Depth 5 vary seq cutoff from 1-4
        String[] fens = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", "r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -","2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -"};
        for (String fen: fens) {
        		System.out.println("Experiment begins with fen: " + fen);
	        	Searcher <ArrayMove, ArrayBoard> parallelBot = new ParallelSearcher<ArrayMove,ArrayBoard>();
	        	Searcher <ArrayMove, ArrayBoard> jamboreeBot = new JamboreeSearcher<ArrayMove,ArrayBoard>();
	        	performTestAllCutoff(fen, parallelBot, "ParallelSearcher");
	        	performTestAllCutoff(fen, jamboreeBot, "JamBoreeSearcher");
        } 
    }
    
    public static void performTestAllCutoff(String fen, 	Searcher <ArrayMove, ArrayBoard> bot, String name) {
        int NUM_TESTS = 25;
        int NUM_WARMUP = 5;
		for (int i = 1; i < 5; i++) {
			// Do warmUp!
			double totalTime = 0;
			for (int j = 0; j < NUM_TESTS; j++) {
				long startTime = System.currentTimeMillis();
				getBestMove(fen, bot, 5, i);
				long endTime = System.currentTimeMillis();
				if (NUM_WARMUP <= j) {
					totalTime += (endTime - startTime);
				}
			}
			double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
			System.out.println("Cutoff: " + i + " Average Time: " + averageRuntime + " Bot: " + name);
		}
    }
}
