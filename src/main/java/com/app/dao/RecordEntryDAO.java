package com.app.dao;


import com.app.model.RecordEntry;

import java.sql.Timestamp;

public interface RecordEntryDAO {

    public void saveOrUpdate(RecordEntry recordEntry);

    public RecordEntry get(Timestamp timestamp);
}
