package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;

@Entity("tutorialtimestamp")
@Type("tutorialtimestamp")
public class TutorialTimestamp {
	@Id
	@com.github.jasminb.jsonapi.annotations.Id(LongIdHandler.class)
	private String id;
	
	private String name;
	
	
	private String timestamp;
	
	
	public TutorialTimestamp(String timestamp,String name) {
		this.name = name;
		this.timestamp = timestamp;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	
}
