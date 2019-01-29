package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.extension.tutorial.util.CountingIdGenerator;
import net.explorviz.extension.tutorial.util.IdGenerator;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on user objects, backed by a MongoDB instance as
 * persistence layer. Each user has the following fields:
 * <ul>
 * <li>id: the unique id of the tutorial</li>
 * </ul>
 *
 */
@Service
public class TutorialMongoCrudService implements MongoCrudService<Tutorial> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TutorialMongoCrudService.class);

	private final IdGenerator<Long> idGen;

	private final Datastore datastore;

	/**
	 * Creates a new {@code UserCrudMongoDb}.
	 *
	 */
	@Inject
	public TutorialMongoCrudService(final Datastore datastore) {

		this.datastore = datastore;

		final Tutorial tutorialWithMaxId = this.datastore.createQuery(Tutorial.class).order("-id").get();

		// Create a new id generator, which will count upwards beginning from the max id
		long counterInitValue = 0L;

		if (tutorialWithMaxId != null) {
			counterInitValue = tutorialWithMaxId.getId();
		}

		this.idGen = new CountingIdGenerator(counterInitValue);
	}

	@Override
	public List<Tutorial> getAll() {
		return this.datastore.createQuery(Tutorial.class).asList();
	}

	@Override
	public Optional<Tutorial> saveNewEntity(final Tutorial tutorial) throws MongoException {
		// Generate an id
		tutorial.setId(this.idGen.next());

		this.datastore.save(tutorial);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inserted new tutorial with id " + tutorial.getId());
		}
		return Optional.ofNullable(tutorial);
	}

	@Override
	public void updateEntity(final Tutorial tutorial) throws MongoException {
		this.datastore.save(tutorial);
	}

	@Override
	public Optional<Tutorial> getEntityById(final Long id) throws MongoException {

		final Tutorial tutorialObject = this.datastore.get(Tutorial.class, id);

		return Optional.ofNullable(tutorialObject);
	}

	@Override
	public void deleteEntityById(final Long id) throws MongoException {

		this.datastore.delete(Tutorial.class, id);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Deleted tutorial with id " + id);
		}

	}

	@Override
	public Optional<Tutorial> findEntityByFieldValue(final String field, final Object value) {

		final Tutorial foundTutorial = this.datastore.createQuery(Tutorial.class).filter(field, value).get();

		return Optional.ofNullable(foundTutorial);
	}

}
