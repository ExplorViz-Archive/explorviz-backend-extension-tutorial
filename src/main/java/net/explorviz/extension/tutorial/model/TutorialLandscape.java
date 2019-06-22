package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

import xyz.morphia.annotations.Entity;
import xyz.morphia.annotations.Id;
import xyz.morphia.annotations.Reference;

@Entity("tutoriallandscape")
@Type("tutoriallandscape")
public class TutorialLandscape {
	
	@Id
	@com.github.jasminb.jsonapi.annotations.Id
	private String id;
	
	@Reference
	@Relationship("timestamp")
	private TutorialTimestamp tutorialtimestamp;
	
	private String landscape;
	
	public TutorialLandscape() {
		
	}
	
	public TutorialLandscape(String id,TutorialTimestamp tutorialtimestamp,String landscape) {
		this.id=id;
		this.tutorialtimestamp=tutorialtimestamp;
		this.landscape=landscape;
	}
	
	public TutorialTimestamp getTimestamp() {
		return tutorialtimestamp;
	}
	
	public void setTimestamp(TutorialTimestamp tutorialtimestamp) {
		this.tutorialtimestamp = tutorialtimestamp;
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

