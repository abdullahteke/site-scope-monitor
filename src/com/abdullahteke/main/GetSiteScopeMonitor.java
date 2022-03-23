package com.abdullahteke.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.abdullahteke.controller.RestClient;
import com.abdullahteke.controller.TimeController;
import com.mercury.sitescope.api.data.IAPIDataAcquisitionProxy;

public class GetSiteScopeMonitor {

	
    private static final String UTF_8 = "UTF-8";
	private static final String LINE_SEPARATOR = "line.separator";
	private static final String USAGE = "data_acquisition";

	String strFilePath;
	Map<String,String> timeValue;
	Map<String,Map<String,String>> monitorTimeValue;
	Map<String,Map<String,Map<String,String>>> hostMonitorTimeValue;
	
	

	
	public GetSiteScopeMonitor() {
		// TODO Auto-generated constructor stub
		timeValue=new HashMap<String,String>();
		monitorTimeValue=new HashMap<String,Map<String,String>>();
		hostMonitorTimeValue= new HashMap<String,Map<String,Map<String,String>>>();
	}

	public static void main(String[] args) {
		
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config/config.properties");
			prop.load(input);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disableCertificateValidation();
		IAPIDataAcquisitionProxy proxy= new IAPIDataAcquisitionProxy(prop.getProperty("endPoint"));
		
		String to=""+TimeController.getManagerInstance().getCurrentDateTime();
		String from=""+TimeController.getManagerInstance().get5MinutesBefore();
		
		String[] query=new String[]{from,to,prop.getProperty("monitorTypes"),prop.getProperty("targetServers"),"",prop.getProperty("monitorNames"),""};
		
		//String[] query2=new String[]{"1560868800000","1560869100000",prop.getProperty("monitorTypes"),prop.getProperty("targetServers"),"",prop.getProperty("monitorNames"),""};

		try {
			byte[]result = proxy.getData(query, prop.getProperty("userName"), prop.getProperty("userPasswd"));
			
			writeResultToFile(result, "logs/result.xml");
			parseXMLFile("logs/result.xml");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	
	public static void writeResultToFile(byte[] byteContent, String strFilePath) {
		Writer fileWriter = null;
		try {
			InputStream byteArrayInputStream = new GZIPInputStream(new ByteArrayInputStream(byteContent));
			String lineSeparator = System.getProperty(LINE_SEPARATOR);
            BufferedReader is = new BufferedReader(new InputStreamReader(byteArrayInputStream, UTF_8));
            StringBuffer xmlFull = new StringBuffer(1024);
            String buf = null;
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFilePath), UTF_8));
            while ((buf = is.readLine()) != null) {
				xmlFull.append(buf).append(lineSeparator);
            }
            fileWriter.write(xmlFull.toString());
		} catch(FileNotFoundException ex) {
			System.out.println("FileNotFoundException : " + ex);
		} catch(IOException ioe) {
			System.out.println("IOException : " + ioe);
        } finally {
            if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
	}

