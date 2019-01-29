package net.explorviz.extension.tutorial.model;

public class InteractablePlaceholder extends Interactable {

	@Override
	public String getUniqueIdentifier() {
		return "" + System.identityHashCode(this);
	}

}
