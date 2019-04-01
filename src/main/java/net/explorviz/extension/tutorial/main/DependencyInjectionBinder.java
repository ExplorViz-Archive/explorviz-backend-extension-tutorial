package net.explorviz.extension.tutorial.main;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.github.jasminb.jsonapi.ResourceConverter;

import net.explorviz.extension.tutorial.server.injection.DatastoreFactory;
import net.explorviz.extension.tutorial.server.injection.LandscapeDatastore;
import net.explorviz.extension.tutorial.server.injection.MongoConnection;
import net.explorviz.extension.tutorial.server.providers.ResourceConverterFactory;
import net.explorviz.extension.tutorial.services.LandscapeMongoService;
import net.explorviz.extension.tutorial.services.SequenceMongoCrudService;
import net.explorviz.extension.tutorial.services.StepMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialMongoCrudService;
import net.explorviz.shared.config.annotations.ConfigValues;
import net.explorviz.shared.config.annotations.injection.ConfigInjectionResolver;
import net.explorviz.shared.config.annotations.injection.ConfigValuesInjectionResolver;
import net.explorviz.shared.exceptions.ErrorObjectHelper;
import xyz.morphia.Datastore;

/**
 * The DependencyInjectionBinder is used to register Contexts and Dependency
 * Injection (CDI) aspects for this application.
 */
public class DependencyInjectionBinder extends AbstractBinder {

	@Override
	public void configure() {

		// Injectable config properties

		
		this.bind(new ConfigInjectionResolver()).to(new TypeLiteral<InjectionResolver<ConfigValues>>() {
		});
		this.bind(new ConfigValuesInjectionResolver()).to(new TypeLiteral<InjectionResolver<ConfigValues>>() {
		});

		this.bindFactory(ResourceConverterFactory.class).to(ResourceConverter.class).in(Singleton.class);

		this.bind(MongoConnection.class).to(MongoConnection.class).in(Singleton.class);
		this.bind(LandscapeDatastore.class).to(LandscapeDatastore.class).in(Singleton.class);

		this.bindFactory(DatastoreFactory.class).to(Datastore.class).in(Singleton.class);

		
		this.bind(TutorialMongoCrudService.class).to(TutorialMongoCrudService.class).in(Singleton.class);
		this.bind(SequenceMongoCrudService.class).to(SequenceMongoCrudService.class).in(Singleton.class);
		this.bind(StepMongoCrudService.class).to(StepMongoCrudService.class).in(Singleton.class);
		this.bind(LandscapeMongoService.class).to(LandscapeMongoService.class).in(Singleton.class);

		// ErrorObject Handler
		this.bind(ErrorObjectHelper.class).to(ErrorObjectHelper.class).in(Singleton.class);

	}
}
