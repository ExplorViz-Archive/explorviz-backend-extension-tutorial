package net.explorviz.extension.tutorial.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import net.explorviz.extension.tutorial.server.injection.LandscapeDatastore;

/**
 * Stores and retrieves landscapes from a mongodb, which is given in the
 * {@code explorviz.properties} resource.
 *
 * <p>
 *
 * This repository will return all requested landscapes as actual java objects. Since landscapes are
 * stored in json api format, this requires costy deserialization. If you don't need the actual
 * object but rather just their strin representation, use {@link MongoLandscapeJsonApiRepository}
 *
 * </p>
 *
 * <p>
 *
 * This class is just a decorator for {@link MongoLandscapeJsonApiRepository} which deserializes the
 * retrieved objects.
 *
 * </p>
 */
public class LandscapeMongoService implements MongoCrudService<String> {
	  
    @Inject
    private LandscapeDatastore landscapeDatastore; // NOPMD
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeMongoService.class.getSimpleName());

    
    public static String getTimestampFromLandscape(String landscapejson) {
    	Pattern p = Pattern.compile("\\\\\\\"timestamp\\\\\\\"\\:(\\d+)");
    	Matcher m = p.matcher(landscapejson);
        if (m.find()) {
        	return m.group();
        }else {
        	return null;
        }
    }
    
    public static Matcher getDataAndIncludedFromLandscape(String landscapejson) {
    	Pattern p = Pattern.compile("^\\{\\\"data\\\"\\:(.*(?=\\,\\\"included\\\"\\:))\\,\\\"included\\\"\\:\\[(.*(?=\\]\\}$))");
    	Matcher m = p.matcher(landscapejson);
        return m;
    }
    
    @Override
	public Optional<String> saveNewEntity(String landscape) {
		    final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();

		    final Document landscapeDocument = new Document();
		    landscapeDocument.append(LandscapeDatastore.FIELD_ID, getTimestampFromLandscape(landscape));
		    landscapeDocument.append(LandscapeDatastore.FIELD_LANDSCAPE, landscape);

		    try {
		      landscapeCollection.insertOne(landscapeDocument);
		    } catch (final Exception e) {
		      if (LOGGER.isErrorEnabled()) {
		        LOGGER.error("No document saved.");
		        return Optional.ofNullable(landscape);
		      }
		    }
		    if (LOGGER.isInfoEnabled()) {
		      LOGGER.info(String.format("Saved landscape {timestamp: %d}",
		    		  getTimestampFromLandscape(landscape)));
		    }
	        return Optional.ofNullable(landscape);
	}

	@Override
	public void updateEntity(String landscape) {
			
	}

	@Override
	public Optional<String> getEntityById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAll() {
		FindIterable<Document> col = this.landscapeDatastore.getLandscapeCollection().find();
		List<String> list = new LinkedList<String>();
		for(Document d: col) {
			list.add(d.getString(LandscapeDatastore.FIELD_LANDSCAPE));
		}
		return list;
	}

	@Override
	public void deleteEntityById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<String> findEntityByFieldValue(String field, Object value) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();

		final Document landscapeDocument = new Document();
		landscapeDocument.append(field, value);

		    final FindIterable<Document> result = landscapeCollection.find(landscapeDocument);

		if (result.first() != null) {
		    return Optional.ofNullable(result.first().getString(LandscapeDatastore.FIELD_LANDSCAPE));
		} else {
	    	return Optional.empty();
		}		
	}

	public boolean entityExistsByFieldValue(String field, Object value) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();
		final Document landscapeDocument = new Document();
		landscapeDocument.append(field, value);
		final FindIterable<Document> result = landscapeCollection.find(landscapeDocument);
		return (result.first() != null);
	}

	public boolean entityExistsByTimestamp(String value) {
		return this.entityExistsByFieldValue("_id",value);
	}
	
	public Optional<String> findEntityByTimestamp(String value) {
		return this.findEntityByFieldValue("_id",value);
	}

	public void saveNewEntity(String timestamp,String jsonlandscape) {
	    final Document landscapeDocument = new Document();
	    landscapeDocument.append("_id", timestamp);
	    landscapeDocument.append("landscape",jsonlandscape );
	    this.landscapeDatastore.getLandscapeCollection().insertOne(landscapeDocument);
	}

}
