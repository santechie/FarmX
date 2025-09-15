package com.ascentya.AsgriV2.utility.model;

import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;

import androidx.annotation.DrawableRes;

public class Utility {

    private int image;
    private Constants.TestType testType;
    private Class activityClass;
    private Components.Component component;

    public Utility(@DrawableRes int image, Constants.TestType testType, Class activityClass, Components.Component component){
        setImage(image);
        setTestType(testType);
        setActivityClass(activityClass);
        this.component = component;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public Constants.TestType getTestType() {
        return testType;
    }

    public void setTestType(Constants.TestType testType) {
        this.testType = testType;
    }

    public Components.Component getComponent() {
        return component;
    }
}
