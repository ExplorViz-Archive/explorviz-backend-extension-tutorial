package net.explorviz.extension.tutorial.services;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.TutorialTimestamp;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.security.model.roles.Role;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on tutorialTimestamp objects, backed by a MongoDB instance as persistence layer. Each
 * tutorialTimestamp has the following fields:
 * <ul>
 * <li>id: the unique id of the tutorialTimestamp</li>
 * <li>tutorialTimestampname: name of the tutorialTimestamp, unique</li>
 * <li>password: hashed password</li>
 * <li>roles: list of role that are assigned to the tutorialTimestamp</li>
 * </ul>
 *
 */
@Service
public class TutorialTimestampMongoCrudService implements MongoCrudService<TutorialTimestamp> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TutorialTimestampMongoCrudService.class);



  private final Datastore datastore;


  @Inject
  private IdGenerator idGenerator;

  /**
   * Creates a new TutorialTimestampMongoDB
   *
   * @param datastore - the datastore instance
   */
  @Inject
  public TutorialTimestampMongoCrudService(final Datastore datastore) {

    this.datastore = datastore;
  }

  @Override
  public List<TutorialTimestamp> getAll() {
    return this.datastore.createQuery(TutorialTimestamp.class).asList();
  }

  @Override
  /**
   * Persists an tutorialTimestamp entity
   *
   * @param tutorialTimestamp - a tutorialTimestamp entity
   * @return an Optional, which contains a TutorialTimestamp or is empty
   */
  public Optional<TutorialTimestamp> saveNewEntity(final TutorialTimestamp tutorialTimestamp) {
    // Generate an id
    tutorialTimestamp.setId(this.idGenerator.generateId());

    this.datastore.save(tutorialTimestamp);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Inserted new tutorialTimestamp with id " + tutorialTimestamp.getId());
    }
    return Optional.ofNullable(tutorialTimestamp);
  }

  @Override
  public void updateEntity(final TutorialTimestamp tutorialTimestamp) {
    this.datastore.save(tutorialTimestamp);
  }

  @Override
  public Optional<TutorialTimestamp> getEntityById(final String id) {

    final TutorialTimestamp tutorialTimestampObject = this.datastore.get(TutorialTimestamp.class, id);

    return Optional.ofNullable(tutorialTimestampObject);
  }

  @Override
  public void deleteEntityById(final String id){
    this.datastore.delete(TutorialTimestamp.class, id);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Deleted tutorialTimestamp with id " + id);
    }
  }

  
  @Override
  public Optional<TutorialTimestamp> findEntityByFieldValue(final String field, final Object value) {

    final TutorialTimestamp foundTutorialTimestamp = this.datastore.createQuery(TutorialTimestamp.class).filter(field, value).get();

    return Optional.ofNullable(foundTutorialTimestamp);
  }



}
