package experiments;

import java.lang.*;
import java.util.*;
import java.text.SimpleDateFormat;

import models.*;

public class BacktrackingExperiment{
    private static SudokuSolver solver;
    private static final int maxRank = 7; // inclusive
    private static final int samples = 5;

    public static void main(String[] args){
	Vector<Integer> ranks = new Vector<Integer>();
	for(int rank = 2; rank <= maxRank; rank++)
	    ranks.add(rank);

        Map<Integer, Double> maxRates = new HashMap<>();
	maxRates.put(2, 0.9);
	maxRates.put(3, 0.8);
	maxRates.put(4, 0.4);
	maxRates.put(5, 0.3);
	maxRates.put(6, 0.2);
	maxRates.put(7, 0.2);
	
	//	solver = new HeuristicSolver();
	solver = new BacktrackingSolver();
	
	for(int rank: ranks){
	    double holesRate = 0.1;
	    while( holesRate <= maxRates.get(rank) + (1e-9)){				
		//experiment(rank, holesRate, true); 
		experiment(rank, holesRate, false);		
		holesRate += 0.1;
	    }
	}
    }

    public static void experiment(int rank, double holesRate, boolean findAll)
    throws IllegalArgumentException{
	if( !(0<holesRate&&holesRate<=1) )
	    throw new IllegalArgumentException("HolesRate should be between 0 and 1.");

	double avgTime = 0;
	double avgExpanded = 0;
	for(int i = 0; i < samples; i++){
	    final int holes = (int)Math.floor(Math.pow(rank, 4)*holesRate);
	    State initial = SudokuGenerator.generate(rank*rank, holes);
	    //System.err.println(String.format("Rank: %d, Holes: %d, HRate: %.2f", rank, holes, holesRate));

	    State solved = solver.solve(initial, findAll);
	    final long executionTime = solver.getExecutionTime();
	    final long visitedNodes = solver.getVisitedNodes();

	    avgTime += executionTime;
	    avgExpanded += visitedNodes;

	    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	    //System.out.println(String.format("%2d %5d %.2f %c %15d %15d",
	    //rank, holes, holesRate, (findAll)?'T':'F',
	    //executionTime, visitedNodes));
	}
	avgTime /= samples;
	avgExpanded /= samples;
	
	System.out.println(String.format("%2d %.2f %.6f %.6f",
					 rank, holesRate, avgTime, avgExpanded));
    }
    
}
