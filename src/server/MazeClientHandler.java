package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;

public class MazeClientHandler implements ClientHandler {
	SolutionMaker solutionMaker;
	public MazeClientHandler() {
		solutionMaker = SolutionMaker.getInstance();
	}

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {	
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outToClient);
			ObjectInputStream objectInputStream=new ObjectInputStream(inFromClient);
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

