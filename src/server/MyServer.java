package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import presenter.ServerProperties;

/**
 * <h1>MyServer class</h1>
 * this class is the main server,
 * the server responsable of the the server behivor and for remote client connection
 * this server is the model in our mvp design
 * @author ofir calif and ariel rosenfeld
 *
 */
public class MyServer extends Observable{

	private ServerSocket server;
	private ClientHandler clientHandler;
	private volatile boolean stop;
	private int timeOut;
	private int port;
	private int numOfClients;
	private Thread mainThread;
	private ExecutorService threadPool;

	/**	
	 * <h1>MyServer constructor</h1>
	 * the constructor of SyServer,
	 * initialize server properties
	 * @param port the port that the server is listening to
	 * @param clientHandler the clientHandler that handle the clients
	 * @param numOfClients the max parllel clients connection
	 * @param timeOut timeout for client connections
	 */
	public MyServer(int port,ClientHandler clientHandler,int numOfClients,int timeOut) {
		this.port=port;
		this.clientHandler=clientHandler;
		server=null;
		this.timeOut=timeOut;
		this.numOfClients=numOfClients;
	}

	/**
	 * <h1>start method</h1>
	 * this method start the server
	 * @throws IOException in case of failure of starting the server
	 */
	public void start() throws IOException
	{
		threadPool = Executors.newFixedThreadPool(numOfClients);
		clientHandler.initialize();
		setChanged();
		notifyObservers("Waiting for client connection please wait..."+'\n');
		server=new ServerSocket(port);
		server.setSoTimeout(timeOut);
		mainThread =new Thread(new Runnable() {

			@Override
			public void run() {
				while(!stop){
					try{
						System.out.println("waiting for client");
						final Socket someClient ;

						try{if (server.isClosed()==false){
							someClient= server.accept();

							if(someClient!=null){
								threadPool.execute(new Runnable() {

									@Override
									public void run() {

										try {
											System.out.println("client connected");
											setChanged();
											notifyObservers("client connected: "+someClient.getRemoteSocketAddress()+"\n");
											clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
											someClient.close();

										} catch (IOException e) {e.printStackTrace();}
									}
								});			
							}}
						}catch(SocketTimeoutException e){}
					}catch (IOException e) {e.printStackTrace();}
				}		
			}
		});
		mainThread.start();
		setChanged();
		notifyObservers("server address: "+InetAddress.getLocalHost().getHostAddress()+ "\n"+
				"server port is: "+port+"\n"
				+"number of clients to handle on the same time is: "+numOfClients+"\n");
	}

	/**
	 * <h1>disconnect method</h1>
	 * this method close the server and initialize the stop flag for restarting the server 
	 */
	public void disconnect(){
		close();
		stop=false;
		setChanged();
		notifyObservers("Client disconnected");
	}

	/**
	 * <h1>close method</h1>
	 * this method closes the server
	 */
	public void close() {
		try {
			stop=true;
			if(clientHandler!=null)
				clientHandler.shutdown();
			if(threadPool!=null)
			{
				threadPool.shutdown();
				while (!threadPool.awaitTermination(10, TimeUnit.SECONDS));
			}
			if (server!=null){
				mainThread.join();
				server.close();
			}
		} catch (IOException e) {  

			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <h1>setXMLproperties method</h1>
	 *  this method get args that represent the server properties and apply them to the server
	 * @param args
	 */
	public void setProperties(String[] args){
		try{
			ServerProperties properties = ServerProperties.getInstance();
			int ClientNum=Integer.parseInt(args[1]);
			int PortNum=Integer.parseInt(args[2]);
			int serverTimeout=Integer.parseInt(args[3]);
			String searcher = args[4];
			if ((ClientNum>0&&ClientNum<100)&&(PortNum>=1000&&PortNum<=9999)) {
				properties.setNumOfClients(ClientNum); 
				properties.setServerPort(PortNum);
				properties.setServerTimeout(serverTimeout);
				properties.setSearcher(searcher);
				properties.saveToXML();
				numOfClients=ClientNum;
				port=PortNum;
				timeOut=serverTimeout;
				setChanged();
				notifyObservers("XML saved successfully");

			}
			else {
				setChanged();
				notifyObservers("bad fields input,failed to set properties");
			}
		} catch(Exception e) {
			setChanged();
			notifyObservers("failed to set properties");
		}
	}
}
