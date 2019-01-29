package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;

@Type("step")
@Entity("step")
public class Step {

	@Id
	@com.github.jasminb.jsonapi.annotations.Id(LongIdHandler.class)
	private Long id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;

	private String action;

	private Interactable target;

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

	public String getAction() {
		return action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public Interactable getTarget() {
		return target;
	}

	public void setTarget(final Interactable target) {
		this.target = target;
	}

}
