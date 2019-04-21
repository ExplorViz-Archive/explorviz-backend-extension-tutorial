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

import net.explorviz.extension.tutorial.model.TutorialTimestamp;
import net.explorviz.extension.tutorial.services.TutorialTimestampMongoCrudService;

@Path("v1/tutorials/timestamps")
public class TimestampResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TITLE = "Invalid title";
	private static final String MSG_TUTORIAL_NOT_RETRIEVED = "Could not retrieve tutorialTutorialTimestamp ";
	private static final String MSG_TUTORIAL_NOT_UPDATED = "Could not update tutorialTutorialTimestamp ";

	private static final Logger LOGGER = LoggerFactory.getLogger(TimestampResource.class);

	@Inject
	private TutorialTimestampMongoCrudService tutorialTutorialTimestampCrudService;

	/**
	 * Retrieves a single tutorialTutorialTimestamp identified by its id.
	 *
	 * @param id the id of the tutorialTutorialTimestamp to return
	 * @return the {@link TutorialTimestamp} object with the given id
	 */
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public TutorialTimestamp tutorialTutorialTimestampById(@PathParam("id") final String id) {
		TutorialTimestamp foundTutorialTimestamp = null;

		try {
			foundTutorialTimestamp = this.tutorialTutorialTimestampCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve tutorialTutorialTimestamp: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return foundTutorialTimestamp;
	}

	@GET
	@Produces(MEDIA_TYPE)
	public List<TutorialTimestamp> tutorialTutorialTimestampsAll() {
		List<TutorialTimestamp> foundTutorialTimestamps = null;

		try {
			foundTutorialTimestamps = this.tutorialTutorialTimestampCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all tutorialTutorialTimestamps: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundTutorialTimestamps;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public TutorialTimestamp updateTutorialTimestamp(@PathParam("id") final String id, final TutorialTimestamp updatedTutorialTimestamp) { // NOPMD
		TutorialTimestamp targetTutorialTimestamp = null;
		try {
			targetTutorialTimestamp = this.tutorialTutorialTimestampCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

//		if (updatedTutorialTimestamp.getId() != null || updatedTutorialTimestamp.getId() != id) { // NOPMD
//			LOGGER.info("Won't update id");
//		}

			targetTutorialTimestamp.setName(updatedTutorialTimestamp.getName());
			targetTutorialTimestamp.setTimestamp(updatedTutorialTimestamp.getTimestamp());
			
		try {
			this.tutorialTutorialTimestampCrudService.updateEntity(targetTutorialTimestamp);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetTutorialTimestamp;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeTutorialTimestamp(@PathParam("id") final String id) {
		try {
			this.tutorialTutorialTimestampCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update tutorialTutorialTimestamp: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public TutorialTimestamp newTutorialTimestamp(final TutorialTimestamp tutorialTutorialTimestamp) { // NOPMD

//		if (tutorialTutorialTimestamp.getId() != null) {
//			throw new BadRequestException("Can't create tutorialTutorialTimestamp with id. Payload must not have an id.");
//		}

		try {
			return this.tutorialTutorialTimestampCrudService.saveNewEntity(tutorialTutorialTimestamp).orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("TutorialTimestamp already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}
	
	@POST
	@Path("/import")
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public TutorialTimestamp importTutorialTimestamp(final TutorialTimestamp tutorialTutorialTimestamp) { // NOPMD

		try {
			return this.tutorialTutorialTimestampCrudService.saveNewEntity(tutorialTutorialTimestamp).orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("TutorialTimestamp already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}

}
