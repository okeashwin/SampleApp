package com.app.model;


import java.sql.Time;
import java.sql.Timestamp;

public class RecordEntry {
    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private float cpuUsage;
    private float memoryUsage;
    private float diskUsage;

    public void setId(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime=startTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime=endTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage=cpuUsage;
    }
    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setMemoryUsage(float memoryUsage) {
        this.memoryUsage=memoryUsage;
    }
    public float getMemoryUsage() {
        return memoryUsage;
    }

    public void setDiskUsage(float diskUsage) {
        this.diskUsage=diskUsage;
    }
    public float getDiskUsage() {
        return diskUsage;
    }
}