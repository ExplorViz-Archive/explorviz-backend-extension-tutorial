package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

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
public class StepMongoCrudService implements MongoCrudService<Step> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StepMongoCrudService.class);

	private final IdGenerator<Long> idGen;

	private final Datastore datastore;

	/**
	 * Creates a new {@code UserCrudMongoDb}.
	 *
	 */
	@Inject
	public StepMongoCrudService(final Datastore datastore) {

		this.datastore = datastore;

		final Step stepWithMaxId = this.datastore.createQuery(Step.class).order("-id").get();

		// Create a new id generator, which will count upwards beginning from the max id
		long counterInitValue = 0L;

		if (stepWithMaxId != null) {
			counterInitValue = stepWithMaxId.getId();
		}

		this.idGen = new CountingIdGenerator(counterInitValue);
	}

	@Override
	public List<Step> getAll() {
		return this.datastore.createQuery(Step.class).asList();
	}

	@Override
	public Optional<Step> saveNewEntity(final Step step) throws MongoException {
		// Generate an id
		step.setId(this.idGen.next());

		this.datastore.save(step);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inserted new step with id " + step.getId());
		}
		return Optional.ofNullable(step);
	}

	@Override
	public void updateEntity(final Step step) throws MongoException {
		this.datastore.save(step);
	}

	@Override
	public Optional<Step> getEntityById(final Long id) throws MongoException {

		final Step stepObject = this.datastore.get(Step.class, id);

		return Optional.ofNullable(stepObject);
	}

	@Override
	public void deleteEntityById(final Long id) throws MongoException {

		this.datastore.delete(Step.class, id);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Deleted step with id " + id);
		}

	}

	@Override
	public Optional<Step> findEntityByFieldValue(final String field, final Object value) {

		final Step foundStep = this.datastore.createQuery(Step.class).filter(field, value).get();

		return Optional.ofNullable(foundStep);
	}

}
