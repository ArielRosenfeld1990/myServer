package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
/**
 * <h1>ServerCLI</h1>
 * ServerCLI class represents a CLI in order to interact between the client
 * and the View model,this class is Observable so the View layer could get a notify
 * when a change occured in the CLI
 * 
 * @author Ariel Rosenfeld and Ofir Calif
 *
 */

public class ServerCLI extends Observable{
	private BufferedReader in; 
	private PrintWriter out;
	private String[] splitedCommand;
	Thread mainThread;
	volatile boolean stop;
   
	/**
	 * <h1>ServerCLI constructor</h1>
	 * ServerCLI constructor initiliazing in and out datamembers
	 * @param in is an InputStream for the in data member
	 * @param out is an OutputStream for the out data member
	 */
	public ServerCLI(InputStream in, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = new PrintWriter(out);
		splitedCommand = null;

	}

	/**
	 * <h1>start</h1>
	 * this method is used to start the main loop of the cli-starting the
	 * interaction between and client and the CLI
	 */
	public void start() {
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String inputString;
				try {
					while (!stop) {
						inputString = in.readLine();
						splitedCommand = inputString.split(" ");
						setChanged();
						notifyObservers();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mainThread.start();
	}

	/**
	 * <h1>display</h1>
	 * display method is for displaying the result in the CLI
	 * @param obj is the object that we got.
	 * @param displayer is the kind of displayer we need to show in the CLI.
	 */
	public void display(String params) {
		out.println(params);
	}

	/**
	 * <h1>close</h1>
	 * close method stops the main loop and stopping the CLI
	 */
	public void close() {
		stop = true;
		out.println("bye");
		out.flush();
	}

	/**
	 * <h1>getUserCommand</h1>
	 * getUserCommand method is used to get the user command
	 */

	public String[] getUserCommand() {
		return splitedCommand;
	}


}
