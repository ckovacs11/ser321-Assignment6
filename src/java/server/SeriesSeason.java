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
* Purpose: A SeriesSeason class for holding information about a seriesSeason
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/





import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.util.ArrayList;

public class SeriesSeason {

  private String series_name;
  private String season;
  private String imdbRating;
  private String genre;
  private String poster;
  private String summary;
  private String name_and_season;

  private ArrayList<Episode> episodes;
  private int episode_count;

  

  public SeriesSeason(String aName, String aSeason, String aRating,
                            String aGenre, String aPoster, String aSummary){

        this.series_name = aName;
        this.season = aSeason;
        this.imdbRating = aRating;
        this.genre = aGenre;
        this.poster = aPoster;
        this.summary = aSummary;
        this.name_and_season = this.series_name + " - Season " + this.season;
  }


  public SeriesSeason(String jsonString){
     this(new JSONObject(jsonString));
     
  }

  //constructor to create a SeriesSeason object from the library json file
  public SeriesSeason(JSONObject jsonObj){
     try{
        
        //create a new episodes arraylist
        this.episodes = new ArrayList<Episode>();
        this.name_and_season = jsonObj.getString("seriesSeason");
        this.poster = jsonObj.getString("Poster");
        this.genre = jsonObj.getString("Genre");
        this.summary = jsonObj.getString("Plot");
        JSONArray epArr = jsonObj.optJSONArray("Episodes");
        this.imdbRating = jsonObj.getString("imdbRating");
        addEpisodes(epArr);


        
     }catch(Exception ex){
        System.out.println("Exception in SeriesSeason(JSONObject): "+ex.getMessage());
     }
  }

  //constructor to create a SeriesSeason object from the omdb api calls
  public SeriesSeason(JSONObject series, String s){
      try{
          
          


          //pull series info from the series json file
          this.series_name = series.getString("Title");
          this.genre = series.getString("Genre");
          this.imdbRating = series.getString("imdbRating");
          this.poster = series.getString("Poster");
          this.summary = series.getString("Plot");
          

          //set season number
          this.season = s;
          //create new arraylist for episodes
          this.episodes = new ArrayList<Episode>();
          

        //create the name_and_season string
        this.name_and_season = this.series_name + " - Season " + this.season;
        System.out.println("Created the seriesSeason: " + this.name_and_season + ".");
        }catch(Exception ex){
            System.out.println("Exception in SeriesSeason(JSONObject series, JSONObject epi): "+ex.getMessage());
        }
  }

  public void addEpisodes(JSONArray arr){

    //iterate through episode JSONArray and create and add new Episodes
    for(int i = 0; i < arr.length(); i++){
      Episode newEpisode = new Episode(arr.optJSONObject(i));
      
      this.episodes.add(newEpisode);
    }




  }

  public void addEpisode(JSONObject epi){
    Episode new_episode = new Episode(epi);

    
    this.episodes.add(new_episode);


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

  //converts the SeriesSeason object to json
  public JSONObject toJson(){




    JSONObject ssJson = new JSONObject();
    JSONArray epArr = new JSONArray();

    ssJson.put("seriesSeason", this.name_and_season);  
    ssJson.put("Poster", this.poster);
    ssJson.put("Plot", this.summary);
    ssJson.put("Genre", this.genre);
    ssJson.put("imdbRating", this.imdbRating);

    for(int i = 0; i < this.episodes.size(); i++){
      Episode temp = this.episodes.get(i);
      
      epArr.put(temp.toJson());
    }
    
    ssJson.put("Episodes", epArr);

    
    return ssJson;
      
  }

  
  public String getSeriesName(){
      return this.name_and_season;
  }

  public int getEpisodeCount(){
      return this.episode_count;
  }
  public ArrayList getEpisodes(){
      return this.episodes;
  }

  public Episode getEpisode(String epi){
      Episode e = null;
      for(int i = 0; i < this.episodes.size(); i++){
          if(episodes.get(i).getEpisodeName().equals(epi)){
              e = (Episode)this.episodes.get(i);
          }
      }
      return e;
  }

  public String getGenre(){
      return this.genre;
  }

  public String getSummary(){
      return this.summary;
  }

  public String getRating(){
      return this.imdbRating;
  }

  public String getPoster(){
      return this.poster;
  }

}
