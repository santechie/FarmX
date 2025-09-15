package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class ObjectItem {

	@SerializedName("user_type")
	private String userType;

	@SerializedName("content_id")
	private String contentId;

	@SerializedName("id")
	private String id;

	@SerializedName("subscription")
	private String subscription;

	@SerializedName("message")
	private String message;

	@SerializedName("object_id")
	private String objectId;

	@SerializedName("status")
	private String status;

	public String getUserType(){
		return userType;
	}

	public String getContentId(){
		return contentId;
	}

	public String getId(){
		return id;
	}

	public String getSubscription(){
		return subscription;
	}

	public String getMessage(){
		return message;
	}

	public String getObjectId(){
		return objectId;
	}

	public String getStatus(){
		return status;
	}
}