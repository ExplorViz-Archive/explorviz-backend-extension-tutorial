package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;

@Entity("tutoriallandscape")
@Type("tutoriallandscape")
public class TutorialLandscape {
	
	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;
	
	
	private TutorialTimestamp timestamp;
	
	private String landscape;
		
	public TutorialLandscape() {
		
	}
	
	public TutorialLandscape(String id,TutorialTimestamp timestamp,String landscape) {
		this.id=id;
		this.timestamp=timestamp;
		this.landscape=landscape;
	}
	
	public TutorialTimestamp getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(TutorialTimestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getLandscape() {
		return landscape;
	}
	
	public void setLandscape(String landscape) {
		this.landscape = landscape;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
}

