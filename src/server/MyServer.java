package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presenter.Presenter;
import presenter.ServerProperties;
import view.MyServerCliView;
import view.ServerGuiWindow;

public class MyServer extends Observable{
	int port;
	ServerSocket server;
	volatile boolean stop;
	ClientHandler clientHandler;
	int numOfClients;
    int connectedClients;
    public static ServerProperties properties=new ServerProperties();
    
	public MyServer(int port,ClientHandler clientHandler,int numOfClients) {
		this.port=port;
		this.clientHandler=clientHandler;
		this.numOfClients=numOfClients;
		connectedClients=0;
	}

	public void start() throws IOException
	{
		setChanged();
		notifyObservers("Waiting for client connection please wait..."+'\n');
		server=new ServerSocket(port);
		server.setSoTimeout(10*1000);
		final ExecutorService threadpool = Executors.newFixedThreadPool(numOfClients);

		new Thread(new Runnable() {

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
								threadpool.execute(new Runnable() {

									@Override
									public void run() {

										try {
											System.out.println("client connected");
											setChanged();
											notifyObservers("client connected"+"\n"+"the number of clients is: "+connectedClients+"\n");
											clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
								//			setChanged();
								//			notifyObservers("the number of clients is: "+connectedClients);
											someClient.getInputStream().close();
											someClient.getOutputStream().close();
											someClient.close();
											//צריך לדאוג שהכל ייסגר גם אם זה קורה באמצע
							
										} catch (IOException e) {e.printStackTrace();}
									}
								});			
							}}
						}catch(SocketTimeoutException e){}
					}catch (IOException e) {e.printStackTrace();}
				}		
			}
		}).start();
		setChanged();
		notifyObservers("server address: "+properties.getIPaddress()+"\n"+
		                "server port is: "+properties.getServerPort()+"\n"
		                +"number of clients to handle is: "+properties.getNumOfClients()+"\n");
	}

	public void close() {
		try {
			if (server!=null){
			stop=true;
			server.close();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
 public void setXMLproperties(String[] args){
	 try{
	 int ClientNum=Integer.parseInt(args[1]);
	 int PortNum=Integer.parseInt(args[3]);
	 if ((ClientNum>0&&ClientNum<100)&&(PortNum>=1000&&PortNum<=9999)&&(args[2].equals("127.0.0.1")||args[2].equals("localhost"))) {
		properties.setNumOfClients(ClientNum); 
		properties.setServerPort(PortNum);
		properties.setIPaddress(args[2]);
		properties.saveToXML();
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

	public static void main(String[] args) throws IOException {
		properties.saveToXML();
/*		System.out.println("start server");
		MyServer server = new MyServer(5400,new MazeClientHandler(),10);
		server.start();
		int x= System.in.read();
		server.close();*/
		ServerGuiWindow ui = new ServerGuiWindow("MyServerGUI", 600, 600);
		//MyServerCliView CliView=new MyServerCliView();
		MyServer server = new MyServer(properties.getServerPort(),new MazeClientHandler(),properties.getNumOfClients());
		Presenter p = new Presenter(ui, server);
		ui.addObserver(p);
		server.addObserver(p);
		new Thread(ui).run();
	//	ui.run();
		int x=System.in.read();
		server.close();
	}

}
