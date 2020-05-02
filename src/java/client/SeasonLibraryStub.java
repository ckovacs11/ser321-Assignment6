package ser321.assign6.ckovacs1.java.client;

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
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }


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