	public static void parseXMLFile(String strFilePath) {
		JSONArray array = new JSONArray();

		File fXmlFile = new File(strFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		String metricDomain="DOMAIN-HBYS";
		String metricClass="";
		String metricName="";
		String metricNode="";
		String metricValue="";
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		
			
			NodeList monList = doc.getElementsByTagName("monitor");
			Element countElement;
			Node countNode;
			HashMap<String, String> counterValueMap;
			JSONObject obj;

			for (int i = 0; i < monList.getLength(); i++) {

				Node monNode = monList.item(i);

				if (monNode.getNodeType() == Node.ELEMENT_NODE) {

					Element monElement = (Element) monNode;
					
					metricNode=monElement.getAttribute("target");
	
					NodeList counterNodeList;
					Node counterNode;
					
					if (monElement.getAttribute("name").contains("SQL Performance Metrics")) {
						
						metricClass="Database-MSSQL";
						counterValueMap= new HashMap<String,String>();
						counterNodeList=monElement.getElementsByTagName("counter");
						for (int k=0;k<counterNodeList.getLength();k++) {
							counterNode=counterNodeList.item(k);
							if (counterNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("Transactions/sec")) {
								countElement=(Element)counterNode;
								metricName="transactionCountPerSec";
								metricValue=countElement.getAttribute("value");
								//System.out.println(metricName+"---"+metricValue);
								
								counterValueMap.put(metricName, metricValue);
								
								obj=RestClient.getManagerInstance().createJsonObject(metricDomain,metricClass,metricNode,counterValueMap);

								array.put(obj);
								break;
							}
						}
						
					}else if (monElement.getAttribute("name").contains("IIS Stats")) {
						metricClass="WEB-IIS";
						
						counterValueMap= new HashMap<String,String>();

						counterNodeList=monElement.getElementsByTagName("counter");
						
						for (int k=0;k<counterNodeList.getLength();k++) {
							counterNode=counterNodeList.item(k);
							//System.out.println(counterNode.getAttributes().getNamedItem("name").getNodeValue());
							if (counterNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("Web Service -- Anonymous Users/sec -- _Total")) {
							
								countElement=(Element)counterNode;
								metricName="userCountPerSec";
								metricValue=countElement.getAttribute("value");
								counterValueMap.put(metricName, metricValue);
						
							}else if (counterNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("Web Service -- Connection Attempts/sec -- _Total")) {
								countElement=(Element)counterNode;
								metricName="conAttempsPerSec";
								metricValue=countElement.getAttribute("value");
								counterValueMap.put(metricName, metricValue);

							}else if (counterNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("Web Service -- Current Anonymous Users -- _Total")) {
								countElement=(Element)counterNode;
								metricName="curUserCount";
								metricValue=countElement.getAttribute("value");
								counterValueMap.put(metricName, metricValue);
							}else if (counterNode.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("Web Service -- Current Connections -- _Total")) {
								countElement=(Element)counterNode;
								metricName="curConCount";
								metricValue=countElement.getAttribute("value");
								counterValueMap.put(metricName, metricValue);
							}
						}
						
						obj=RestClient.getManagerInstance().createJsonObject(metricDomain,metricClass,metricNode,counterValueMap);
						array.put(obj);
					}
					
				}
			}
			RestClient.getManagerInstance().sendDataToBVD(array);
			//System.out.println(array.toString());
			
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

	/*
	public static void parseXMLFile(String strFilePath) {

		File fXmlFile = new File(strFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		String metricClass="";
		String metricName="";
		String metricNode="";
		String metricValue="";
		long metricTime=0;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		
			
			NodeList monList = doc.getElementsByTagName("monitor");
			Element countElement;
			Node countNode;

			for (int i = 0; i < monList.getLength(); i++) {

				Node monNode = monList.item(i);

				if (monNode.getNodeType() == Node.ELEMENT_NODE) {

					Element monElement = (Element) monNode;
					
					metricNode=monElement.getAttribute("target");
	
					
					
					if (monElement.getAttribute("name").contains("Database Performance Counter ")) {
						metricClass="Database-MSSQL";
						
						countNode=monElement.getElementsByTagName("counter").item(1);
						countElement=(Element)countNode;
						metricName="transactionCountPerSec";
						metricTime=Long.parseLong(monElement.getAttribute("time"))/1000;
						metricValue=countElement.getAttribute("value");
						appendLogFile(metricDomain,metricClass,metricName,metricNode,metricValue,metricTime);
						
					}else if (monElement.getAttribute("name").contains("IIS Performance Counter")) {
						metricClass="WEB-IIS";
						
						countNode=monElement.getElementsByTagName("counter").item(1);
						countElement=(Element)countNode;
						metricName="userCountPerSec";
						metricTime=Long.parseLong(monElement.getAttribute("time"))/1000;
						metricValue=countElement.getAttribute("value");
						appendLogFile(metricDomain,metricClass,metricName,metricNode,metricValue,metricTime);
						
						countNode=monElement.getElementsByTagName("counter").item(2);
						countElement=(Element)countNode;
						metricName="conAttempsPerSec";
						metricTime=Long.parseLong(monElement.getAttribute("time"))/1000;
						metricValue=countElement.getAttribute("value");
						appendLogFile(metricDomain,metricClass,metricName,metricNode,metricValue,metricTime);
						
						countNode=monElement.getElementsByTagName("counter").item(3);
						countElement=(Element)countNode;
						metricName="curUserCount";
						metricTime=Long.parseLong(monElement.getAttribute("time"))/1000;
						metricValue=countElement.getAttribute("value");
						appendLogFile(metricDomain,metricClass,metricName,metricNode,metricValue,metricTime);
						
						countNode=monElement.getElementsByTagName("counter").item(4);
						countElement=(Element)countNode;
						metricName="curConCount";
						metricTime=Long.parseLong(monElement.getAttribute("time"))/1000;
						metricValue=countElement.getAttribute("value");
						appendLogFile(metricDomain,metricClass,metricName,metricNode,metricValue,metricTime);
						
					}
					
					
				}
			}


			
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


	private static void appendLogFile(String metricDomain, String metricClass, String metricName,String metricNode, String metricValue, long metricTime) {
		
		String st=metricDomain+"|"+metricClass+"|"+metricName+"|"+metricNode+"|"+metricValue+"|"+metricTime+"\n";
		
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("logs/sitescopeMetrics.log", true), "UTF-8"));
			writer.append(st);
			writer.close();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
*/

	public static void disableCertificateValidation() {
		// See http://wiki.apache.org/ws/FrontPage/Axis/AxisClientConfiguration/Ssl
		AxisProperties.setProperty("axis.socketSecureFactory","org.apache.axis.components.net.SunFakeTrustSocketFactory");
	}
}
