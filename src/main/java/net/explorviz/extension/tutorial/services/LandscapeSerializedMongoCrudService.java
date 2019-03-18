package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.LandscapeSerialized;
import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.util.CountingIdGenerator;
import net.explorviz.extension.tutorial.util.IdGenerator;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on user objects, backed by a MongoDB instance as
 * persistence layer. Each user has the following fields:
 * <ul>
 * <li>id: the unique id of the step</li>
 * </ul>
 *
 */
@Service
public class LandscapeSerializedMongoCrudService implements MongoCrudService<LandscapeSerialized> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeSerializedMongoCrudService.class);

	private final IdGenerator<Long> idGen;

	private final Datastore datastore;

	/**
	 * Creates a new {@code UserCrudMongoDb}.
	 *
	 */
	@Inject
	public LandscapeSerializedMongoCrudService(final Datastore datastore) {

		this.datastore = datastore;

		final LandscapeSerialized landscapeSerializedWithMaxId = this.datastore.createQuery(LandscapeSerialized.class).order("-id").get();

		// Create a new id generator, which will count upwards beginning from the max id
		long counterInitValue = 0L;

		if (landscapeSerializedWithMaxId != null) {
			counterInitValue = landscapeSerializedWithMaxId.getTimestamp();
		}

		this.idGen = new CountingIdGenerator(counterInitValue);
	}

	@Override
	public List<LandscapeSerialized> getAll() {
		return this.datastore.createQuery(LandscapeSerialized.class).asList();
	}

	@Override
	public Optional<LandscapeSerialized> saveNewEntity(final LandscapeSerialized landscapeSerialized) throws MongoException {
		// Generate an id

		this.datastore.save(landscapeSerialized);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inserted new step with id " + landscapeSerialized.getTimestamp());
		}
		return Optional.ofNullable(landscapeSerialized);
	}

	@Override
	public void updateEntity(final LandscapeSerialized landscapeSerialized) throws MongoException {
		this.datastore.save(landscapeSerialized);
	}

	@Override
	public Optional<LandscapeSerialized> getEntityById(final Long id) throws MongoException {

		final LandscapeSerialized landscapeSerializedObject = this.datastore.get(LandscapeSerialized.class, id);

		return Optional.ofNullable(landscapeSerializedObject);
	}

	@Override
	public void deleteEntityById(final Long id) throws MongoException {

		this.datastore.delete(Step.class, id);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Deleted step with id " + id);
		}

	}

	@Override
	public Optional<LandscapeSerialized> findEntityByFieldValue(final String field, final Object value) {

		final LandscapeSerialized foundLandscapeSerialized = this.datastore.createQuery(LandscapeSerialized.class).filter(field, value).get();

		return Optional.ofNullable(foundLandscapeSerialized);
	}

}
