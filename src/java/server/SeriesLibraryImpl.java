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
* Purpose: An library class for holding information and methods regarding seriesSeasons
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/


import java.net.MalformedURLException;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.util.ArrayList;


public class SeriesLibraryImpl implements SeriesLibrary {

    private Hashtable<String,SeriesSeason> aLib;
    private static final String fileName ="series.json";

    public SeriesLibraryImpl () {
       this.aLib = new Hashtable<String,SeriesSeason>();
      try{
          //new input stream to read in the json file in the library
          InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
          if(is==null){
             is = new FileInputStream(new File(this.fileName));
          }
          JSONArray libArray = new JSONArray(new JSONTokener(is));
          for(int i = 0; i < libArray.length(); i++){
            JSONObject newSeries = libArray.optJSONObject(i);
            String seriesSeasonName = newSeries.getString("seriesSeason");
            

            if(aLib.containsKey(seriesSeasonName)){
                 aLib.get(seriesSeasonName).addEpisode(newSeries);
             } else if (newSeries != null){
                
                SeriesSeason ss = new SeriesSeason(newSeries);

                aLib.put(seriesSeasonName, ss);

             }

          }

      }catch (Exception ex){
          System.out.println("Exception reading "+fileName+": "+ex.getMessage());
       }
    }
    public synchronized String[] getSeriesSeasonList() {
       String[] result = null;
       try{
          Set<String> vec = aLib.keySet();
          result = vec.toArray(new String[]{});
      }catch(Exception ex){
          System.out.println("exception in getTitles: "+ex.getMessage());
       }
       return result;
    }

    public synchronized SeriesSeason getSeriesSeasonObj(String mediaTitle) {
       SeriesSeason result = null;
       try{
          result = aLib.get(mediaTitle);
          ArrayList<Episode> arr = result.getEpisodes();
          
      }catch(Exception ex){
          System.out.println("exception in getSeriesSeasonObj: "+ex.getMessage());
       }
       return result;
    }

    public synchronized boolean addSeriesSeason(SeriesSeason md) {
       boolean result = false;
       System.out.println("Adding: "+ md.getSeriesName());
       try{
          aLib.put(md.getSeriesName(),md);
          result = true;
      }catch(Exception ex){
          System.out.println("exception in add: "+ex.getMessage());
       }
       return result;
    }

    public synchronized boolean removeSeriesSeason(SeriesSeason ss) {
    String mediaTitle = ss.getSeriesName();
       boolean result = false;
       System.out.println("Removing "+mediaTitle);
       try{
          aLib.remove(mediaTitle);
          result = true;
      }catch(Exception ex){
          System.out.println("exception in remove: "+ex.getMessage());
       }
       return result;
    }

    public synchronized boolean saveLibraryToFile(){

      boolean ret = true;

        JSONArray master = new JSONArray();
        //iterate through the seriesSeason objects
        aLib.forEach((name, ss)-> {
          JSONObject seriesJsonObj = ss.toJson();
          master.put(seriesJsonObj);


          
          });


        
            //Write JSON file
            try{
                FileWriter file = new FileWriter(fileName);

                file.write(master.toString(1));
                file.flush();

            } catch (Exception e) {
              ret = false;
                e.printStackTrace();
            }

            return ret;

    }

    public synchronized boolean restoreLibraryFromFile(){
      boolean ret = true;

      this.aLib = new Hashtable<String,SeriesSeason>();
      try{
          //new input stream to read in the json file in the library
          InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
          if(is==null){
             is = new FileInputStream(new File(this.fileName));
          }
          JSONArray libArray = new JSONArray(new JSONTokener(is));
          for(int i = 0; i < libArray.length(); i++){
            JSONObject newSeries = libArray.optJSONObject(i);
            String seriesSeasonName = newSeries.getString("seriesSeason");
            

            if(aLib.containsKey(seriesSeasonName)){
                 
                 aLib.get(seriesSeasonName).addEpisode(newSeries);
             } else if (newSeries != null){
                
                SeriesSeason ss = new SeriesSeason(newSeries);

                aLib.put(seriesSeasonName, ss);

             }

          }

      }catch (Exception ex){
          System.out.println("Exception reading "+fileName+": "+ex.getMessage());
          ret = false;
       }

      return ret;

    }





}
