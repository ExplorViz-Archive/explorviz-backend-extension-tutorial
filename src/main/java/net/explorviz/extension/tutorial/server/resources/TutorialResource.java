package net.explorviz.extension.tutorial.server.resources;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.extension.tutorial.services.TutorialMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialService;

@Path("v1/tutorial")
public class TutorialResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final Logger LOGGER = LoggerFactory.getLogger(TutorialResource.class);

	@Inject
	private TutorialService tutorialService;

	@Inject
	private TutorialMongoCrudService tutorialCrudService;

	/**
	 * Retrieves a single tutorial identified by its id.
	 *
	 * @param id the id of the tutorial to return
	 * @return the {@link Tutorial} object with the given id
	 */
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public Tutorial tutorialById(@PathParam("id") final Long id) {
		if (id == null || id <= 0) {
			throw new BadRequestException("Id must be positive integer");
		}

		Tutorial foundTutorial = null;

		try {
			foundTutorial = this.tutorialCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve user: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return foundTutorial;

	}

}
