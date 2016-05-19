package com.app.dao;


import com.app.model.RecordEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordEntryDAOImpl implements RecordEntryDAO {
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = Logger.getLogger(RecordEntryDAOImpl.class.getName());

    public RecordEntryDAOImpl() {
    }

    public RecordEntryDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveOrUpdate(RecordEntry recordEntry) {

        String query = "INSERT INTO record (id, startTime, endTime, cpuUsed, memoryUsed, diskUsed)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query, recordEntry.getId(), recordEntry.getStartTime(), recordEntry.getEndTime(), recordEntry.getCpuUsage(), recordEntry.getMemoryUsage(), recordEntry.getDiskUsage());
        } catch (Exception e) {
            logger.log(Level.WARNING, "An exception in saveOrUpdate() method");
        }
    }

    @Override
    public RecordEntry get(Timestamp timestamp) {
        String query="SELECT * FROM record where startTime >='"+timestamp.toString()+"' AND endTime<='"+timestamp.toString()+"'";
        logger.log(Level.INFO, "Query generated =                                                                                                               "+query);
        List<RecordEntry> recordsList = jdbcTemplate.query(query, new RowMapper<RecordEntry>() {
            @Override
            public RecordEntry mapRow(ResultSet rs, int arg1) throws SQLException {
                RecordEntry recordTuple = new RecordEntry();
                recordTuple.setId(Integer.parseInt(rs.getString("id")));
                recordTuple.setStartTime(rs.getTimestamp("startTime"));
                recordTuple.setEndTime(rs.getTimestamp("endTime"));
                recordTuple.setCpuUsage(rs.getFloat("cpuUsed"));
                recordTuple.setDiskUsage(rs.getFloat("memoryUsed"));
                recordTuple.setMemoryUsage(rs.getFloat("diskUsed"));

                return recordTuple;
            }
            });

        return recordsList.size()>0?recordsList.get(0):null;
    }
}
