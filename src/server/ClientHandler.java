package server;

import java.io.InputStream;
import java.io.OutputStream;
/**
 * <h1>ClientHandler interface</h1>
 * an interface for handling clients 
 * @author Ariel Rosenfeld and Ofir Calif
 *
 */
public interface ClientHandler {

	/**
	 * <h1>handleClient</h1>
	 * the main logic,
	 * responsible for the client conversation
	 * @param inFromClient the inputStream  that contains the client request 
	 * @param outToClient the outputStream that will contains the server response
	 */
	void handleClient(InputStream inFromClient,OutputStream outToClient);
	
	/**
	 * <h1>shutdown</h1>
	 * responsible to close the client handler safely
	 */
	void shutdown();
	
	/**
	 * <h1>initialize</h1>
	 * initializing the clientHandler
	 */
	void initialize();
}
  