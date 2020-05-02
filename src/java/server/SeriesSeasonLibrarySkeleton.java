package ser321.assign6.ckovacs1.java.server;

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

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;
import ser321.assign6.ckovacs1.java.server.*;


public class SeriesSeasonLibrarySkeleton extends Object {

	private static final boolean debugOn = false;

    private SeriesLibrary sl;

   public SeriesSeasonLibrarySkeleton (SeriesLibrary sl){
      this.sl = sl;
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   public String callMethod(String request){
      JSONObject result = new JSONObject();

      //get variables from the call
      try{
         JSONObject theCall = new JSONObject(request);
         debug("Request is: "+ theCall.toString());
         String method = theCall.getString("method");
         int id = theCall.getInt("id");
         JSONArray params = null;
         if(!theCall.isNull("params")){
            params = theCall.getJSONArray("params");
         }

         //add id and json rpc 2.0 to result
         result.put("id",id);
         result.put("jsonrpc","2.0");

         //begin methods
         if(method.equals("getSeriesSeasonList")){
            String[] names = sl.getSeriesSeasonList();
            JSONArray resArr = new JSONArray();
            for (int i=0; i<names.length; i++){
               resArr.put(names[i]);
            }
            debug("getSeriesSeasonList request found: "+ resArr.toString());
            result.put("result",resArr);
         }else if(method.equals("getSeriesSeasonObj")){
            String seriesName = params.getString(0);
            
            SeriesSeason ss = sl.getSeriesSeasonObj(seriesName);
            if(ss == null){

            	System.out.println("Result of getSeriesSeasonObj is null");
            	result.put("result", "null");
            }
            else{

            	JSONObject ssJson = ss.toJson();
            	debug("getSeriesSeasonObj request found: "+ ssJson.toString());
            	result.put("result",ssJson);
        	}
            
         }else if(method.equals("addSeriesSeason")){
            JSONObject ssJson = params.getJSONObject(0);
            SeriesSeason ssToAdd = new SeriesSeason(ssJson);

            debug("adding SeriesSeason: "+ ssToAdd.toJsonString());
            sl.addSeriesSeason(ssToAdd);
            result.put("result",true);
         }else if(method.equals("removeSeriesSeason")){
            JSONObject ssJson = params.getJSONObject(0);
            SeriesSeason ssToRemove = new SeriesSeason(ssJson);
            debug("removing SeriesSeason named "+ ssToRemove.toJsonString());
            sl.removeSeriesSeason(ssToRemove);
            result.put("result",true);
         }else if(method.equals("saveLibraryToFile")){
         	System.out.println("Attempting to save library");
            
            debug("saving SeriesLibrary");
            sl.saveLibraryToFile();
            result.put("result",true);
         
            
         }else if(method.equals("restoreLibraryFromFile")){
         	System.out.println("Restoring library from file.");
         	sl.restoreLibraryFromFile();
         	result.put("result", true);
         }else{
            debug("Unable to match method: "+method+". Returning 0.");
            result.put("result",0.0);
         }
      }catch(Exception ex){
         System.out.println("exception in callMethod: "+ex.getMessage());
      }
      return result.toString();
   }
}