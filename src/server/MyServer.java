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
import boot.Run;

public class MyServer extends Observable{
	int port;
	ServerSocket server;
	volatile boolean stop;
	ClientHandler clientHandler;
	int connectedClients;
	int timeOut;
	Thread mainThread;
	int numOfClients;
	final ExecutorService threadPool;

	public MyServer(int port,ClientHandler clientHandler,int numOfClients,int timeOut) {
		this.port=port;
		this.clientHandler=clientHandler;
		connectedClients=0;
		threadPool = Executors.newFixedThreadPool(numOfClients);
		server=null;
		this.timeOut=timeOut;
		this.numOfClients=numOfClients;
		
	}

	public void start() throws IOException
	{
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
							connectedClients++;

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
				"server port is: "+server.getLocalPort()+"\n"
				+"number of clients to handle on the same time is: "+numOfClients+"\n");
	}

	public void close() {
		try {
			stop=true;
			threadPool.shutdown();
			while (!threadPool.awaitTermination(10, TimeUnit.SECONDS));
			if (server!=null){
				mainThread.join();
				server.close();
			}
		} catch (IOException e) {  

			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setXMLproperties(String[] args){
		try{
			int ClientNum=Integer.parseInt(args[1]);
			int PortNum=Integer.parseInt(args[3]);
			if ((ClientNum>0&&ClientNum<100)&&(PortNum>=1000&&PortNum<=9999)&&(args[2].equals("127.0.0.1")||args[2].equals("localhost"))) {
				Run.properties.setNumOfClients(ClientNum); 
				Run.properties.setServerPort(PortNum);
				Run.properties.setIPaddress(args[2]);
				Run.properties.saveToXML();
				setChanged();
				notifyObservers("XML saved successfully");

			}
			else {
				setChanged();
				notifyObservers("bad field input,failed to set properties");
			}
		} catch(Exception e) {
			setChanged();
			notifyObservers("failed to set properties");
		}


	}



}
