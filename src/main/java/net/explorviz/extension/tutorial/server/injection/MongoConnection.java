package net.explorviz.extension.tutorial.server.injection;

import com.mongodb.MongoClient;

import net.explorviz.shared.config.annotations.Config;

public class MongoConnection extends MongoClient{
	
	@Config("mongo.host")
	@Config("mongo.port")
	public MongoConnection(final String host, final String port) {
		super(host + ":" + port);
	}
	
}
