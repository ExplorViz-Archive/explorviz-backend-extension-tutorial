package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.security.model.roles.Role;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on tutorialLandscape objects, backed by a MongoDB instance as persistence layer. Each
 * tutorialLandscape has the following fields:
 * <ul>
 * <li>id: the unique id of the tutorialLandscape</li>
 * <li>tutorialLandscapename: name of the tutorialLandscape, unique</li>
 * <li>password: hashed password</li>
 * <li>roles: list of role that are assigned to the tutorialLandscape</li>
 * </ul>
 *
 */
@Service
public class TutorialLandscapeMongoCrudService implements MongoCrudService<TutorialLandscape> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TutorialLandscapeMongoCrudService.class);



  private final Datastore datastore;


  @Inject
  private IdGenerator idGenerator;

  /**
   * Creates a new TutorialLandscapeMongoDB
   *
   * @param datastore - the datastore instance
   */
  @Inject
  public TutorialLandscapeMongoCrudService(final Datastore datastore) {

    this.datastore = datastore;
  }

  @Override
  public List<TutorialLandscape> getAll() {
    return this.datastore.createQuery(TutorialLandscape.class).asList();
  }

  @Override
  /**
   * Persists an tutorialLandscape entity
   *
   * @param tutorialLandscape - a tutorialLandscape entity
   * @return an Optional, which contains a TutorialLandscape or is empty
   */
  public Optional<TutorialLandscape> saveNewEntity(final TutorialLandscape tutorialLandscape) {
    // Generate an id
    tutorialLandscape.setId(this.idGenerator.generateId());

    this.datastore.save(tutorialLandscape);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Inserted new tutorialLandscape with id " + tutorialLandscape.getId());
    }
    return Optional.ofNullable(tutorialLandscape);
  }

  @Override
  public void updateEntity(final TutorialLandscape tutorialLandscape) {
    this.datastore.save(tutorialLandscape);
  }

  @Override
  public Optional<TutorialLandscape> getEntityById(final String id) {

    final TutorialLandscape tutorialLandscapeObject = this.datastore.get(TutorialLandscape.class, id);

    return Optional.ofNullable(tutorialLandscapeObject);
  }

  @Override
  public void deleteEntityById(final String id){
    this.datastore.delete(TutorialLandscape.class, id);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Deleted tutorialLandscape with id " + id);
    }
  }

  
  @Override
  public Optional<TutorialLandscape> findEntityByFieldValue(final String field, final Object value) {

    final TutorialLandscape foundTutorialLandscape = this.datastore.createQuery(TutorialLandscape.class).filter(field, value).get();

    return Optional.ofNullable(foundTutorialLandscape);
  }



}
