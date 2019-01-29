package net.explorviz.extension.tutorial.server.main;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEvent.Type;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.extension.tutorial.model.Interactable;
import net.explorviz.extension.tutorial.model.InteractablePlaceholder;
import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.extension.tutorial.util.PasswordStorage.CannotPerformOperationException;
import xyz.morphia.Datastore;

/**
 * Primary starting class - executed, when the servlet context is started.
 */
@WebListener
public class SetupApplicationListener implements ApplicationEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetupApplicationListener.class);

	private static final String ADMIN_NAME = "admin";

	@Inject
	private Datastore datastore;

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
				}
			}
		}

	}

	@Override
	public RequestEventListener onRequest(final RequestEvent requestEvent) {
		return null;
	}

	private void createDefaultData() throws CannotPerformOperationException {

		final Interactable i = new InteractablePlaceholder();

		final Step step = new Step();
		step.setId(1L);
		step.setTitle("Step1");
		step.setAction("CLICK");
		step.setTarget(i);
		this.datastore.save(step);

		final Sequence seq = new Sequence();
		seq.setId(1L);
		seq.setTitle("Sequence1");
		seq.setAction("CLICK");
		seq.setTarget(i);
		seq.addStep(step);
		this.datastore.save(seq);

		final Tutorial tutorial = new Tutorial();
		tutorial.setId(1L);
		tutorial.setTitle("First");
		tutorial.setText("Testtext");
		tutorial.addSequence(seq);
		LOGGER.info("Created default tutorial: " + tutorial.getTitle());
		this.datastore.save(tutorial);

	}

}
