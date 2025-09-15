package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class UserPrivilege{

	@SerializedName("operation_name")
	private String operationName;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("module_id")
	private String moduleId;

	@SerializedName("module")
	private String module;

	@SerializedName("operation_id")
	private String operationId;

	@SerializedName("id")
	private String id;

	@SerializedName("subscription")
	private String subscription;

	@SerializedName("module_name")
	private String moduleName;

	@SerializedName("message")
	private String message;

	@SerializedName("operation")
	private String operation;

	@SerializedName("status")
	private String status;

	public String getOperationName(){
		return operationName;
	}

	public String getUserType(){
		return userType;
	}

	public String getModuleId(){
		return moduleId;
	}

	public String getModule(){
		return module;
	}

	public String getOperationId(){
		return operationId;
	}

	public String getId(){
		return id;
	}

	public String getSubscription(){
		return subscription;
	}

	public String getModuleName(){
		return moduleName;
	}

	public String getMessage(){
		return message;
	}

	public String getOperation(){
		return operation;
	}

	public String getStatus(){
		return status;
	}
}