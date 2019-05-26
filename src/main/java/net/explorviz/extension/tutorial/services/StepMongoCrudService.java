package net.explorviz.extension.tutorial.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.shared.common.idgen.IdGenerator;
import xyz.morphia.Datastore;

/**
 * Offers CRUD operations on step objects, backed by a MongoDB instance as persistence layer. Each
 * step has the following fields:
 * <ul>
 * <li>id: the unique id of the step</li>
 * <li>stepname: name of the step, unique</li>
 * <li>password: hashed password</li>
 * <li>roles: list of role that are assigned to the step</li>
 * </ul>
 *
 */
@Service
public class StepMongoCrudService implements MongoCrudService<Step> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StepMongoCrudService.class);



  private final Datastore datastore;


  @Inject
  private IdGenerator idGenerator;

  /**
   * Creates a new StepMongoDB
   *
   * @param datastore - the datastore instance
   */
  @Inject
  public StepMongoCrudService(final Datastore datastore) {

    this.datastore = datastore;
  }

  @Override
  public List<Step> getAll() {
    return this.datastore.createQuery(Step.class).asList();
  }

  @Override
  /**
   * Persists an step entity
   *
   * @param step - a step entity
   * @return an Optional, which contains a Step or is empty
   */
  public Optional<Step> saveNewEntity(final Step step) {
    // Generate an id
    step.setId(this.idGenerator.generateId());

    this.datastore.save(step);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Inserted new step with id " + step.getId());
    }
    return Optional.ofNullable(step);
  }

  @Override
  public void updateEntity(final Step step) {
    this.datastore.save(step);
  }

  @Override
  public Optional<Step> getEntityById(final String id) {

    final Step foundStep = this.datastore.get(Step.class, id);
//    Sequence foundSequence = this.datastore.createQuery(Sequence.class).filter("steps", foundStep).get();
//    
//    Tutorial foundTutorial = this.datastore.createQuery(Tutorial.class).filter("sequences", foundSequence).get();
// 
//    foundSequence.setTutorial(foundTutorial);
//    foundStep.setSequence(foundSequence);
    return Optional.ofNullable(foundStep);
  }

  @Override
  public void deleteEntityById(final String id){
    this.datastore.delete(Step.class, id);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Deleted step with id " + id);
    }
  }

  
  @Override
  public Optional<Step> findEntityByFieldValue(final String field, final Object value) {

    final Step foundStep = this.datastore.createQuery(Step.class).filter(field, value).get();
//    Sequence foundSequence = this.datastore.createQuery(Sequence.class).filter("steps", foundStep).get();
//   
//    Tutorial foundTutorial = this.datastore.createQuery(Tutorial.class).filter("sequences", foundSequence).get();
// 
//    foundSequence.setTutorial(foundTutorial);
//    foundStep.setSequence(foundSequence);
    return Optional.ofNullable(foundStep);
  }



}
