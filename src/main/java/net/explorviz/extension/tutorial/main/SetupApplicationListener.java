package net.explorviz.extension.tutorial.main;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.ClientErrorException;

import org.bson.Document;
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
import net.explorviz.extension.tutorial.server.injection.LandscapeDatastore;
import net.explorviz.extension.tutorial.services.LandscapeMongoService;
import net.explorviz.extension.tutorial.util.PasswordStorage.CannotPerformOperationException;
import net.explorviz.shared.landscape.model.landscape.Landscape;
import xyz.morphia.Datastore;

/**
 * Primary starting class - executed, when the servlet context is started.
 */
@WebListener
public class SetupApplicationListener implements ApplicationEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetupApplicationListener.class);

	@Inject
	private Datastore datastore;
	
	
	@Inject
	private LandscapeMongoService landscapeMongoService;
	

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
		step.setId(1L);
		step.setTitle("Step1");
		this.datastore.save(step);
		final Step step2 = new Step();
		step2.setId(2L);
		step2.setTitle("Step2");
		this.datastore.save(step);
		
		final Sequence seq = new Sequence();
		seq.setId(1L);
		seq.setTitle("Sequence1");
		seq.addStep(step);
		seq.addStep(step2);
		this.datastore.save(seq);

		if(!this.landscapeMongoService.entityExistsByTimestamp("1553961723688")){
			String jsonlandscape= "{\"data\":{\"type\":\"landscape\",\"id\":\"landscape-5-1\",\"attributes\":{\"extensionAttributes\":{}},\"relationships\":{\"timestamp\":{\"data\":{\"type\":\"timestamp\",\"id\":\"landscape-5-967\"}},\"systems\":{\"data\":[]},\"events\":{\"data\":[]},\"totalApplicationCommunications\":{\"data\":[]}}},\"included\":[{\"type\":\"timestamp\",\"id\":\"landscape-5-967\",\"attributes\":{\"extensionAttributes\":{},\"timestamp\":1553961723688,\"totalRequests\":0}}]}";
			this.landscapeMongoService.saveNewEntity("1553961723688", jsonlandscape);
		}
		
		final Tutorial tutorial = new Tutorial();
		tutorial.setLandscapeTimestamp("1553961723688");
		tutorial.setId(1L);
		tutorial.setTitle("First");
		tutorial.setText("Testtext");
		tutorial.addSequence(seq);
		LOGGER.info("Created default tutorial: " + tutorial.getTitle());
		LOGGER.info("ID: " + tutorial.getId());

		this.datastore.save(tutorial);

	}

}
