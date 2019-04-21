package net.explorviz.extension.tutorial.main;

import org.glassfish.jersey.server.ResourceConfig;

import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.extension.tutorial.server.providers.JsonApiListProvider;
import net.explorviz.extension.tutorial.server.providers.JsonApiProvider;
import net.explorviz.extension.tutorial.server.providers.ResourceConverterFactory;
import net.explorviz.extension.tutorial.server.resources.LandscapeResource;
import net.explorviz.extension.tutorial.server.resources.SequenceResource;
import net.explorviz.extension.tutorial.server.resources.StepResource;
import net.explorviz.extension.tutorial.server.resources.TimestampResource;
import net.explorviz.extension.tutorial.server.resources.TutorialResource;
import net.explorviz.shared.exceptions.mapper.GeneralExceptionMapper;
import net.explorviz.shared.exceptions.mapper.WebApplicationExceptionMapper;
import net.explorviz.shared.security.filters.CorsResponseFilter;

/**
 * JAX-RS application. This class is responsible for registering all types of
 * classes, e.g., resource classes or exception mappers
 */
public class Application extends ResourceConfig {

	/**
	 * JAX-RS application. This class is responsible for registering all types of
	 * classes, e.g., resource classes or exception mappers
	 */
	public Application() { // NOPMD

		// register CDI
		this.register(new DependencyInjectionBinder());

		// this.register(AuthenticationFilter.class);
		this.register(CorsResponseFilter.class);

		// this.register(net.explorviz.shared.security.filters.AuthenticationFilter.class);
		// this.register(net.explorviz.shared.security.filters.AuthorizationFilter.class);

		// exception handling (mind the order !)
		this.register(WebApplicationExceptionMapper.class);
		this.register(GeneralExceptionMapper.class);

		this.register(SetupApplicationListener.class);
		
		this.register(JsonApiProvider.class);
		this.register(JsonApiListProvider.class);
		this.register(ResourceConverterFactory.class);

		// register all resources
		this.register(TutorialResource.class);
		this.register(SequenceResource.class);
		this.register(StepResource.class);
		this.register(LandscapeResource.class);
		this.register(TimestampResource.class);

	}

}
