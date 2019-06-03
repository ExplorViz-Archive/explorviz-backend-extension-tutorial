package net.explorviz.extension.tutorial.main;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.github.jasminb.jsonapi.ResourceConverter;

import net.explorviz.extension.tutorial.server.injection.DatastoreFactory;
import net.explorviz.extension.tutorial.server.injection.MongoConnection;
import net.explorviz.extension.tutorial.server.providers.ResourceConverterFactory;
import net.explorviz.extension.tutorial.services.SequenceMongoCrudService;
import net.explorviz.extension.tutorial.services.StepMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialLandscapeMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialTimestampMongoCrudService;
import net.explorviz.shared.common.idgen.AtomicEntityIdGenerator;
import net.explorviz.shared.common.idgen.EntityIdGenerator;
import net.explorviz.shared.common.idgen.IdGenerator;
import net.explorviz.shared.common.idgen.ServiceIdGenerator;
import net.explorviz.shared.common.idgen.UuidServiceIdGenerator;
import net.explorviz.shared.common.injection.CommonDependencyInjectionBinder;
import net.explorviz.shared.config.annotations.Config;
import net.explorviz.shared.config.annotations.ConfigValues;
import net.explorviz.shared.config.annotations.injection.ConfigInjectionResolver;
import net.explorviz.shared.config.annotations.injection.ConfigValuesInjectionResolver;
import net.explorviz.shared.exceptions.ErrorObjectHelper;
import net.explorviz.shared.exceptions.JsonApiErrorObjectHelper;
import net.explorviz.shared.security.TokenParserService;
import xyz.morphia.Datastore;

/**
 * The DependencyInjectionBinder is used to register Contexts and Dependency
 * Injection (CDI) aspects for this application.
 */
public class DependencyInjectionBinder extends CommonDependencyInjectionBinder {

	@Override
	public void configure() {
		
//
//	    // Injectable config properties
//	    this.bind(new ConfigInjectionResolver())
//	        .to(new TypeLiteral<InjectionResolver<ConfigValues>>() {});
//	    this.bind(new ConfigValuesInjectionResolver())
//	        .to(new TypeLiteral<InjectionResolver<ConfigValues>>() {});
//

//
//	    this.bind(TokenParserService.class).to(TokenParserService.class).in(Singleton.class);
//
//	    // injectable config properties
//	    this.bind(new ConfigInjectionResolver()).to(new TypeLiteral<InjectionResolver<Config>>() {});
//
//	    // ErrorObject Handler
//	    this.bind(JsonApiErrorObjectHelper.class).to(ErrorObjectHelper.class).in(Singleton.class);
//
//	    // Id Generator
//	    this.bind(UuidServiceIdGenerator.class).to(ServiceIdGenerator.class).in(Singleton.class);
//	    this.bind(AtomicEntityIdGenerator.class).to(EntityIdGenerator.class).in(PerLookup.class);
//	    this.bind(IdGenerator.class).to(IdGenerator.class).in(PerLookup.class);
		super.configure();
		
		 this.bindFactory(ResourceConverterFactory.class).to(ResourceConverter.class)
	        .in(Singleton.class);
		 
		this.bind(MongoConnection.class).to(MongoConnection.class).in(Singleton.class);

		this.bindFactory(DatastoreFactory.class).to(Datastore.class).in(Singleton.class);
		
		this.bind(TutorialMongoCrudService.class).to(TutorialMongoCrudService.class).in(Singleton.class);
		this.bind(SequenceMongoCrudService.class).to(SequenceMongoCrudService.class).in(Singleton.class);
		this.bind(StepMongoCrudService.class).to(StepMongoCrudService.class).in(Singleton.class);
		this.bind(TutorialLandscapeMongoCrudService.class).to(TutorialLandscapeMongoCrudService.class).in(Singleton.class);
		this.bind(TutorialTimestampMongoCrudService.class).to(TutorialTimestampMongoCrudService.class).in(Singleton.class);

	}
}
