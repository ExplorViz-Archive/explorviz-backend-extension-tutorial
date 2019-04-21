package net.explorviz.extension.tutorial.server.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.Step;
import net.explorviz.extension.tutorial.services.StepMongoCrudService;

@Path("v1/tutorials/steps")
public class StepResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TITLE = "Invalid title";
	private static final String MSG_TUTORIAL_NOT_RETRIEVED = "Could not retrieve step ";
	private static final String MSG_TUTORIAL_NOT_UPDATED = "Could not update step ";

	private static final Logger LOGGER = LoggerFactory.getLogger(StepResource.class);

	@Inject
	private StepMongoCrudService stepCrudService;

	/**
	 * Retrieves a single step identified by its id.
	 *
	 * @param id the id of the step to return
	 * @return the {@link Step} object with the given id
	 */
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public Step stepById(@PathParam("id") final String id) {
		Step foundStep = null;

		try {
			foundStep = this.stepCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve step: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundStep;
	}

	@GET
	@Produces(MEDIA_TYPE)
	public List<Step> stepsAll() {
		List<Step> foundSteps = null;

		try {
			foundSteps = this.stepCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all steps: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundSteps;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public Step updateStep(@PathParam("id") final String id, final Step updatedStep) { // NOPMD
		Step targetStep = null;
		try {
			targetStep = this.stepCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		if (updatedStep.getId() != null || updatedStep.getId() != id) { // NOPMD
			LOGGER.info("Won't update id");
		}

		if (updatedStep.getTitle() != null) {
			if (updatedStep.getTitle().equals("")) {
				throw new BadRequestException(MSG_INVALID_TITLE);
			}
			targetStep.setTitle(updatedStep.getTitle());
		}

			
		try {
			this.stepCrudService.updateEntity(targetStep);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetStep;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeStep(@PathParam("id") final String id) {
		try {
			this.stepCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update step: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public Step newStep(final Step step) { // NOPMD

		if (step.getTitle() == null || step.getTitle().equals("")) {
			throw new BadRequestException(MSG_INVALID_TITLE);
		}

		if (step.getId() != null) {
			throw new BadRequestException("Can't create step with id. Payload must not have an id.");
		}

		try {
			return this.stepCrudService.saveNewEntity(step).orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("Step already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}

}
