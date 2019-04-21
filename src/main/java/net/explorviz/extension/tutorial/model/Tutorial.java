package net.explorviz.extension.tutorial.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;
import xyz.morphia.annotations.Reference;
import xyz.morphia.annotations.Transient;

@Type("tutorial")
@Entity("tutorial")
public class Tutorial implements Serializable {
	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;

	private String text;
	
	private String landscapeTimestamp;

	@Transient
	private String landscape;
	
	private String targetId;

	private String targetType;

	
	@Reference
	@Relationship("sequences")
	private List<Sequence> sequences = new ArrayList<>();

	public String getLandscapeTimestamp() {
		return landscapeTimestamp;
	}

	public void setLandscapeTimestamp(String landscapeTimestamp) {
		this.landscapeTimestamp = landscapeTimestamp;
	}
		
	public String getId() {
		return id;
	}

	public void setId(final String id) {
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

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getLandscape() {
		return landscape;
	}

	public void setLandscape(String landscape) {
		this.landscape = landscape;
	}



}
