package com.abdullahteke.controller;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient
{
	
	private static RestClient managerInstance;

	
	public static RestClient getManagerInstance() {
		
		if (managerInstance==null){
			managerInstance=new RestClient();
		}
		return managerInstance;
	}



	private static void getJSONDataFromRest(JSONObject obj) {
		Client client= Client.create();
		WebResource webResource=client.resource("");
		
		ClientResponse response=webResource.type("application/json").post(ClientResponse.class,obj.toString());
		
		String output=response.getEntity(String.class);
		
	}
	
	private static void sendJSONDataToRest(JSONObject obj) {

		
	}
	


	public JSONObject createJsonObject(String metricDomain, String metricClass, String metricNode,Map<String, String> counterMap) {
		
		Iterator<String> iter= counterMap.keySet().iterator();
		String counterName;
		String value;
		
		JSONObject obj= new JSONObject();
		obj.put("metricDomain", metricDomain);
		obj.put("metricClass", metricClass);
		obj.put("metricNode", metricNode);

	
		JSONObject tmp=new JSONObject();
		while (iter.hasNext()) {

			counterName=iter.next();
			value=counterMap.get(counterName);
			tmp.put(counterName, value);
			
			
		}
		obj.put("metricsName", tmp);
		//System.out.println(obj.toString());
		
		return obj;
	}



	public void sendDataToBVD(JSONArray array) {
		
		System.out.println(array.toString());

		Client client= Client.create();
		WebResource webResource=client.resource("http://bvd01:12224/api/submit/a90142d0918911e983a8bda65cf2fd67/dims/metricDomain,metricClass,metricNode/tags/SiteScope,HBYS-STATS");
		//System.out.println(array.toString());
		
		ClientResponse response=webResource.type("application/json").post(ClientResponse.class,array.toString());
		
		String output=response.getEntity(String.class);
		//System.out.println(output);
		
		
		
	}


}