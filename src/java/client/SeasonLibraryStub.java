package ser321.assign6.ckovacs1.java.client;

/*
* Copyright 2020 Curtis Kovacs,
*
* This software is the intellectual property of the author, and can not be
distributed, used, copied, or
* reproduced, in whole or in part, for any purpose, commercial or otherwise.
The author grants the ASU
* Software Engineering program the right to copy, execute, and evaluate this
work for the purpose of
* determining performance of the author in coursework, and for Software
Engineering program evaluation,
* so long as this copyright and right-to-use statement is kept in-tact in such
use.
* All other uses are prohibited and reserved to the author.
*
* Purpose: An class that acts as a controller in a MVC system
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/

import ser321.assign6.ckovacs1.java.server.*;
import java.net.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;



public class SeasonLibraryStub extends Object implements SeriesLibrary {


	private static final int buffSize = 4096;
   	private static int id = 0;
   	private String host;
   	private int port;
   	private Socket sock;
   	private OutputStream os;
   	private InputStream is;
   	private static final boolean debugOn = false;

   	public SeasonLibraryStub (String host, int port) {
      this.host = host;
      this.port = port;
      try{
      
      
  } catch(Exception ex){
  	System.out.println("exception Stub constructor: "+ex.getMessage());
  }
      
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   //sends the json rpc info to the skeleton
   public String callMethod(String method, Object[] params) {
      JSONObject theCall = new JSONObject();
      String ret = "{}";
      try{
         debug("Request is: "+theCall.toString());
         theCall.put("method",method);
         theCall.put("id",id);
         theCall.put("jsonrpc","2.0");
         ArrayList<Object> al = new ArrayList();
         for (int i=0; i<params.length; i++){
            al.add(params[i]);
         }
         JSONArray paramsJson = new JSONArray(al);
         theCall.put("params",paramsJson);
         sock = new Socket(host,port);
         os = sock.getOutputStream();
         is = sock.getInputStream();
         int numBytesReceived;
         int bufLen = 2048;
         String strToSend = theCall.toString();
         byte bytesReceived[] = new byte[buffSize];
         byte bytesToSend[] = strToSend.getBytes();
         os.write(bytesToSend,0,bytesToSend.length);
         numBytesReceived = is.read(bytesReceived,0,bufLen);
         ret = new String(bytesReceived,0,numBytesReceived);
         debug("callMethod received from server: "+ret);
         
      }catch(Exception ex){
         System.out.println("exception in callMethod: "+ex.getMessage());
      }
      return ret;
   }
   //close the socket and streams
   public void closeSocket(){
   	try{
   		os.close();
   		is.close();
   		sock.close();
   	} catch(Exception ex){
   		System.out.println("exception in closeSocket: " + ex.getMessage());

   	}
   	
   }

   public String[] getSeriesSeasonList() {

   	  String[] ret = new String[]{};
      String result = callMethod("getSeriesSeasonList", new Object[0]);
      
      JSONObject res = new JSONObject(result);
      JSONArray namesJson = res.optJSONArray("result");
      ret = new String[namesJson.length()];
      for (int i=0; i<namesJson.length(); i++){
         ret[i] = namesJson.optString(i);
      }
      return ret;
   }

   public SeriesSeason getSeriesSeasonObj(String title) {

   	  
      String jsonResult = callMethod("getSeriesSeasonObj", new Object[]{title});
      
      JSONObject jsonObjResult = new JSONObject(jsonResult);
      if(jsonObjResult.optString("result").equals("null")){
      	return null;
      } else {
      	JSONObject seriesJson = jsonObjResult.optJSONObject("result");

      	SeriesSeason ret = new SeriesSeason(seriesJson);
      	return ret;

      }
      
   }

   public boolean addSeriesSeason(SeriesSeason ss){

   	  boolean ret = false;
      String result = callMethod("addSeriesSeason", new Object[]{ss.toJson()});
      JSONObject res = new JSONObject(result);
      ret = res.optBoolean("result",false);
      return ret;
   }

   public boolean removeSeriesSeason(SeriesSeason ss){


   	boolean ret = false;
   	String result = callMethod("removeSeriesSeason", new Object[]{ss.toJson()});
   	JSONObject res = new JSONObject(result);
   	ret = res.optBoolean("result", false);
   	return ret;
   }

   public boolean saveLibraryToFile(){

   	boolean ret = false;
   	String result = callMethod("saveLibraryToFile", new Object[]{});
   	JSONObject res = new JSONObject(result);
   	ret = res.optBoolean("result", false);
   	if(ret == true){
   		System.out.println("Library saved successfully");

   	}
   	
   	return ret;



   }

   public boolean restoreLibraryFromFile(){

   	boolean ret = false;
   	String result = callMethod("restoreLibraryFromFile", new Object[]{});
   	JSONObject res = new JSONObject(result);
   	ret = res.optBoolean("result", false);
   	if(ret == true){
   		System.out.println("Library restored successfully");

   	}
   	
   	return ret;



   }







}