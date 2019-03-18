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

import net.explorviz.extension.tutorial.model.LandscapeSerialized;
import net.explorviz.extension.tutorial.services.LandscapeSerializedMongoCrudService;

@Path("v1/tutorials/landscapes")
public class LandscapeSerializedResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TIMESTAMP = "Invalid timestamp";
	private static final String MSG_LANDSCAPE_NOT_RETRIEVED = "Could not retrieve landscapeSerialized ";
	private static final String MSG_LANDSCAPE_NOT_UPDATED = "Could not update landscapeSerialized ";

	private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeSerializedResource.class);

	@Inject
	private LandscapeSerializedMongoCrudService landscapeSerializedCrudService;

	/**
	 * Retrieves a single landscapeSerialized identified by its id.
	 *
	 * @param id the id of the landscapeSerialized to return
	 * @return the {@link LandscapeSerialized} object with the given id
	 */
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public LandscapeSerialized landscapeSerializedById(@PathParam("id") final Long id) {
		if (id == null || id <= 0) {
			throw new BadRequestException("Id must be positive integer");
		}

		LandscapeSerialized foundLandscape = null;

		try {
			foundLandscape = this.landscapeSerializedCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve landscape: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return foundLandscape;
	}

	@GET
	@Produces(MEDIA_TYPE)
	public List<LandscapeSerialized> landscapeSerializedsAll() {
		List<LandscapeSerialized> foundLandscape = null;

		try {
			foundLandscape = this.landscapeSerializedCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all landscapes: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundLandscape;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public LandscapeSerialized updateLandscapeSerialized(@PathParam("id") final Long id, final LandscapeSerialized updatedLandscapeSerialized) { // NOPMD
		LandscapeSerialized targetLandscapeSerialized = null;
		try {
			targetLandscapeSerialized = this.landscapeSerializedCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_LANDSCAPE_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		if (updatedLandscapeSerialized.getTimestamp() != null || updatedLandscapeSerialized.getTimestamp() != id) { // NOPMD
			LOGGER.info("Won't update id");
		}

		targetLandscapeSerialized.setSerializedData(updatedLandscapeSerialized.getSerializedData());
	
		try {
			this.landscapeSerializedCrudService.updateEntity(targetLandscapeSerialized);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_LANDSCAPE_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetLandscapeSerialized;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeLandscapeSerialized(@PathParam("id") final Long id) {
		try {
			this.landscapeSerializedCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update landscapeSerialized: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public LandscapeSerialized newLandscapeSerialized(final LandscapeSerialized landscapeSerialized) { // NOPMD

		if (landscapeSerialized.getTimestamp() == null || landscapeSerialized.getTimestamp().equals("")) {
			throw new BadRequestException(MSG_INVALID_TIMESTAMP);
		}

		try {
			return this.landscapeSerializedCrudService.saveNewEntity(landscapeSerialized)
					.orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("LandscapeSerialized already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_LANDSCAPE_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}

}
