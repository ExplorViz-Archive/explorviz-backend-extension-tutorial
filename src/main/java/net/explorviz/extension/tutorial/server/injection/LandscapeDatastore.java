package net.explorviz.extension.tutorial.server.injection;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.explorviz.extension.tutorial.model.TutorialLandscape;

public class LandscapeDatastore {
	
	  private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeDatastore.class.getSimpleName());
	
	  @Inject
	  private  MongoConnection client; // NOPMD

	  
	  
	  public static final String FIELD_LANDSCAPE = "landscape";
	  public static final String FIELD_ID = "_id";
	  public static final String FIELD_REQUESTS = "totalRequests";
	  
	  private static final String REPLAY_COLLECTION = "replay";
	  
	  private static final String DBNAME = "explorviz";

	  /**
	   * Returns the {@link MongoDatabase} given in the configuration file.
	   *
	   */
	  public MongoDatabase getDatabase() {
	    return this.getClient().getDatabase(DBNAME);
	  }

	
	  /**
	   * Returns a connection to the landscape collection.
	   */

	
	  /**
	   * Returns a {@link MongoClient} which is connected to the database given in the configuration
	   * file. This method is private in order to prevent unintentional closing of all connections.
	   *
	   * @see #close()
	   */
	  private MongoConnection getClient() {
	    return this.client;
	  }

	  public  MongoCollection<Document> getLandscapeCollection() {
			return this.getDatabase().getCollection("landscape");
		 }
	 public List<TutorialLandscape> getLandscapeList() {
		 MongoCollection<Document> docs = this.getDatabase().getCollection("landscape");
		 LinkedList<TutorialLandscape> list= new LinkedList<>();
		 for(Document doc: docs.find()) {
			 list.add(new TutorialLandscape(doc.getString(FIELD_ID),doc.getString(FIELD_LANDSCAPE)));
		 } 
		return list;
	 }
	 
	  
}
