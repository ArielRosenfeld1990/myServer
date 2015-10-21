package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.search.AStar;
import algorithms.search.Bfs;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import io.MyDecompressorInputStream;

public class MazeClientHandler implements ClientHandler {
	static ExecutorService threadPool = Executors.newFixedThreadPool(5);//קונפיגורציה למספר התרדים

	static HashMap<Maze3d, Solution> mazesSolution= new HashMap<Maze3d,Solution>(); //יש דרך יותר יפה לאתחל את המשתנה
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		try {
			
			ObjectInputStream in = new ObjectInputStream(inFromClient);
			PrintWriter out = new PrintWriter(new ObjectOutputStream(outToClient));
			while()
			in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void solveByMaze(Maze3d maze,String algorithm) {

		if (mazesSolution.containsKey(maze)){
			return;
		}

		Future<Solution> solutionFuture = threadPool.submit(new Callable<Solution>() {
			@Override
			public Solution call() throws Exception {
				Searcher searcher;
				switch (algorithm) {
				case "bfs":
					searcher = new Bfs();
					break;
				case "aStarAir":
					searcher = new AStar(new MazeManhattanDistance());
					break;
				case "aStarManhattan":
					searcher = new AStar(new MazeAirDistance());
					break;
				default:
					throw new InvalidParameterException("invalid searcher");
				}
				return searcher.search(new Maze3dSearchable(maze));
			}
		});

		try{
			mazesSolution.put(maze,solutionFuture.get() );
		}
		catch (Exception e) {
		}
	}
}
