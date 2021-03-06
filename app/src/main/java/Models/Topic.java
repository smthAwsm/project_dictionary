package models;

import com.orm.SugarRecord;

/**
 * Created by XPS on 4/17/2016.
 */
public class Topic extends SugarRecord {

    private long dictionaryID;
    private String topicName;
    private long imageRecourceID;

    public Topic(){}

    public Topic(long dictionaryIdValue,String name, long recourceID ){
        dictionaryID = dictionaryIdValue;
        topicName = name;
        imageRecourceID = recourceID;
    }

    public String getTopicName() {
        return topicName;
    }

    public long getImageRecourceID() {
        return imageRecourceID;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setImageRecourceID(long imageRecourceID) {
        this.imageRecourceID = imageRecourceID;
    }
}
