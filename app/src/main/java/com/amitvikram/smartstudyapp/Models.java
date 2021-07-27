package com.amitvikram.smartstudyapp;

public class Models {
    String modelName;
    String modelLink;

    public Models(String modelName, String modelLink) {
        this.modelName = modelName;
        this.modelLink = modelLink;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelLink() {
        return modelLink;
    }

    public void setModelLink(String modelLink) {
        this.modelLink = modelLink;
    }
}
