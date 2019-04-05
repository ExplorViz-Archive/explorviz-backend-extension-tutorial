package net.explorviz.extension.tutorial.server.resources;

import java.util.List;
import java.util.regex.Matcher;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;

import net.explorviz.extension.tutorial.model.Sequence;
import net.explorviz.extension.tutorial.services.LandscapeMongoService;

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
	public String getLandscapeByTimestamp(@QueryParam("timestamp") final String timestamp) {
		return this.landscapeMongoService.findEntityByTimestamp(timestamp).get();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	@Path("/import")
	public String importLandscape(@QueryParam("landscape") final String landscape) {
		try {
			return 	this.landscapeMongoService.saveNewEntity(landscape)
					.orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("Sequence already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_LANDSCAPE_NOT_SAVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}
	

	@GET
	@Produces(MEDIA_TYPE)
	public String landscapesAll() {
		List<String> foundLandscapes = null;
		try {
			foundLandscapes = this.landscapeMongoService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all sequences: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		String data ="";
		String included ="";
		for(String landscape:foundLandscapes) {
			System.out.println(landscape);
			Matcher m=this.landscapeMongoService.getDataAndIncludedFromLandscape(landscape);
			m.find();
			if(m.groupCount()==2){
				included+=m.group(2)+",";
				data+=m.group(1)+",";
			}
		}
		data="{\"data\":"+data.substring(0, data.length()-1)+",";
		included="\"included\":["+included.substring(0, included.length()-1)+"]}";
		return data+included;
	}

}
