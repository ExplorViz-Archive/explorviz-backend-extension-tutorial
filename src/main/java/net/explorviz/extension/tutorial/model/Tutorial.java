package net.explorviz.extension.tutorial.model;

import java.util.ArrayList;
import java.util.List;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;
import xyz.morphia.annotations.Reference;

@Type("tutorial")
@Entity("tutorial")
public class Tutorial {
	@Id
	@com.github.jasminb.jsonapi.annotations.Id(LongIdHandler.class)
	private Long id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;

	private String text;
	
	@Reference
	@Relationship("landscape")
	private LandscapeSerialized landscape;

	@Reference
	@Relationship("sequences")
	private List<Sequence> sequences = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public List<Sequence> getSequences() {
		return sequences;
	}

	public void setSequences(final List<Sequence> sequences) {
		this.sequences = sequences;
	}

	public void addSequence(final Sequence sequence) {
		this.sequences.add(sequence);
	}

	public LandscapeSerialized getLandscape() {
		return landscape;
	}

	public void setLandscape(LandscapeSerialized landscape) {
		this.landscape = landscape;
	}

}
