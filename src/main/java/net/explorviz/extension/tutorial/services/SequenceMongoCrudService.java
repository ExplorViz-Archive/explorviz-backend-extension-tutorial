package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.management.Query;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.security.model.roles.Role;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on sequence objects, backed by a MongoDB instance as persistence layer. Each
 * sequence has the following fields:
 * <ul>
 * <li>id: the unique id of the sequence</li>
 * <li>sequencename: name of the sequence, unique</li>
 * <li>password: hashed password</li>
 * <li>roles: list of role that are assigned to the sequence</li>
 * </ul>
 *
 */
@Service
public class SequenceMongoCrudService implements MongoCrudService<Sequence> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SequenceMongoCrudService.class);



  private final Datastore datastore;


  @Inject
  private IdGenerator idGenerator;

  /**
   * Creates a new SequenceMongoDB
   *
   * @param datastore - the datastore instance
   */
  @Inject
  public SequenceMongoCrudService(final Datastore datastore) {

    this.datastore = datastore;
  }

  @Override
  public List<Sequence> getAll() {
    return this.datastore.createQuery(Sequence.class).asList();
  }

  @Override
  /**
   * Persists an sequence entity
   *
   * @param sequence - a sequence entity
   * @return an Optional, which contains a Sequence or is empty
   */
  public Optional<Sequence> saveNewEntity(final Sequence sequence) {
    // Generate an id
    sequence.setId(this.idGenerator.generateId());

    this.datastore.save(sequence);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Inserted new sequence with id " + sequence.getId());
    }
    return Optional.ofNullable(sequence);
  }

  @Override
  public void updateEntity(final Sequence sequence) {
    this.datastore.save(sequence);
  }

  @Override
  public Optional<Sequence> getEntityById(final String id) {
    final Sequence sequenceObject = this.datastore.get(Sequence.class, id);
    return Optional.ofNullable(sequenceObject);
  }

  @Override
  public void deleteEntityById(final String id){
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
