package net.explorviz.extension.tutorial.main;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEvent.Type;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.extension.tutorial.model.TutorialTimestamp;
import net.explorviz.extension.tutorial.services.TutorialLandscapeMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialTimestampMongoCrudService;
import xyz.morphia.Datastore;

/**
 * Primary starting class - executed, when the servlet context is started.
 */
@WebListener
public class SetupApplicationListener implements ApplicationEventListener {

	  @SuppressWarnings("serial")
	  public static class CannotPerformOperationException extends Exception {
	    public CannotPerformOperationException(final String message) {
	      super(message);
	    }

	    public CannotPerformOperationException(final String message, final Throwable source) {
	      super(message, source);
	    }
	  }
	  
	private static final Logger LOGGER = LoggerFactory.getLogger(SetupApplicationListener.class);

	@Inject
	private Datastore datastore;
	
	
	@Inject
	private TutorialLandscapeMongoCrudService landscapeMongoService;
	
	@Inject
	private TutorialTimestampMongoCrudService timestampMongoService;

	@Override
	public void onEvent(final ApplicationEvent event) {

		// After this type, CDI (e.g. injected LandscapeExchangeService) has been
		// fullfilled
		final Type t = Type.INITIALIZATION_FINISHED;

		if (event.getType().equals(t)) {
			try {
				this.createDefaultData();
			} catch (final CannotPerformOperationException e) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("Unable to create default tutorial: " + e.getMessage());
					LOGGER.warn("Is the database AND reverse-proxy running/ other docker container using the port?");
				}
			}
		}

	}

	@Override
	public RequestEventListener onRequest(final RequestEvent requestEvent) {
		return null;
	}

	private void createDefaultData() throws CannotPerformOperationException {


		final Step step = new Step();
		step.setId("step-1");
		step.setTitle("Step1");
		this.datastore.save(step);
		final Step step2 = new Step();
		step2.setId("step-2");
		step2.setTitle("Step2");
		this.datastore.save(step2);
		
		final Sequence seq = new Sequence();
		seq.setId("sequence-1");
		seq.setTitle("Sequence1");
		seq.addStep(step);
		seq.addStep(step2);
		this.datastore.save(seq);
		if(!this.landscapeMongoService.getEntityById("landscape-5-1").isPresent()){
//			String jsonlandscape= "{\"data\":{\"type\":\"tutoriallandscape\",\"id\":\"landscape-5-1\",\"attributes\":{\"extensionAttributes\":{}},\"relationships\":{\"timestamp\":{\"data\":{\"type\":\"tutorialtimestamp\",\"id\":\"landscape-5-967\"}},\"systems\":{\"data\":[]},\"events\":{\"data\":[]},\"totalApplicationCommunications\":{\"data\":[]}}},\"included\":[{\"type\":\"tutorialtimestamp\",\"id\":\"landscape-5-967\",\"attributes\":{\"extensionAttributes\":{},\"timestamp\":1553961723688,\"totalRequests\":7}}]}";
//			TutorialTimestamp tutstamp=new TutorialTimestamp("landscape-5-967","1553961723688","Empty Landscape");
//			this.timestampMongoService.saveNewEntity(tutstamp);
//			TutorialLandscape tutlandscape = new TutorialLandscape("landscape-5-1",tutstamp,jsonlandscape);
//			this.landscapeMongoService.saveNewEntity(tutlandscape);
		}

		final Tutorial tutorial = new Tutorial();
		seq.setLandscapeTimestamp("");
		tutorial.setId("tutorial-1");
		tutorial.setTitle("First");
		tutorial.addSequence(seq);
		LOGGER.info("Created default tutorial: " + tutorial.getTitle());
		LOGGER.info("ID: " + tutorial.getId());

		this.datastore.save(tutorial);

	}
}


