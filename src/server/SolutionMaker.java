package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.search.AStar;
import algorithms.search.Bfs;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import boot.Run;

public class SolutionMaker {

	private static volatile SolutionMaker instance;
	private ExecutorService threadPool;
	private HashMap<Maze3d, Solution> mazesSolution;   

	private SolutionMaker() {
		threadPool = Executors.newFixedThreadPool(Run.properties.getThreadNumber());//קונפיגורציה למספר התרדים

		loadCache();
	}

	public static SolutionMaker getInstance() {//make it threadsafe
		if(instance==null)
			instance = new SolutionMaker();
		return instance;
	}

	public Solution Solve(Maze3d maze) throws Exception
	{
		solveByMaze(maze, Run.properties.getSearcher());//add configuration
		Solution solution;
		if((solution=mazesSolution.get(maze))!=null)
		{
			return solution;
		}
		else {
			throw new Exception("faild to create solution");
		}
	}
	private void solveByMaze(Maze3d maze,String algorithm) throws InterruptedException, ExecutionException {

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

		mazesSolution.put(maze,solutionFuture.get() );
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				saveCache();

			}
		});
	}
	/**
	 * This method is used for saving our hashmap with the solutions to a Cache.zip file
	 */
	private void saveCache()
	{
		ObjectOutputStream outToCache=null;
		try {
			outToCache = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("Cache.zip")));
			outToCache.writeObject(mazesSolution);
			outToCache.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(outToCache!=null)
				try {outToCache.close();} catch (IOException e) {e.printStackTrace();}
		}
	}

	/**
	 * This method is used for loading our hashmap with the solutions from a Cache.zip file
	 */
	private void loadCache()
	{
		ObjectInputStream inFromCache=null;
		try {
			inFromCache = new ObjectInputStream(new GZIPInputStream(new FileInputStream("Cache.zip")));
			@SuppressWarnings("unchecked")
			HashMap<Maze3d, Solution> temp =(HashMap<Maze3d, Solution>)inFromCache.readObject();
			mazesSolution=temp;
		} catch (IOException e) {
			mazesSolution= new HashMap<Maze3d, Solution>();
		} catch (ClassNotFoundException e) {
			mazesSolution= new HashMap<Maze3d, Solution>();
		}finally {
			if(inFromCache!=null)
				try {inFromCache.close();} catch (IOException e) {e.printStackTrace();}
		}
	}


}
