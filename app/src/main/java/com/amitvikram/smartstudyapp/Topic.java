package com.amitvikram.smartstudyapp;

public class Topic {
    String topicName;
    String topicDetails;
    String modelLink, modelName;

    public String getModelLink() {
        return modelLink;
    }

    public void setModelLink(String modelLink) {
        this.modelLink = modelLink;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Topic(String topicName, String topicDetails, String modelName, String modelLink) {
        this.topicName = topicName;
        this.topicDetails = topicDetails;
        this.modelName = modelName;
        this.modelLink = modelLink;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDetails() {
        return topicDetails;
    }

    public void setTopicDetails(String topicDetails) {
        this.topicDetails = topicDetails;
    }
}
