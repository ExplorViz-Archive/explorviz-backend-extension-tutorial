package net.explorviz.extension.tutorial.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Id;

import net.explorviz.shared.landscape.model.helper.BaseEntity;
import xyz.morphia.annotations.Transient;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
public class BaseEntityMorphia extends BaseEntity{

	  /*
	   * This attribute can be used by extensions to insert custom properties to any meta-model object.
	   * Non primitive types (your custom model class) must be annotated with type annotations, e.g., as
	   * shown in any model entity
	   */
	  

}
