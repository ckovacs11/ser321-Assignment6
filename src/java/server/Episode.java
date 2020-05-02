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
* Purpose: An Episode class for holding information about a seriesSeason episode
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/



import org.json.JSONArray;
import org.json.JSONObject;

public class Episode {
  private String name;
  private String imdbRating;


  public Episode(String aName, String aRating){
      this.name = aName;
      this.imdbRating = aRating;
  }

  public Episode(JSONObject jsonObj){
     try{
        
        this.name = jsonObj.getString("Title");
        this.imdbRating = jsonObj.getString("imdbRating");
        

     }catch(Exception ex){
        System.out.println("Exception in Episode(JSONObject): "+ex.getMessage());
     }
  }

  public String toJsonString(){
     String ret = "{}";
     try{
        ret = this.toJson().toString(0);
     }catch(Exception ex){
        System.out.println("Exception in toJsonString: "+ex.getMessage());
     }
     return ret;
  }

  public JSONObject toJson(){
     JSONObject obj = new JSONObject();
     try{
        obj.put("Title", this.name);

        obj.put("imdbRating", this.imdbRating);

     }catch(Exception ex){
        System.out.println("Exception in toJson: "+ex.getMessage());
     }
     return obj;
  }

  public String getEpisodeName(){
      return name;
  }

  public String getRating(){
      return imdbRating;
  }



}
