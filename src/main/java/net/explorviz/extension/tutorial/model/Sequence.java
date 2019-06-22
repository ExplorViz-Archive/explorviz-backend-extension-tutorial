package net.explorviz.extension.tutorial.model;

import java.io.Serializable;
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
import xyz.morphia.annotations.Transient;

//import net.explorviz.model.landscape.Landscape;
@Type("sequence")
@Entity("sequence")
public class Sequence implements Serializable{

	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;

	private String title;

	private String text;

	@Reference
	@Relationship("steps")
	private List<Step> steps = new ArrayList<>();
	
	private String landscapeTimestamp;
	
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

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(final List<Step> steps) {
		this.steps = steps;
	}

	public void addStep(final Step step) {
		this.steps.add(step);
	}
	public void removeStep(Step step) {
		for(Step s: steps) {
			if(s.getId().equals(step.getId())){
				step=s;
			}
		}
		this.steps.remove(step);

	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	

}
