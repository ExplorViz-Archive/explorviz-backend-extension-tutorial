package net.explorviz.extension.tutorial.model;

import java.io.Serializable;

import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.IndexOptions;
import xyz.morphia.annotations.Indexed;

@Type("step")
@Entity("step")
public class Step{

	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;

	@Indexed(options = @IndexOptions(unique = true))
	private String title;


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



}
