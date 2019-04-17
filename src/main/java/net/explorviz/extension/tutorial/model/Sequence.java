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

//import net.explorviz.model.landscape.Landscape;
@Type("sequence")
@Entity("sequence")
public class Sequence {

	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;

	private String text;

	@Reference
	@Relationship("steps")
	private List<Step> steps = new ArrayList<>();

	private String action;
	// private final Landscape landscape;

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

	public String getAction() {
		return action;
	}

	public void setAction(final String action) {
		this.action = action;
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

	public String getText() {
		return text;
	}	

}
