package com.ascentya.AsgriV2.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ObjectAccess{

	@SerializedName("name")
	private String name;

	@SerializedName("operation_id")
	private String operationId;

	@SerializedName("id")
	private String id;

	@SerializedName("message")
	private String message;

	@SerializedName("value")
	private String value;

	@SerializedName("accesses")
	private List<ObjectItem> accesses;

	@SerializedName("status")
	private String status;

	public String getName(){
		return name;
	}

	public String getOperationId(){
		return operationId;
	}

	public String getId(){
		return id;
	}

	public String getMessage(){
		return message;
	}

	public String getValue(){
		return value;
	}

	public List<ObjectItem> getAccesses(){
		return accesses;
	}

	public String getStatus(){
		return status;
	}
}