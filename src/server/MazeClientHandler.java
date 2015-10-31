package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;

/**
 * <h1>MazeClientHandler class</h1>
 * this class implements the ClientHandler interface
 * and responsible for the client-server conversation
 * @author ofir calif and ariel rosenfeld
 *
 */
public class MazeClientHandler implements ClientHandler {
	private SolutionMaker solutionMaker;
	
	/**
	 * <h1>MazeClientHandler</h1>
	 * constructor that initialize class dataMembers
	 */
	public MazeClientHandler() {
		initialize();
	}

	/**
	 * an override method of the HandleClient interface that create a specific conversation between the client to the server
	 * expected to get an arrayList<object> that contains command and the proper objects for the command
	 */
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {	
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outToClient);
			ObjectInputStream objectInputStream=new ObjectInputStream(inFromClient);

			@SuppressWarnings("unchecked")
			ArrayList<Object> problem =(ArrayList<Object>)objectInputStream.readObject();

			String command = (String)problem.get(0);
			if(command.equals("get solution")) {	
				Maze3d maze = (Maze3d)problem.get(1);
				Solution solution= solutionMaker.Solve(maze);

				objectOutputStream.writeObject(solution);
				objectOutputStream.flush();

				objectInputStream.readObject();

				objectInputStream.close();
				objectOutputStream.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {  
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void shutdown() {
		solutionMaker.close();
	}

	@Override
	public void initialize() {
		solutionMaker=new SolutionMaker();		
	}

}

