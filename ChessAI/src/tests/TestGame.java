package tests;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.LazySearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;
import chess.bots.*;

public class TestGame {
    public Searcher<ArrayMove, ArrayBoard> whitePlayer;
    public Searcher<ArrayMove, ArrayBoard> blackPlayer;
    //public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    public static final String STARTING_POSITION = "rnbqkbnr/pp1p2pp/4p3/2p2p2/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq -";

    private ArrayBoard board;
    
    public static void main(String[] args) {
        TestGame game = new TestGame();
        game.play();
    }

    public TestGame() {
        setupWhitePlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 3, 3);
    		//setupWhitePlayer(new ParallelSearcher<ArrayMove, ArrayBoard>(), 5, 1);
        setupBlackPlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 4, 4);
    }
    
    public void play() {
//        int NUM_TESTS = 6;
//        int NUM_WARMUP = 2;
//        double totalTime = 0;
        /* Note that this code does NOT check for stalemate... */
//        for (int i = 0; i < NUM_TESTS; i++) {
    	
	       this.board = ArrayBoard.FACTORY.create().init(STARTING_POSITION);
	       Searcher<ArrayMove, ArrayBoard> currentPlayer = this.blackPlayer;
	       int turn = 0;
//    	       long startTime = System.currentTimeMillis();

			while (!board.inCheck() || board.generateMoves().size() > 0) {
		           currentPlayer = currentPlayer.equals(this.whitePlayer) ? this.blackPlayer : this.whitePlayer;
		           System.out.printf("%3d: " + board.fen() + "\n", turn);
		           this.board.applyMove(currentPlayer.getBestMove(board, 1000, 1000));
		           turn++;
		    }
			
//			long endTime = System.currentTimeMillis();
//			if (NUM_WARMUP <= i) {
//				totalTime += (endTime - startTime);
//			}
       //} 
       //double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
//	   double averageRuntime = endTime - startTime;
//       System.out.println(averageRuntime);
//       System.err.println(averageRuntime);
    }
    
    public Searcher<ArrayMove, ArrayBoard> setupPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());
        return searcher; 
    }
    public void setupWhitePlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.whitePlayer = setupPlayer(searcher, depth, cutoff);
    }
    public void setupBlackPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.blackPlayer = setupPlayer(searcher, depth, cutoff);
    }
}