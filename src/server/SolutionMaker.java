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
import java.util.concurrent.TimeUnit;
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
import presenter.ServerProperties;

/**
 * <h1>class SolutionMaker</h1>
 * this class responsible for creating solutions
 * @author ofir calif and ariel rosenfeld
 *
 */
public class SolutionMaker {

	private ExecutorService threadPool;
	private HashMap<Maze3d, Solution> mazesSolution;   

	/**
	 * <h1>SolutionMaker</h1>
	 * the constructor initialize the threadpool and load solution cache
	 */
	public SolutionMaker() {
		threadPool = Executors.newFixedThreadPool(ServerProperties.getInstance().getThreadNumber());
		loadCache();
	}

/**
 * <h1>solve method</h1>
 * this method get a Maze3d maze, solve him and return his solution
 * @param maze the maze to solve
 * @return the solution for the given maze
 * @throws Exception if it failed to create a solution
 */
	public Solution Solve(Maze3d maze) throws Exception
	{
		solveByMaze(maze, ServerProperties.getInstance().getSearcher());
		Solution solution;
		if((solution=mazesSolution.get(maze))!=null)
		{
			return solution;
		}
		else {
			throw new Exception("failed to create solution");
		}
	}

	/**
	 * <h1>solveByMaze method</h1>
	 * this method creates a searcher for the given algorithm and use it to solve the given maze.
	 * after his done, the method insert the solution to the hashMap and save the cache to a file
	 * @param maze the maze to solve
	 * @param algorithm the algorithm to solve by
	 * @throws InterruptedException throws if there the thread was interrupted
	 * @throws ExecutionException throws if the thread was failed to execute
	 */
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
	 * <h1>saveCache method</h1>
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
	 * <h1>loadCache method</h1>
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

	/**
	 * <h1>close method</h1>
	 * closes all working threads
	 */
	public void close(){
		threadPool.shutdown();

		try {
			while (!threadPool.awaitTermination(10,TimeUnit.SECONDS)){

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
