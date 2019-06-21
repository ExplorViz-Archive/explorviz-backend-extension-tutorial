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

@Type("tutorial")
@Entity("tutorial")
public class Tutorial implements Serializable {
	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;
	
	private String targetId;

	private String targetType;

	private String landscapeTimestamp;
	
	public String getLandscapeTimestamp() {
		return landscapeTimestamp;
	}

	public void setLandscapeTimestamp(String landscapeTimestamp) {
		this.landscapeTimestamp = landscapeTimestamp;
	}

	@Reference
	@Relationship("sequences")
	private List<Sequence> sequences = new ArrayList<>();


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

	public List<Sequence> getSequences() {
		return sequences;
	}

	public void setSequences(final List<Sequence> sequences) {
		this.sequences = sequences;
	}

	public void addSequence(final Sequence sequence) {
		this.sequences.add(sequence);
	}
	public void removeSequence(Sequence sequence) {
		for(Sequence s: sequences) {
			if(s.getId().equals(sequence.getId())){
				sequence=s;
			}
		}
		this.sequences.remove(sequence);
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


}
