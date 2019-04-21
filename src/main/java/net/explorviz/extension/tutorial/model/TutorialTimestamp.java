package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;

@Entity("tutorialtimestamp")
@Type("tutorialtimestamp")
public class TutorialTimestamp {
	
	private String name;
	
	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;
	
	private String timestamp;
	
	private Integer totalRequests;
	
	public TutorialTimestamp() {
		
	}
	public TutorialTimestamp(String id,String timestamp,String name) {
		this.id=id;
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
	public Integer getTotalRequests() {
		return totalRequests;
	}
	public void setTotalRequests(Integer totalRequests) {
		this.totalRequests = totalRequests;
	}
	
	
}
