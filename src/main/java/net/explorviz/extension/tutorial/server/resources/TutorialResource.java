package net.explorviz.extension.tutorial.server.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

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

import net.explorviz.extension.tutorial.model.Tutorial;
import net.explorviz.extension.tutorial.services.LandscapeMongoService;
import net.explorviz.extension.tutorial.services.TutorialMongoCrudService;
import net.explorviz.shared.config.helper.PropertyHelper;

@Path("v1/tutorials")
public class TutorialResource {
	private static final String MEDIA_TYPE = "application/vnd.api+json";

	private static final String MSG_INVALID_TITLE = "Invalid title";
	private static final String MSG_TUTORIAL_NOT_RETRIEVED = "Could not retrieve tutorial ";
	private static final String MSG_TUTORIAL_NOT_UPDATED = "Could not update tutorial ";
	private static final String MSG_INVALID_LANDSCAPE = "Invalid Landscape";

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TutorialResource.class);

	@Inject
	private TutorialMongoCrudService tutorialCrudService;

	@Inject
	private LandscapeMongoService landscapeMongoService;

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
				LOGGER.error("Could not retrieve tutorial: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		;
		LOGGER.info("Delivered: " +foundTutorial.getId() );
		return foundTutorial;
	}

	@GET
	@Produces(MEDIA_TYPE)
	public List<Tutorial> tutorialsAll() {
		List<Tutorial> foundTutorials = null;

		try {
			foundTutorials = this.tutorialCrudService.getAll();
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not retrieve all tutorials: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
		
		String tutIds="";
		for(Tutorial tut: foundTutorials ) {
			tutIds+=" "+tut.getId();
		}
		LOGGER.info("Delivered: " +tutIds );

		return foundTutorials;
	}

	@PATCH
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	@Consumes(MEDIA_TYPE)
	public Tutorial updateTutorial(@PathParam("id") final Long id, final Tutorial updatedTutorial) throws IOException { // NOPMD
		Tutorial targetTutorial = null;
		try {
			targetTutorial = this.tutorialCrudService.getEntityById(id).orElseThrow(() -> new NotFoundException());
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		if (updatedTutorial.getId() != null && !updatedTutorial.getId().equals(id)) { // NOPMD
			LOGGER.info("Won't update id "+updatedTutorial.getId()+" "+id);
		}

		if (updatedTutorial.getTitle() != null) {
			if (updatedTutorial.getTitle().equals("")) {
				throw new BadRequestException(MSG_INVALID_TITLE);
			}

			targetTutorial.setTitle(updatedTutorial.getTitle());
		}
		
		if (updatedTutorial.getLandscapeTimestamp() != null) {
			if (updatedTutorial.getLandscapeTimestamp().equals("")) {
				throw new BadRequestException(MSG_INVALID_LANDSCAPE);
			}
			
//			Optional<String> landscape = landscapeMongoService.findEntityByTimestamp(updatedTutorial.getLandscapeTimestamp());
//			if(!landscape.isPresent()){
//				Integer port = 8080;
//				  try {
//				      port = PropertyHelper.getIntegerProperty("landscape.port");
//				    } catch (final NumberFormatException e) {
//				      if (LOGGER.isInfoEnabled()) {
//				        LOGGER.info(
//				            "ATTENTION: Using default port "+port+". Check explorviz.properties file.",
//				            e);
//				      }				
//				    }
//				  
//				    String url =  PropertyHelper.getStringProperty("landscape.url");
//	                String host = PropertyHelper.getStringProperty("landscape.host");
//	                URL oracle = new URL("http://"+host+":"+port+url+updatedTutorial.getLandscapeTimestamp()); // URL to Parse
//		            URLConnection yc = oracle.openConnection();
//		            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//		            
//		            String inputLine;
//		            while ((inputLine = in.readLine()) != null) {
//		            	System.out.println(inputLine);
//		            }
//			}
			targetTutorial.setLandscapeTimestamp(updatedTutorial.getLandscapeTimestamp());
		}
		
		if (updatedTutorial.getTargetId() != null) {
			targetTutorial.setTargetId(updatedTutorial.getTargetId());
		}
		
		if (updatedTutorial.getTargetType() != null) {
			targetTutorial.setTargetType(updatedTutorial.getTargetType());
		}

		if (updatedTutorial.getSequences().size() > 0) {
			targetTutorial.setSequences(updatedTutorial.getSequences());
		}

		try {
			this.tutorialCrudService.updateEntity(targetTutorial);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_UPDATED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return targetTutorial;
	}

	/**
	 * Removes the user with the given id.
	 *
	 * @param id the id of the user to delete
	 */
	@DELETE
	@Path("{id}")
	public Response removeTutorial(@PathParam("id") final Long id) {
		try {
			this.tutorialCrudService.deleteEntityById(id);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Could not update tutorial: " + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}

		return Response.status(HttpStatus.NO_CONTENT_204).build();
	}

	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public Tutorial newTutorial(final Tutorial tutorial) { // NOPMD

		if (tutorial.getTitle() == null || tutorial.getTitle().equals("")) {
			throw new BadRequestException(MSG_INVALID_TITLE);
		}

		if (tutorial.getId() != null) {
			throw new BadRequestException("Can't create tutorial with id. Payload must not have an id.");
		}

		try {
			return this.tutorialCrudService.saveNewEntity(tutorial)
					.orElseThrow(() -> new InternalServerErrorException());
		} catch (final DuplicateKeyException ex) {
			throw new BadRequestException("Tutorial already exists", ex);
		} catch (final MongoException ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(MSG_TUTORIAL_NOT_RETRIEVED + ex.getMessage() + " (" + ex.getCode() + ")");
			}
			throw new InternalServerErrorException(ex);
		}
	}

}
