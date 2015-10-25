package boot;

import java.io.IOException;

import presenter.Presenter;
import presenter.ServerProperties;
import server.MazeClientHandler;
import server.MyServer;
import view.ServerGuiWindow;

public class Run {
	public static ServerProperties properties=new ServerProperties();
	public static void main(String[] args) throws IOException {
		ServerGuiWindow ui = new ServerGuiWindow("MyServerGUI", 600, 600);
		//MyServerCliView CliView=new MyServerCliView();
		MyServer server = new MyServer(properties.getServerPort(),new MazeClientHandler(),properties.getNumOfClients(),properties.getServerTimeout());
		Presenter p = new Presenter(ui, server);
		ui.addObserver(p);  
		server.addObserver(p);
		new Thread(ui).run();
	}
}
