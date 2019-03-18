package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.util.CountingIdGenerator;
import net.explorviz.extension.tutorial.util.IdGenerator;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on user objects, backed by a MongoDB instance as
 * persistence layer. Each user has the following fields:
 * <ul>
 * <li>id: the unique id of the sequence</li>
 * </ul>
 *
 */
@Service
public class SequenceMongoCrudService implements MongoCrudService<Sequence> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceMongoCrudService.class);

	private final IdGenerator<Long> idGen;

	private final Datastore datastore;

	/**
	 * Creates a new {@code UserCrudMongoDb}.
	 *
	 */
	@Inject
	public SequenceMongoCrudService(final Datastore datastore) {

		this.datastore = datastore;

		final Sequence sequenceWithMaxId = this.datastore.createQuery(Sequence.class).order("-id").get();

		// Create a new id generator, which will count upwards beginning from the max id
		long counterInitValue = 0L;

		if (sequenceWithMaxId != null) {
			counterInitValue = sequenceWithMaxId.getId();
		}

		this.idGen = new CountingIdGenerator(counterInitValue);
	}

	@Override
	public List<Sequence> getAll() {
		return this.datastore.createQuery(Sequence.class).asList();
	}

	@Override
	public Optional<Sequence> saveNewEntity(final Sequence sequence) throws MongoException {
		// Generate an id
		sequence.setId(this.idGen.next());

		this.datastore.save(sequence);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inserted new sequence with id " + sequence.getId());
		}
		return Optional.ofNullable(sequence);
	}

	@Override
	public void updateEntity(final Sequence sequence) throws MongoException {
		this.datastore.save(sequence);

	}

	@Override
	public Optional<Sequence> getEntityById(final Long id) throws MongoException {

		final Sequence sequenceObject = this.datastore.get(Sequence.class, id);

		return Optional.ofNullable(sequenceObject);
	}

	@Override
	public void deleteEntityById(final Long id) throws MongoException {

		this.datastore.delete(Sequence.class, id);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Deleted sequence with id " + id);
		}

	}

	@Override
	public Optional<Sequence> findEntityByFieldValue(final String field, final Object value) {

		final Sequence foundSequence = this.datastore.createQuery(Sequence.class).filter(field, value).get();

		return Optional.ofNullable(foundSequence);
	}

}
