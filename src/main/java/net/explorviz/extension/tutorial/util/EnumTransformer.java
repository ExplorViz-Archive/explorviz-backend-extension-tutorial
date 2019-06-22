package net.explorviz.extension.tutorial.util;

import org.bson.Transformer;

public class EnumTransformer implements Transformer {
	@Override
	public Object transform(final Object o) {
		return o.toString(); // convert any object (including an Enum) to its String representation
	}
}
