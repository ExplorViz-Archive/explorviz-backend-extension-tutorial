package net.explorviz.extension.tutorial.server.injection;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.model.Tutorial;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;

public class DatastoreFactory implements Factory<Datastore> {

	// @Config("mongo.port")
	// private String port;

	private final Datastore datastore;
	
	
	@Inject
	public DatastoreFactory(MongoConnection mongoclient) {
		final Morphia morphia = new Morphia();
		// Map the model classes
		morphia.map(Tutorial.class, Sequence.class, Step.class);
		this.datastore = morphia.createDatastore(mongoclient, "explorviz");
		this.datastore.ensureIndexes();
	}

	@Override
	public Datastore provide() {
		return this.datastore;
	}

	@Override
	public void dispose(final Datastore instance) {
		// nothing to do
	}
			
}
