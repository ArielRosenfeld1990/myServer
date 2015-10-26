package presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ServerProperties implements Serializable{
	private static volatile ServerProperties instance;
	private  int numOfClients;
	private  int ServerPort;
	private  String IPaddress;
	private  String Searcher;
	private  int serverTimeout;
	private  int ThreadNumber;  
	private Document docXML;
	/**
	 * 
	 */
	private final long serialVersionUID = 1L;
	private ServerProperties(){
		try {
			loadFromXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized static ServerProperties getInstance() {
		if(instance==null)
			instance = new ServerProperties();
		return instance;
	}

	public ServerProperties(InputStream doc) throws FileNotFoundException{
		try {	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			docXML=dBuilder.parse(doc);
			loadFromXML();
		}
		catch (FileNotFoundException e){
			throw new FileNotFoundException("Properties file wasnt found");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveToXML() {
		try {
			Integer num1 = numOfClients;
			Integer num2 = ServerPort;
			Integer num3 = serverTimeout;
			Integer num4 = ThreadNumber;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument(); 
			Element Properties = doc.createElement("ServerProperties");
			doc.appendChild(Properties);
			Element ClientsNumber = doc.createElement("NumberOfClients");
			ClientsNumber.appendChild(doc.createTextNode(num1.toString()));
			Properties.appendChild(ClientsNumber);
			Element PortOfServer = doc.createElement("ServerPort");
			PortOfServer.appendChild(doc.createTextNode(num2.toString()));
			Properties.appendChild(PortOfServer);
			Element ServerIpAddress = doc.createElement("ServerIP");
			ServerIpAddress.appendChild(doc.createTextNode(IPaddress));
			Properties.appendChild(ServerIpAddress);
			Element ServerSearcher = doc.createElement("ServerSeacher");
			ServerSearcher.appendChild(doc.createTextNode(Searcher));
			Properties.appendChild(ServerSearcher);
			Element Timeout = doc.createElement("ServerTimeout");
			Timeout.appendChild(doc.createTextNode(num3.toString()));
			Properties.appendChild(Timeout);
			Element ThreadNum = doc.createElement("ThreadNum");
			ThreadNum.appendChild(doc.createTextNode(num4.toString()));
			Properties.appendChild(ThreadNum);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("ServerProperties.xml"));
			transformer.transform(source, result);
			System.out.println("Properties saved");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}
	public void loadFromXML() throws FileNotFoundException {

		try {
			if (docXML==null) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder;
				dBuilder = dbFactory.newDocumentBuilder();
				docXML = dBuilder.parse("ServerProperties.xml");
			}

			NodeList nList = docXML.getElementsByTagName("ServerProperties");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String ClientsNum = eElement.getElementsByTagName("NumberOfClients").item(0).getTextContent();
					numOfClients=Integer.parseInt(ClientsNum);
					String PortOfServer = eElement.getElementsByTagName("ServerPort").item(0).getTextContent();
					ServerPort = Integer.parseInt(PortOfServer);
					IPaddress = eElement.getElementsByTagName("ServerIP").item(0).getTextContent();
					Searcher=eElement.getElementsByTagName("ServerSeacher").item(0).getTextContent();
					String Timeout = eElement.getElementsByTagName("ServerTimeout").item(0).getTextContent();
					serverTimeout=Integer.parseInt(Timeout);
					String Threadnum = eElement.getElementsByTagName("ThreadNum").item(0).getTextContent();
					ThreadNumber=Integer.parseInt(Threadnum);
				}
				System.out.println("file loaded successfully");
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block 
			throw new FileNotFoundException("Properties file wasnt found");
		}

	}

	public  int getNumOfClients() {
		return numOfClients;
	}

	public void setNumOfClients(int numOfClients) {
		this.numOfClients = numOfClients;
	}

	public int getServerPort() {
		return ServerPort;
	}

	public void setServerPort(int serverPort) {
		this.ServerPort = serverPort;
	}

	public String getIPaddress() {
		return IPaddress;
	}

	public void setIPaddress(String iPaddress) {
		this.IPaddress = iPaddress;
	}

	public String getSearcher() {
		return Searcher;
	}

	public void setSearcher(String searcher) {
		this.Searcher = searcher;
	}

	public int getServerTimeout() {
		return serverTimeout;
	}

	public void setServerTimeout(int serverTimeout) {
		this.serverTimeout = serverTimeout;
	}

	public int getThreadNumber() {
		return ThreadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.ThreadNumber = threadNumber;
	}

}
