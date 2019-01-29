package net.explorviz.extension.tutorial.services;

import java.util.ArrayList;
import java.util.List;

import org.jvnet.hk2.annotations.Service;

import net.explorviz.extension.tutorial.model.Tutorial;

@Service
public class TutorialService {

	public List<Tutorial> getAllTutorials() {
		final List<Tutorial> tutorialList = new ArrayList<Tutorial>();
		final Tutorial tutorial = new Tutorial();
		tutorial.setId(2L);
		tutorial.setTitle("Default Tutorial");
		tutorialList.add(tutorial);
		return tutorialList;
	}
}
