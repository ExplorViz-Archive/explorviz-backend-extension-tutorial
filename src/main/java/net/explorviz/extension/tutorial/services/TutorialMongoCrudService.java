package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.security.model.roles.Role;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on tutorial objects, backed by a MongoDB instance as persistence layer. Each
 * tutorial has the following fields:
 * <ul>
 * <li>id: the unique id of the tutorial</li>
 * <li>tutorialname: name of the tutorial, unique</li>
 * <li>password: hashed password</li>
 * <li>roles: list of role that are assigned to the tutorial</li>
 * </ul>
 *
 */
@Service
public class TutorialMongoCrudService implements MongoCrudService<Tutorial> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TutorialMongoCrudService.class);



  private final Datastore datastore;


  @Inject
  private IdGenerator idGenerator;

  /**
   * Creates a new TutorialMongoDB
   *
   * @param datastore - the datastore instance
   */
  @Inject
  public TutorialMongoCrudService(final Datastore datastore) {

    this.datastore = datastore;
  }

  @Override
  public List<Tutorial> getAll() {
    return this.datastore.createQuery(Tutorial.class).asList();
  }

  @Override
  /**
   * Persists an tutorial entity
   *
   * @param tutorial - a tutorial entity
   * @return an Optional, which contains a Tutorial or is empty
   */
  public Optional<Tutorial> saveNewEntity(final Tutorial tutorial) {
    // Generate an id
    //tutorial.setId(this.idGenerator.generateId());

    this.datastore.save(tutorial);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Inserted new tutorial with id " + tutorial.getId());
    }
    return Optional.ofNullable(tutorial);
  }

  @Override
  public void updateEntity(final Tutorial tutorial) {
    this.datastore.save(tutorial);
  }

  @Override
  public Optional<Tutorial> getEntityById(final String id) {

    final Tutorial tutorialObject = this.datastore.get(Tutorial.class, id);

    return Optional.ofNullable(tutorialObject);
  }

  @Override
  public void deleteEntityById(final String id){
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
