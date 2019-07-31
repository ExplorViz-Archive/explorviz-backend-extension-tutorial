package net.explorviz.extension.tutorial.repository.persistence.mongo;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import java.util.List;
import javax.inject.Inject;

import net.explorviz.extension.tutorial.model.Tutorial;

/**
 * Helper class for de-/serializing tutorials from/to json api.
 *
 */
public class TutorialSerializationHelper {

	private final ResourceConverter jsonApiConverter;

	@Inject
	public TutorialSerializationHelper(final ResourceConverter jsonApiConverter) {
		this.jsonApiConverter = jsonApiConverter;
	}

	/**
	 * Serializes a tutorial to a json api string.
	 *
	 * @throws DocumentSerializationException if the tutorial could not be parsed.
	 */
	public String serialize(final Tutorial t) throws DocumentSerializationException {
		final JSONAPIDocument<Tutorial> tutorialDoc = new JSONAPIDocument<>(t);
		final byte[] tutorialBytes = this.jsonApiConverter.writeDocument(tutorialDoc);
		return new String(tutorialBytes);
	}

	/**
	 * Serializes a list of tutorials to a json api string.
	 *
	 * @throws DocumentSerializationException if the tutorial list could not be
	 *                                        parsed.
	 */
	public String serializeToList(final List<Tutorial> t) throws DocumentSerializationException {
		final JSONAPIDocument<List<Tutorial>> tutorialDoc = new JSONAPIDocument<>(t);
		final byte[] tutorialBytes = this.jsonApiConverter.writeDocumentCollection(tutorialDoc);
		return new String(tutorialBytes);
	}

	/**
	 * Deserializes a json-api string to a {@link Tutorial} object.
	 *
	 * @param jsonApi the json api string representing a landscape
	 * @return the tutorial
	 * @throws DocumentSerializationException if the given string can't be
	 *                                        deserialized to a tutorial
	 */
	public Tutorial deserialize(final String jsonApi) throws DocumentSerializationException {

		final byte[] b = jsonApi.getBytes();
		final JSONAPIDocument<Tutorial> tutorialDoc = this.jsonApiConverter.readDocument(b, Tutorial.class);

		return tutorialDoc.get();
	}

	/**
	 * Deserializes a json-api string to a list of {@link Tutorial} objects.
	 *
	 * @param jsonApi the json api string representing a tutorial
	 * @return the tutorial list
	 * @throws DocumentSerializationException if the given string can't be
	 *                                        deserialized to a tutorial
	 */
	public List<Tutorial> deserializeToList(final String jsonApi) throws DocumentSerializationException {

		final byte[] b = jsonApi.getBytes();
		final JSONAPIDocument<List<Tutorial>> tutorialDoc = this.jsonApiConverter.readDocumentCollection(b,
				Tutorial.class);

		return tutorialDoc.get();
	}

}
