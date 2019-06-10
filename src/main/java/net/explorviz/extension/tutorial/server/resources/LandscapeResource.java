package net.explorviz.extension.tutorial.server.resources;

import java.util.List;
import java.util.NoSuchElementException;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.extension.tutorial.model.TutorialTimestamp;
import net.explorviz.extension.tutorial.services.TutorialCrudException;
import net.explorviz.extension.tutorial.services.TutorialLandscapeMongoCrudService;
import net.explorviz.extension.tutorial.services.TutorialTimestampMongoCrudService;

@Path("v1/tutorials/landscapes")
public class LandscapeResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TITLE = "Invalid title";
	private static final String MSG_TUTORIAL_NOT_RETRIEVED = "Could not retrieve tutorialLandscape ";
	private static final String MSG_TUTORIAL_NOT_UPDATED = "Could not update tutorialLandscape ";

	private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeResource.class);

	@Inject
	private TutorialLandscapeMongoCrudService tutorialLandscapeCrudService;

	@Inject
	private TutorialTimestampMongoCrudService timestampLandscapeCrudService;
	
	/**
	 * Retrieves a single tutorialLandscape identified by its id.
	 *
	 * @param id the id of the tutorialLandscape to return
	 * @return the {@link TutorialLandscape} object with the given id
	 */
	@GET
	@Path("/by-timestamp")
	@Produces(MEDIA_TYPE)
	public TutorialLandscape tutorialLandscapeByTimestamp(@QueryParam("timestamp") final String timestamp) throws TutorialCrudException{
		TutorialLandscape foundTutorialLandscape = null;
		TutorialTimestamp foundTimestamp = null;

		try {
			foundTimestamp = this.timestampLandscapeCrudService.findEntityByFieldValue("timestamp", timestamp).get();
			foundTutorialLandscape = this.tutorialLandscapeCrudService.findEntityByFieldValue("tutorialtimestamp", foundTimestamp).get();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve tutorialLandscape: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		} catch (final NoSuchElementException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve tutorialLandscape: " + ex.getMessage());
			}
			throw new TutorialCrudException("landscape not found");		
			}

		return foundTutorialLandscape;
	}

	@GET
	@Path("/all")
	@Produces(MEDIA_TYPE)
	public List<TutorialLandscape> tutorialLandscapesAll() {
		List<TutorialLandscape> foundTutorialLandscapes = null;

		try {
			foundTutorialLandscapes = this.tutorialLandscapeCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all tutorialLandscapes: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundTutorialLandscapes;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public TutorialLandscape updateTutorialLandscape(@PathParam("id") final String id, final TutorialLandscape updatedTutorialLandscape) { // NOPMD
		TutorialLandscape targetTutorialLandscape = null;
		try {
			targetTutorialLandscape = this.tutorialLandscapeCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		if (updatedTutorialLandscape.getId() != null || updatedTutorialLandscape.getId() != id) { // NOPMD
			LOGGER.info("Won't update id");
		}

			targetTutorialLandscape.setLandscape(updatedTutorialLandscape.getLandscape());
			targetTutorialLandscape.setTimestamp(updatedTutorialLandscape.getTimestamp());
			
		try {
			this.tutorialLandscapeCrudService.updateEntity(targetTutorialLandscape);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetTutorialLandscape;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeTutorialLandscape(@PathParam("id") final String id) {
		try {
			this.tutorialLandscapeCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update tutorialLandscape: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public TutorialLandscape newTutorialLandscape(final TutorialLandscape tutorialLandscape) { // NOPMD

		if (tutorialLandscape.getId() != null) {
			throw new BadRequestException("Can't create tutorialLandscape with id. Payload must not have an id.");
		}

		try {
			return this.tutorialLandscapeCrudService.saveNewEntity(tutorialLandscape).orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("TutorialLandscape already exists", ex);
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
	public TutorialLandscape importTutorialLandscape(final TutorialLandscape tutorialLandscape) { // NOPMD
		try {
			return this.tutorialLandscapeCrudService.saveNewEntity(tutorialLandscape).orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("TutorialLandscape already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}


}
