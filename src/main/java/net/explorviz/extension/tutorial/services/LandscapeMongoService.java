package net.explorviz.extension.tutorial.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.extension.tutorial.server.injection.LandscapeDatastore;

/**
 * Stores and retrieves landscapes from a mongodb, which is given in the
 * {@code explorviz.properties} resource.
 *
 * <p>
 *
 * This repository will return all requested landscapes as actual java objects.
 * Since landscapes are stored in json api format, this requires costy
 * deserialization. If you don't need the actual object but rather just their
 * strin representation, use {@link MongoLandscapeJsonApiRepository}
 *
 * </p>
 *
 * <p>
 *
 * This class is just a decorator for {@link MongoLandscapeJsonApiRepository}
 * which deserializes the retrieved objects.
 *
 * </p>
 */
public class LandscapeMongoService implements MongoCrudService<TutorialLandscape> {

	@Inject
	private LandscapeDatastore landscapeDatastore; // NOPMD

	private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeMongoService.class.getSimpleName());

	@Override
	public Optional<TutorialLandscape> saveNewEntity(TutorialLandscape landscape) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();
		Document landscapeDocument = new Document();
		landscapeDocument.append(LandscapeDatastore.FIELD_ID, landscape.getTimestamp());
		landscapeDocument.append(LandscapeDatastore.FIELD_LANDSCAPE, landscape.getLandscape());
		try {
			landscapeCollection.insertOne(landscapeDocument);
		} catch (final Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("No document saved.");
				return Optional.ofNullable(landscape);
			}
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Saved landscape {timestamp: %s}",
					landscapeDocument.getString(LandscapeDatastore.FIELD_ID)));
		}
		return Optional.ofNullable(landscape);
	}

	@Override
	public void updateEntity(TutorialLandscape landscape) {

	}

	@Override
	public Optional<TutorialLandscape> getEntityById(Long id) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();
		Document landscapeDocument = new Document();
		landscapeDocument.append(LandscapeDatastore.FIELD_ID, String.valueOf(id));
		return Optional.ofNullable(LandscapeMongoService.getLandscape(landscapeCollection.find(landscapeDocument).first()));
	}

	@Override
	public List<TutorialLandscape> getAll() {
		return this.landscapeDatastore.getLandscapeList();
	}

	@Override
	public void deleteEntityById(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public Optional<TutorialLandscape> findEntityByFieldValue(String field, Object value) {
		final MongoCollection<Document> landscapeCollection = this.landscapeDatastore.getLandscapeCollection();
		Document landscapeDocument = new Document();
		if(field.equals(LandscapeDatastore.FIELD_ID)) {
			landscapeDocument.append(LandscapeDatastore.FIELD_ID, value.toString());
		}else if(field.equals(LandscapeDatastore.FIELD_LANDSCAPE)) {
			landscapeDocument.append(LandscapeDatastore.FIELD_LANDSCAPE, value.toString());
		}else {
			throw new UnsupportedOperationException("Field should only be timestamp or landscape was: "+field+".");			
		}
		return Optional.ofNullable(LandscapeMongoService.getLandscape(landscapeCollection.find(landscapeDocument).first()));
	}

	public Optional<TutorialLandscape> findEntityByTimestamp(String value) {
		return this.findEntityByFieldValue(LandscapeDatastore.FIELD_ID, value);
	}
	
	public static TutorialLandscape getLandscape(Document doc){
		if(doc!=null) {	
			return new TutorialLandscape(doc.getString(LandscapeDatastore.FIELD_ID),doc.getString(LandscapeDatastore.FIELD_LANDSCAPE));
		}
		return null;
	}
}
