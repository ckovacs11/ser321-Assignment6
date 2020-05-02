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


//client side
import ser321.assign6.ckovacs1.java.server.*;
import ser321.assign2.lindquis.MediaLibraryGui;
import ser321.assign6.ckovacs1.java.client.SeasonLibraryStub;
import java.rmi.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import javax.sound.sampled.*;
import java.beans.*;
import java.net.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.Runtime;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLConnection;
import java.time.Duration;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;


public class MediaLibraryApp extends MediaLibraryGui implements
TreeWillExpandListener,
ActionListener,
TreeSelectionListener {

	private static final boolean debugOn = false;
	private static boolean inLibrary;
    private static final String pre = "https://www.omdbapi.com/?apikey=";
	private static String urlOMBD;
	private String url;
	private static SeasonLibraryStub stub;
	private String omdbKey;
	private SeriesSeason tempSS;
	private SeriesSeason searchViewSS;

	public MediaLibraryApp(String author, String authorKey) {

        // sets the value of 'author' on the title window of the GUI.
        
		super(author);
		


		this.omdbKey = authorKey;
		urlOMBD = pre + authorKey + "&t=";
		this.inLibrary = false;

		//not necessary due RPC
		//library = new SeriesLibraryImpl();

		// register this object as an action listener for menu item clicks. This will cause
		// my actionPerformed method to be called every time the user selects a menuitem.
		
		for(int i=0; i<userMenuItems.length; i++){
			for(int j=0; j<userMenuItems[i].length; j++){
				userMenuItems[i][j].addActionListener(this);
			}
		}
		
		// register this object as an action listener for the Search button. This will cause
		// my actionPerformed method to be called every time the user clicks the Search button
		searchJButt.addActionListener(this);
		try{
			//tree.addTreeWillExpandListener(this);  // add if you want to get called with expansion/contract
			tree.addTreeSelectionListener(this);
			rebuildTree();
		}catch (Exception ex){
			JOptionPane.showMessageDialog(this,"Handling "+
					" constructor exception: " + ex.getMessage());
		}
		try{
			/*
			 * display an image just to show how the album or artist image can be displayed in the
			 * app's window. setAlbumImage is implemented by MediaLibraryGui class. Call it with a
			 * string url to a png file as obtained from an album search.
			 */

			
		}catch(Exception ex){
			System.out.println("unable to open image");
		}
		setVisible(true);
	}

	/**
	 * A method to facilitate printing debugging messages during development, but which can be
	 * turned off as desired.
     * @param message Is the message that should be printed.
     * @return void
	 */
	private void debug(String message) {
		if (debugOn)
			System.out.println("debug: "+message);
	}

	/**
	 * Create and initialize nodes in the JTree of the left pane.
	 * buildInitialTree is called by MediaLibraryGui to initialize the JTree.
	 * Classes that extend MediaLibraryGui should override this method to
	 * perform initialization actions specific to the extended class.
	 * The default functionality is to set base as the label of root.
	 * In your solution, you will probably want to initialize by deserializing
	 * your library and displaying the categories and subcategories in the
	 * tree.
	 * @param root Is the root node of the tree to be initialized.
	 * @param base Is the string that is the root node of the tree.
	 */
	public void buildInitialTree(DefaultMutableTreeNode root, String base){
		//set up the context and base name
		try{
			root.setUserObject(base);
		}catch (Exception ex){
			JOptionPane.showMessageDialog(this,"exception initial tree:"+ex);
			ex.printStackTrace();
		}
	}

	/**
	 * TODO
	 * method to build the JTree of media shown in the left panel of the UI. The
	 * field tree is a JTree as defined and initialized by MediaLibraryGui class.
	 * It is defined to be protected so it can be accessed by extending classes.
	 * This version of the method uses the music library to get the names of
	 * tracks. Your solutions will need to replace this structure with one that
	 * you need for the series/season and Episode. These two classes should store your information.
	 * Your library (so a changes - or newly implemented MediaLibraryImpl) will store
	 * and provide access to Series/Seasons and Episodes.
	 * This method is provided to demonstrate one way to add nodes to a JTree based
	 * on an underlying storage structure.
	 * See also the methods clearTree, valueChanged defined in this class, and
	 * getSubLabelled which is defined in the GUI/view class.
	 **/
	 public void searchTree(SeriesSeason ss){
		tree.removeTreeSelectionListener(this);
 		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
 		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
 		clearTree(root, model);
 		// put nodes in the tree for all registered with the library

 			ArrayList epArr = ss.getEpisodes();

 			for(int j = 0; j < epArr.size(); j++){
 				debug("Adding episode to tree with title:"+ ((Episode)epArr.get(j)).getEpisodeName());
 				DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(((Episode)epArr.get(j)).getEpisodeName());
 				DefaultMutableTreeNode childNode = getSubLabelled(root, ss.getSeriesName());

 				if(childNode!=null){ // if the seriesSeason node exists
 					debug("seriesSeason exists: "+((Episode)epArr.get(j)).getEpisodeName());
 					//insert episode unde the seriesSeason parent
 					model.insertNodeInto(toAdd, childNode,
 							model.getChildCount(childNode));

 				}else{ // seriesSeason node does not exist
 					DefaultMutableTreeNode aSeriesNode =
 							new DefaultMutableTreeNode(ss.getSeriesName());
 					debug("no seriesSeason, so add one with name: "+ss.getSeriesName());
 					//insert series node directly under the root
 					model.insertNodeInto(aSeriesNode, root,
 							model.getChildCount(root));
 					//DefaultMutableTreeNode aSubCatNode =
 							//new DefaultMutableTreeNode("aSubCat");
 					//debug("adding subcat labelled: "+"aSubCat");
 					model.insertNodeInto(toAdd, aSeriesNode,
 							model.getChildCount(aSeriesNode));
 				}

 			}//end episode loop
 		// expand all the nodes in the JTree
 		for(int r =0; r < tree.getRowCount(); r++){
 			tree.expandRow(r);
 		}
 		tree.addTreeSelectionListener(this);
	 }


	public void rebuildTree() {

		tree.removeTreeSelectionListener(this);
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		clearTree(root, model);

		// put nodes in the tree for all registered with the library
		String[] titleList = stub.getSeriesSeasonList();
		

		for (int i = 0; i < titleList.length; i++){
			SeriesSeason ss = stub.getSeriesSeasonObj(titleList[i]);
			ArrayList<Episode> epArr = ss.getEpisodes();

			for(int j = 0; j < epArr.size(); j++){
				debug("Adding episode with title:"+ ((Episode)epArr.get(j)).getEpisodeName());
				DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(((Episode)epArr.get(j)).getEpisodeName());
				DefaultMutableTreeNode childNode = getSubLabelled(root, ss.getSeriesName());

				if(childNode!=null){ // if the seriesSeason node exists
					debug("seriesSeason exists: "+((Episode)epArr.get(j)).getEpisodeName());
					//insert episode unde the seriesSeason parent
					model.insertNodeInto(toAdd, childNode,
							model.getChildCount(childNode));

				}else{ // seriesSeason node does not exist
					DefaultMutableTreeNode aSeriesNode =
							new DefaultMutableTreeNode(ss.getSeriesName());
					debug("no seriesSeason, so add one with name: "+ss.getSeriesName());
					//insert series node directly under the root
					model.insertNodeInto(aSeriesNode, root,
							model.getChildCount(root));
					//DefaultMutableTreeNode aSubCatNode =
							//new DefaultMutableTreeNode("aSubCat");
					//debug("adding subcat labelled: "+"aSubCat");
					model.insertNodeInto(toAdd, aSeriesNode,
							model.getChildCount(aSeriesNode));
				}

			}//end episode loop

		}//end series loop


		// expand all the nodes in the JTree
		for(int r =0; r < tree.getRowCount(); r++){
			tree.expandRow(r);
		}
		tree.addTreeSelectionListener(this);
	}
    /**
     * Remove all nodes in the left pane tree view.
     *
     * @param root Is the root node of the tree.
     * @param model Is a model that uses TreeNodes.
     * @return void
     */
	private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model){
		try{
			DefaultMutableTreeNode next = null;
			int subs = model.getChildCount(root);
			for(int k=subs-1; k>=0; k--){
				next = (DefaultMutableTreeNode)model.getChild(root,k);
				debug("removing node labelled:"+(String)next.getUserObject());
				model.removeNodeFromParent(next);
			}
		}catch (Exception ex) {
			System.out.println("Exception while trying to clear tree:");
			ex.printStackTrace();
		}
	}

	public void treeWillCollapse(TreeExpansionEvent tee) {
		debug("In treeWillCollapse with path: "+tee.getPath());
		tree.setSelectionPath(tee.getPath());
	}

	public void treeWillExpand(TreeExpansionEvent tee) {
		debug("In treeWillExpand with path: "+tee.getPath());
	}

	// TODO:
	// this will be called when you click on a node.
	// It will update the node based on the information stored in the library
	// this will need to change since your library will be of course totally different
	// extremely simplified! E.g. make sure that you display sensible content when the root,
	// the My Series, the Series/Season, and Episode nodes are selected
	public void valueChanged(TreeSelectionEvent e) {
		
			tree.removeTreeSelectionListener(this);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
					tree.getLastSelectedPathComponent();

			if(node!=null){
				String nodeLabel = (String)node.getUserObject();

				debug("In valueChanged. Selected node labelled: "+ nodeLabel);
				// is this a terminal node?

				// All fields empty to start with
				seriesSeasonJTF.setText("");
				genreJTF.setText("");
				ratingJTF.setText("");
				episodeJTF.setText("");
				summaryJTA.setText("");


				DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot(); // get the root
                // First (and only) child of the root (username) node is 'My Series' node.
				DefaultMutableTreeNode mySeries = (DefaultMutableTreeNode)root.getChildAt(0); // mySeries node
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
				String parentLabel = (String)parent.getUserObject();
				


				//check if the selected node is in the library
				if(node.getChildCount() == 0 && (node != (DefaultMutableTreeNode)tree.getModel().getRoot())){
					if(stub.getSeriesSeasonObj(parentLabel) != null){
						inLibrary = true;
					}
				}else{
					if(stub.getSeriesSeasonObj(nodeLabel) != null){
						inLibrary = true;
					}
				}

				//if the node is in the library
				if(inLibrary == true){

					//if node is an episode and not in the library, set tempSS to the SeriesSeason of the episode
				if(node.getChildCount()==0 && 
						(node != (DefaultMutableTreeNode)tree.getModel().getRoot())){

				    tempSS = stub.getSeriesSeasonObj(parentLabel);
					episodeJTF.setText(nodeLabel); // name of the episode

					ratingJTF.setText(((Episode)stub.getSeriesSeasonObj(parentLabel).getEpisode(nodeLabel)).getRating()); // change to rating of the episode

					genreJTF.setText(stub.getSeriesSeasonObj(parentLabel).getGenre()); // change to genre of the series from library
					summaryJTA.setText(stub.getSeriesSeasonObj(parentLabel).getSummary()); // change to Plot of library for season
					seriesSeasonJTF.setText(parentLabel); // Change to season name
					setAlbumImage(stub.getSeriesSeasonObj(parentLabel).getPoster());
						}
				//if node is a SeriesSeason, set tempSS to the SeriesSeason		
				else{
					tempSS = stub.getSeriesSeasonObj(nodeLabel);
					SeriesSeason thisSeriesSeason = stub.getSeriesSeasonObj(nodeLabel);
					seriesSeasonJTF.setText(thisSeriesSeason.getSeriesName()); // season name
					genreJTF.setText(thisSeriesSeason.getGenre()); // genre of the series from library
					ratingJTF.setText(thisSeriesSeason.getRating()); // rating of the season get from library
					episodeJTF.setText(""); // nothing in here since not an episode
					summaryJTA.setText(thisSeriesSeason.getSummary());
					setAlbumImage(thisSeriesSeason.getPoster());
				}

				


				//node is not in library, so it is a searched episode or SeriesSeason
			} else{
				//if the node is a searched episode
				if(node.getChildCount()==0 && 
						(node != (DefaultMutableTreeNode)tree.getModel().getRoot())){

				    //get episode from episode array
				    Episode clickedEpisode = tempSS.getEpisode(nodeLabel);
					
					episodeJTF.setText(nodeLabel); // name of the episode

					ratingJTF.setText(clickedEpisode.getRating()); // change to rating of the episode

					genreJTF.setText(tempSS.getGenre()); // change to genre of the series from library
					summaryJTA.setText(tempSS.getSummary()); // change to Plot of library for season
					seriesSeasonJTF.setText(tempSS.getSeriesName()); // Change to season name
					setAlbumImage(tempSS.getPoster());
				} else {
					seriesSeasonJTF.setText(tempSS.getSeriesName());
					genreJTF.setText(tempSS.getGenre());
					ratingJTF.setText(tempSS.getRating());
					summaryJTA.setText(tempSS.getSummary());
				}


			}
		}
		
		tree.addTreeSelectionListener(this);
		inLibrary = false;
	}

	
	public void actionPerformed(ActionEvent e){

		tree.removeTreeSelectionListener(this);
		if(e.getActionCommand().equals("Exit")) {
			stub.closeSocket();
			System.exit(0);
		}else if(e.getActionCommand().equals("Save")) {
			
			stub.saveLibraryToFile();
		

		}else if(e.getActionCommand().equals("Restore")) {
			stub.restoreLibraryFromFile();
			rebuildTree();

		}else if(e.getActionCommand().equals("Series-SeasonAdd")) {

			ArrayList<Episode> arr = tempSS.getEpisodes();
						
			if(stub.addSeriesSeason(tempSS) == true){
			   System.out.println("Series added to library");
		   }
			   else{
				   System.out.println("Series not added");
			   }

			rebuildTree();
		
		}else if(e.getActionCommand().equals("Search")) {
			// TODO: implement that the search result is used to create new series/season object
            /*
             * In the below API(s) the error response should be appropriately handled
             */

			// with all episodes only display this new series/season with the episodes in tree

			// Doing a fetch two times so that we only get the full series info (with poster, summary, rating) once
			// fetch series info
			String searchReqURL = urlOMBD+seriesSearchJTF.getText().replace(" ", "%20");
			System.out.println("calling fetch with url: "+searchReqURL);
			String json = fetchURL(searchReqURL);
			

			// fetch season info
			String searchReqURL2 = urlOMBD+seriesSearchJTF.getText().replace(" ", "%20")+"&season="+seasonSearchJTF.getText();
			String jsonEpisodes = fetchURL(searchReqURL2);
			

			/* TODO: implement here that this json will be used to create a Season object with the episodes included
			 * This should also then build the tree and display the info in the left side bar (so the new tree with its episodes)
			 * right hand should display the Series information
			 */

			 //create JSONObjects from the json strings and feed them to the SeriesSeason constructor
			 String seasonNumber = seasonSearchJTF.getText();
			 JSONObject series = new JSONObject(json);
			 
			 JSONObject epi = new JSONObject(jsonEpisodes);
			 JSONArray arr = epi.optJSONArray("Episodes");
			 SeriesSeason newSeries = new SeriesSeason(series, seasonNumber);
			 newSeries.addEpisodes(arr);

			 //set the temporary SeriesSeason
			 tempSS = newSeries;

			searchTree(tempSS);


		}else if(e.getActionCommand().equals("Tree Refresh")) {
			
			rebuildTree();
		
		}else if(e.getActionCommand().equals("Series-SeasonRemove")) {
	
			if(stub.removeSeriesSeason(tempSS)){
				System.out.println("Series removed successfully.");
			}
			else{
				System.out.println("Series was not removed.");
			}
			rebuildTree();
		}

		tree.addTreeSelectionListener(this);
	}

	/**
	 *
	 * A method to do asynchronous url request printing the result to System.out
	 * @param aUrl the String indicating the query url for the OMDb api search
	 *
	 **/
	public void fetchAsyncURL(String aUrl){
		try{
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(aUrl))
					.timeout(Duration.ofMinutes(1))
					.build();
			client.sendAsync(request, BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept(System.out::println)
			.join();
		}catch(Exception ex){
			System.out.println("Exception in fetchAsyncUrl request: "+ex.getMessage());
		}
	}

	/**
	 *
	 * a method to make a web request. Note that this method will block execution
	 * for up to 20 seconds while the request is being satisfied. Better to use a
	 * non-blocking request.
	 * @param aUrl the String indicating the query url for the OMDb api search
	 * @return the String result of the http request.
	 *
	 **/
	public String fetchURL(String aUrl) {
		StringBuilder sb = new StringBuilder();
		URLConnection conn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(aUrl);
			conn = url.openConnection();
			if (conn != null)
				conn.setReadTimeout(20 * 1000); // timeout in 20 seconds
			if (conn != null && conn.getInputStream() != null) {
				in = new InputStreamReader(conn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader br = new BufferedReader(in);
				if (br != null) {
					int ch;
					// read the next character until end of reader
					while ((ch = br.read()) != -1) {
						sb.append((char)ch);
					}
					br.close();
				}
			}
			in.close();
		} catch (Exception ex) {
			System.out.println("Exception in url request:"+ ex.getMessage());
		}
		return sb.toString();
	}

	public static void main(String args[]) {

		//obtain host and port from command line args or use localhost by default
		//obtain the name and omdb key from the command line args
		String host="localhost";
		String port="8080";
		String userId = "userID not set in params";
		String omdbKey = "ombd-key not set in params";
		if (args.length >= 4){
		   host = args[0];
		   port = args[1];
		   userId = args[2];
		   omdbKey = args[3];
		}
		

		String url = "http://" + host + ":" + port + "/";
         System.out.println("Opening connection to: " + url);

         stub = (SeasonLibraryStub) new SeasonLibraryStub(host, Integer.parseInt(port));
         

		//call MediaLibraryApp constructor
		try{
			System.out.println("calling constructor name "+ userId);
			MediaLibraryApp mla = new MediaLibraryApp(userId,omdbKey);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
