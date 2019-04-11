package net.explorviz.extension.tutorial.model;

import com.github.jasminb.jsonapi.StringIdHandler;
import com.github.jasminb.jsonapi.annotations.Type;


@Type("tutoriallandscapes")
public class TutorialLandscape {
	
	@com.github.jasminb.jsonapi.annotations.Id(StringIdHandler.class)
	private String timestamp;
	
	private String landscape;
	
	public TutorialLandscape() {
		
	}
	
	public TutorialLandscape(String timestamp,String landscape) {
		this.timestamp=timestamp;
		this.landscape=landscape;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getLandscape() {
		return landscape;
	}
	
	public void setLandscape(String landscape) {
		this.landscape = landscape;
	}
}

