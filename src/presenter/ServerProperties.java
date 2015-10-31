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


@SuppressWarnings("serial")
public class ServerProperties implements Serializable{
	private static volatile ServerProperties instance;
	private  int numOfClients;
	private  int ServerPort;
	private  String Searcher;
	private  int serverTimeout;
	private  int ThreadNumber;  
	private Document docXML;
	/**
	 * <h1>ServerProperties</h1>
	 * @author Ariel Rosenfeld and Ofir Calif
	 * ServerProperties class contains all the static configuration settings that are
	 * required to our server side
	 *
	 */
	/**
	 * <h1>ServerProperties constructor</h1>
	 * our ServerProperties constructor is loading the configuration settings from the
	 * XML file to the relevant data members
	 */
	private ServerProperties(){
		try {
			loadFromXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * <h1>getInstance</h1>
 * getInstance is part of our singleton implementation.
 * returns the instance of our class if it doesn't exsist it creats a new instance 
 * @return instance is the instance for our class
 */
	public synchronized static ServerProperties getInstance() {
		if(instance==null)
			instance = new ServerProperties();
		return instance;
	}
  /**
   * <h1>ServerProperties constructor</h1>
   * ServerProperties constructor is our non-default constructor
   * @param doc is the given XML file
   * @throws FileNotFoundException is thrown in case such a file wasn't found
   */
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
			
			e.printStackTrace();
		} catch (SAXException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
/**
 * <h1>saveToXML</h1>
 * saveToXML method is for saving the ServerProperties data members into
 * a XML file
 */
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
	/**
	 * <h1>loadFromXML</h1>
	 * loadFromXML method is for loading settings from ServerProperties XML into
	 * our class data members
	 * @throws FileNotFoundException is thrown if such a XML wasn't found
	 */
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
					Searcher=eElement.getElementsByTagName("ServerSeacher").item(0).getTextContent();
					String Timeout = eElement.getElementsByTagName("ServerTimeout").item(0).getTextContent();
					serverTimeout=Integer.parseInt(Timeout);
					String Threadnum = eElement.getElementsByTagName("ThreadNum").item(0).getTextContent();
					ThreadNumber=Integer.parseInt(Threadnum);
				}
				System.out.println("file loaded successfully");
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new FileNotFoundException("Properties file wasnt found");
		}

	}
/**
 * <h1>getNumOfClients</h1>
 * getNumOfClients is a getter for numOfClients data member
 * @return numOfClients datamember
 */
	public  int getNumOfClients() {
		return numOfClients;
	}
/**
 * <h1>setNumOfClients</h1>
 * setNumOfClients is a setter for numOfClients
 * @param numOfClients is the value to change the datamember
 */
	public void setNumOfClients(int numOfClients) {
		this.numOfClients = numOfClients;
	}
/**
 * <h1>getServerPort</h1>
 * getServerPort is a getter for the ServerPort
 * @return ServerPort datamember
 */
	public int getServerPort() {
		return ServerPort;
	}
/**
 * <h1>setServerPort</h1>
 * setServerPort is a setter for our ServerPort datamember
 * @param serverPort is the value to put into the ServerPort member
 */
	public void setServerPort(int serverPort) {
		this.ServerPort = serverPort;
	}

/**
 * <h1>getSearcher</h1>
 * getSeacher is our getter for the Searcher data member
 * @return Searcher data member
 */
	public String getSearcher() {
		return Searcher;
	}
/**
 * <h1>setSearcher</h1>
 * setSearcher is a setter for the searcher data member
 * @param searcher is the value for the searcher data member
 */
	public void setSearcher(String searcher) {
		this.Searcher = searcher;
	}
/**
 * <h1>getServerTimeout</h1>
 * getServerTimeout is a getter for our serverTimeout data member
 * @return serverTimeout datamember
 */
	public int getServerTimeout() {
		return serverTimeout;
	}
/**
 * <h1>setServerTimeout</h1>
 * setServerTimeout is a setter for our serverTimeout data member
 * @param serverTimeout is the value for the serverTimeout datamember
 */
	public void setServerTimeout(int serverTimeout) {
		this.serverTimeout = serverTimeout;
	}
/**
 * <h1>getThreadNumber</h1>
 * getThreadNumber is a getter for Threadnumber data member
 * @return ThreadNumber data member
 */
	public int getThreadNumber() {
		return ThreadNumber;
	}
/**
 * <h1>setThreadNumber</h1>
 * setThreadNumber is a setter for ThreadNumber data member
 * @param threadNumber is the value for the ThreadNumber data member
 */
	public void setThreadNumber(int threadNumber) {
		this.ThreadNumber = threadNumber;
	}

}
