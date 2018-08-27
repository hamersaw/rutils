package com.bushpath.rutils.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NoaaRecord {
    protected Long startTime;    
    protected Long endTime;    

    protected Float latitude;
    protected Float longitude;

    protected Float upperLatitude;
    protected Float lowerLatitude;
    protected Float upperLongitude;
    protected Float lowerLongitude;

    protected Map<String, Object> attributes;
    protected Map<String, Object[][]> features;

    protected String storageNodeId;
    protected String physicalGraphPath;

    public NoaaRecord() {
        this.attributes = new HashMap();
        this.features = new HashMap();
    }

    /**
     * temporal properties
     */

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean hasTemporalProperties() {
        return this.startTime != null && this.endTime != null;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    /**
     * spatial coordinates properties
     */

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public boolean hasSpatialCoordinates() {
        return this.latitude != null && this.longitude != null;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    /**
     * spatial range properties
     */

    public void setUpperLatitude(float upperLatitude) {
        this.upperLatitude = upperLatitude;
    }

    public void setLowerLatitude(float lowerLatitude) {
        this.lowerLatitude = lowerLatitude;
    }

    public void setUpperLongitude(float upperLongitude) {
        this.upperLongitude = upperLongitude;
    }

    public void setLowerLongitude(float lowerLongitude) {
        this.lowerLongitude = lowerLongitude;
    }

    public boolean hasSpatialRange() {
        return this.upperLatitude != null && this.lowerLatitude != null
            && this.upperLongitude != null && this.lowerLongitude != null;
    }

    public Float getUpperLatitude() {
        return this.upperLatitude;
    }

    public Float getLowerLatitude() {
        return this.lowerLatitude;
    }

    public Float getUpperLongitude() {
        return this.upperLongitude;
    }

    public Float getLowerLongitude() {
        return this.lowerLongitude;
    }

    /**
     * attributes
     */

    public void putAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public Set<String> getAttributeNames() {
        return this.attributes.keySet();
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    } 

    /**
     * TODO - features
     */

    /**
     * runtime metadata
     */

    public void setStorageNodeId(String storageNodeId) {
        this.storageNodeId = storageNodeId;
    }

    public String getStorageNodeId() {
        return storageNodeId;
    }

    public void setPhysicalGraphPath(String physicalGraphPath) {
        this.physicalGraphPath = physicalGraphPath;
    }

    public String getPhysicalGraphPath() {
        return physicalGraphPath;
    }
}
