package net.explorviz.extension.tutorial.repository.persistence.mongo;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.explorviz.shared.config.annotations.Config;

/**
 * Stores and retrieves landscapes from a mongodb, which is given in the
 * {@code explorviz.properties} resource.
 *
 * <p>
 *
 * This repository will return all requested landscape objects in the json api
 * format, which is the format the objects are persisted in internally. Prefer
 * this class over {@link MongoLandscapeRepository} if you don't need an
 * actually landscape object to avoid costy de-/serialization.
 *
 * </p>
 *
 */
public class MongoTutorialJsonApiRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoTutorialJsonApiRepository.class);

	private final MongoHelper mongoHelper;

	private final TutorialSerializationHelper serializationHelper;

	@Config("repository.history.intervalInMinutes")
	private int intervalInMinutes;

	@Inject
	public MongoTutorialJsonApiRepository(final MongoHelper mongoHelper, final TutorialSerializationHelper helper) {
		this.mongoHelper = mongoHelper;
		this.serializationHelper = helper;
	}

}
