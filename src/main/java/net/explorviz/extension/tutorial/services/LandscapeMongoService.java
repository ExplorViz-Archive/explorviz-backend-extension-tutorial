package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import net.explorviz.extension.tutorial.server.injection.LandscapeDatastore;
import net.explorviz.extension.tutorial.server.resources.LandscapeResource;
import net.explorviz.extension.tutorial.util.LandscapeSerializationHelper;
import net.explorviz.shared.landscape.model.landscape.Landscape;

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
public class LandscapeMongoService implements MongoCrudService<Landscape> {
	
	@Inject
    private LandscapeSerializationHelper serializationHelper;// NOPMD
    
    @Inject
    private LandscapeDatastore landscapeDatastore; // NOPMD
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeMongoService.class.getSimpleName());

    
    @Override
	public Optional<Landscape> saveNewEntity(Landscape landscape) {
		 String landscapeJsonApi;
		    try {
		      landscapeJsonApi = this.serializationHelper.serialize(landscape);
		    } catch (final DocumentSerializationException e) {
		      throw new InternalServerErrorException("Error serializing: " + e.getMessage(), e);
		    }

		    final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();

		    final Document landscapeDocument = new Document();
		    landscapeDocument.append(LandscapeDatastore.FIELD_ID, landscape.getTimestamp());
		    landscapeDocument.append(LandscapeDatastore.FIELD_LANDSCAPE, landscapeJsonApi);

		    try {
		      landscapeCollection.insertOne(landscapeDocument);
		    } catch (final Exception e) {
		      if (LOGGER.isErrorEnabled()) {
		        LOGGER.error("No document saved.");
		        return Optional.ofNullable(landscape);
		      }
		    }
		    if (LOGGER.isInfoEnabled()) {
		      LOGGER.info(String.format("Saved landscape {timestamp: %d, id: %s}",
		    		  landscape.getTimestamp(), landscape.getId()));
		    }
	        return Optional.ofNullable(landscape);
	}

	@Override
	public void updateEntity(Landscape landscape) {
			
	}

	@Override
	public Optional<Landscape> getEntityById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Landscape> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntityById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Landscape> findEntityByFieldValue(String field, Object value) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();

		final Document landscapeDocument = new Document();
		landscapeDocument.append(field, value);

		    final FindIterable<Document> result = landscapeCollection.find(landscapeDocument);

		    if (result.first() != null) {
		    	try {
		      return Optional.ofNullable(serializationHelper.deserialize(result.first().getString(LandscapeDatastore.FIELD_LANDSCAPE)));
		    	}catch (DocumentSerializationException e) {
				      throw new ClientErrorException("An error occured loading the landscape for provided "+ field+" " + value, 404);
				}
		    } else {
		      throw new ClientErrorException("Landscape not found for provided "+ field+" " + value, 404);
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
	
	public Optional<Landscape> findEntityByTimestamp(String value) {
		return this.findEntityByFieldValue("_id",value);
	}

	public void saveNewEntity(String timestamp,String jsonlandscape) {
	    final Document landscapeDocument = new Document();
	    landscapeDocument.append("_id", timestamp);
	    landscapeDocument.append("landscape",jsonlandscape );
	    this.landscapeDatastore.getLandscapeCollection().insertOne(landscapeDocument);
	}

}
