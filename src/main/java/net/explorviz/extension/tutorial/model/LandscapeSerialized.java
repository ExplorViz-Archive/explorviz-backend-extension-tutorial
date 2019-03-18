package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;

@Type("landscape")
@Entity("landscape")
public class LandscapeSerialized {
	@Id
	@com.github.jasminb.jsonapi.annotations.Id(LongIdHandler.class)	
	private Long timestamp;
	
	private String serializedData;

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSerializedData() {
		return serializedData;
	}

	public void setSerializedData(String serializedData) {
		this.serializedData = serializedData;
	}
	
	
}
