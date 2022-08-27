package com.stereo.ignitecacheserver.modal;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.sql.Timestamp;

public class Ping {

    @QuerySqlField(index = true)
    private int id;

    @QuerySqlField(index = true)
    private long timestamp;

    public Ping() {
    }

    public Ping(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }
}
