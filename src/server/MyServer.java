package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
	int port;
	ServerSocket server;
	volatile boolean stop;
	ClientHandler clientHandler;
	int numOfClients;

	public MyServer(int port,ClientHandler clientHandler,int numOfClients) {
		this.port=port;
		this.clientHandler=clientHandler;
		this.numOfClients=numOfClients;
	}

	public void start() throws IOException
	{
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

						try{
							someClient= server.accept();

							if(someClient!=null){
								threadpool.execute(new Runnable() {

									@Override
									public void run() {

										try {
											System.out.println("client connected");
											clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
											someClient.getInputStream().close();
											someClient.getOutputStream().close();
											someClient.close();
											//צריך לדאוג שהכל ייסגר גם אם זה קורה באמצע
							
										} catch (IOException e) {e.printStackTrace();}
									}
								});			
							}
						}catch(SocketTimeoutException e){}
					}catch (IOException e) {e.printStackTrace();}
				}		
			}
		}).start();
	}

	public void close() {
		try {
			stop=true;
			server.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws IOException {
		System.out.println("start server");
		MyServer server = new MyServer(5400,new MyClientHandler(),10);
		server.start();
		int x= System.in.read();
		server.close();
	}

}
