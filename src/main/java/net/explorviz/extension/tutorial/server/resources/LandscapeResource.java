package net.explorviz.extension.tutorial.server.resources;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.TutorialLandscape;
import net.explorviz.extension.tutorial.services.LandscapeMongoService;
import net.explorviz.shared.landscape.model.landscape.Landscape;

/**
 * Resource providing {@link Landscape} data for the frontend.
 */
@Path("v1/tutorials/landscapes")
@RolesAllowed({ "admin" })
public class LandscapeResource {
	
	

	private static final String MEDIA_TYPE = "application/vnd.api+json";
	private static final String MSG_LANDSCAPE_NOT_SAVED = "Could not import landscape.";

	@Inject
	private LandscapeMongoService landscapeMongoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LandscapeResource.class);


	@GET
	@Path("/by-timestamp")
	@Produces(MEDIA_TYPE)
	public TutorialLandscape getLandscapeByTimestamp(@QueryParam("timestamp") final String timestamp) {
		Optional<TutorialLandscape> opt = this.landscapeMongoService.findEntityByTimestamp(timestamp);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new BadRequestException();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	@Path("/import")
	public TutorialLandscape importLandscape(final TutorialLandscape landscape) {
		
		try {
			return 	this.landscapeMongoService.saveNewEntity(landscape).get();
			} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("Sequence already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_LANDSCAPE_NOT_SAVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}
}
