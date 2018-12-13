package chess.bots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class JamboreeSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {
	private static final double PERCENTAGE_SEQUENTIAL = 0.5;
	private static final int DEPTH_CUTOFF = 3;
	private static ForkJoinPool POOL = new ForkJoinPool();
	
	
    public M getBestMove(B board, int myTime, int opTime) {
    		List moves = board.generateMoves();
    		Collections.sort(moves, new Optimizer());
    		return (M) POOL.invoke(new Jamboree(board, evaluator, ply, cutoff, 0,
    				moves.size(), moves,-this.evaluator.infty(), this.evaluator.infty())).move;
    } 
    
    public static void setProcessorCount(int k) {
    		POOL = new ForkJoinPool(k);
    }
    
    public static class Jamboree extends RecursiveTask<BestMove> {
    		private Board board;
		private Evaluator eval;
		private int ply;
		private int cutoff;
		private int lo;
		private int hi;
		private List moves;
		private int alpha;
		private int beta;
    	
	    	public Jamboree(Board board, Evaluator eval, int ply, int cutoff, int lo, int hi, List moves, int alpha, int beta) {
			this.board = board;
			this.eval = eval;
			this.ply = ply;
			this.cutoff = cutoff;
			this.lo = lo;
			this.hi = hi;
			this.moves = moves; 
			this.alpha = alpha;
			this.beta = beta;
		}
	    	
	    	protected BestMove compute() {
	    		if(ply > 0) {
	    			List moves = board.generateMoves();
	    			if(moves.isEmpty()) {
	    				if(board.inCheck()) {
	    					return new BestMove(-eval.mate()-ply);
	    				}else {
	    					return new BestMove(-eval.stalemate());
	    				}
	    			}
	    			if(ply <= cutoff) {
	    				AlphaBetaSearcher cut = new AlphaBetaSearcher();
	    				BestMove move = cut.aplhabeta(eval, board, ply, alpha, beta);
	    				return move;
	    			}
	    			Move bestMove = null;
	    		    	for(double i = 0.0; i < PERCENTAGE_SEQUENTIAL * moves.size(); i += 1.0) {
	    		    		Move move = (Move) moves.get((int) i);

	    		    		board.applyMove(move);
	    		    		List movesList = board.generateMoves();
	    		    		Jamboree seq = new Jamboree(board, eval, ply - 1, cutoff, 0, 
	    		    				movesList.size(), movesList, -beta, -alpha);
	    		    		BestMove curr = seq.compute().negate();
	    		    		board.undoMove();
	    		    		
	    		    		if (curr.value > alpha) {
			    			alpha = curr.value;
			    			bestMove = move;
			    		}if (alpha >= beta) {
			    			return new BestMove(bestMove, alpha);
			    		}
	    		    	}
	    		    	
	    		    	// Find the mid of past percentage sequel and call parallelJamboree
	    		    	
	    		    	JamboreeParallel parallel = new JamboreeParallel(board, eval, ply, cutoff, (int) (PERCENTAGE_SEQUENTIAL * moves.size()),
	    		    			moves.size(), moves, alpha, beta);
	    		    	BestMove parallelPart = parallel.compute();
	    		    	if(parallelPart.value > alpha) {
	    		    		return parallelPart;
	    		    	}
	    		    	return new BestMove(bestMove, alpha);

	    		}
	    		return new BestMove(null, eval.eval(board)); 
	    	}
    }
    
    public static class JamboreeParallel extends RecursiveTask<BestMove> {  
    		
		private Board board;
		private Evaluator eval;
		private int ply;
		private int cutoff;
		private int lo;
		private int hi;
		private List moves;
		private int alpha;
		private int beta;
		
	    	public JamboreeParallel(Board board, Evaluator eval, int ply, int cutoff, int lo, int hi, List moves, int alpha, int beta) {
			this.board = board;
			this.eval = eval;
			this.ply = ply;
			this.cutoff = cutoff;
			this.lo = lo;
			this.hi = hi;
			this.moves = moves; 
			this.alpha = alpha;
			this.beta = beta;
		}

		@Override
		protected BestMove compute() {
			if(ply <= cutoff) {
				return AlphaBetaSearcher.aplhabeta(eval, board, ply, alpha, beta);
			}
			if (hi - lo <= DEPTH_CUTOFF) { // Sequential forking
				
				Move bestMove = null;
				ArrayList<Jamboree> list = new ArrayList<Jamboree>();
				for(int i = lo; i < hi; i++) {
					Board newBoard = (Board) board.copy();
			    		Move move = (Move) moves.get(i);
			    		newBoard.applyMove(move);
			    		List movesList = newBoard.generateMoves();
			    		Jamboree seqFork = new Jamboree(newBoard, eval, ply - 1, cutoff, 0, 
			    				movesList.size(), movesList, -beta, -alpha);
			    		seqFork.fork();
			    		list.add(seqFork);
				}
				for (int i = 0; i < list.size(); i++) {
					BestMove apparentBestMove = list.get(i).join().negate();
					if (apparentBestMove.value > alpha) {
			    			alpha = apparentBestMove.value;
			    			bestMove = (Move) moves.get(lo + i);
			    		}if (alpha >= beta) {
			    			return new BestMove(bestMove, alpha);
			    		}
				}
				return new BestMove(bestMove, alpha);
			}else {
				int middle = lo + ((hi - lo) / 2);
				// Call back the original Jamboree
				JamboreeParallel leftSplit = new JamboreeParallel(board, eval, ply, cutoff, lo, middle, moves, alpha, beta);
				JamboreeParallel rightSplit = new JamboreeParallel(board, eval, ply, cutoff, middle, hi, moves, alpha, beta);
				leftSplit.fork();
				BestMove rightBestMove = rightSplit.compute();
				BestMove leftBestMove = leftSplit.join();
				if (leftBestMove.value > rightBestMove.value) {
					return leftBestMove;
		    		}
				return rightBestMove;
			}

			
	    	}	
    	
    
    }
}
