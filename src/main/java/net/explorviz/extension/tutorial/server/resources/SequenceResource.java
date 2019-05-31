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

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.services.SequenceMongoCrudService;

@Path("v1/tutorials/sequences")
public class SequenceResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TITLE = "Invalid title";
	private static final String MSG_TUTORIAL_NOT_RETRIEVED = "Could not retrieve sequence ";
	private static final String MSG_TUTORIAL_NOT_UPDATED = "Could not update sequence ";
	private static final String MSG_INVALID_LANDSCAPE = "Invalid Landscape";

	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceResource.class);

	@Inject
	private SequenceMongoCrudService sequenceCrudService;

	/**
	 * Retrieves a single sequence identified by its id.
	 *
	 * @param id the id of the sequence to return
	 * @return the {@link Sequence} object with the given id
	 */
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public Sequence sequenceById(@PathParam("id") final String id) {
		Sequence foundSequence = null;

		try {
			foundSequence = this.sequenceCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve sequence: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return foundSequence;
	}

	@GET
	@Produces(MEDIA_TYPE)
	public List<Sequence> sequencesAll() {
		List<Sequence> foundSequences = null;

		try {
			foundSequences = this.sequenceCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all sequences: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		return foundSequences;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public Sequence updateSequence(@PathParam("id") final String id, final Sequence updatedSequence) { // NOPMD
		Sequence targetSequence = null;
		try {
			targetSequence = this.sequenceCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		if (updatedSequence.getId() != null || updatedSequence.getId() != id) { // NOPMD
			LOGGER.info("Won't update id");
		}
		
		if (updatedSequence.getLandscapeTimestamp() != null) {
			targetSequence.setLandscapeTimestamp(updatedSequence.getLandscapeTimestamp());
		}

		if (updatedSequence.getTitle() != null) {
			if (updatedSequence.getTitle().equals("")) {
				throw new BadRequestException(MSG_INVALID_TITLE);
			}

			targetSequence.setTitle(updatedSequence.getTitle());
		}
		
		if (updatedSequence.getSteps().size() > 0) {
			targetSequence.setSteps(updatedSequence.getSteps());
		}

		try {
			this.sequenceCrudService.updateEntity(targetSequence);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetSequence;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeSequence(@PathParam("id") final String id) {
		try {
			this.sequenceCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update sequence: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public Sequence newSequence(final Sequence sequence) { // NOPMD

		if (sequence.getTitle() == null || sequence.getTitle().equals("")) {
			throw new BadRequestException(MSG_INVALID_TITLE);
		}

		if (sequence.getId() != null) {
			throw new BadRequestException("Can't create sequence with id. Payload must not have an id.");
		}

		try {
			return this.sequenceCrudService.saveNewEntity(sequence)
					.orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("Sequence already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}

}
