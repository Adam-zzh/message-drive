package com.sfbest.financial.eventstore.msg;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MysqlFmsMessageStore {

    private final JdbcOperations jdbcTemplate;

    private int partionCount;

    public MysqlFmsMessageStore(DataSource dataSource){
        this (new JdbcTemplate(dataSource));
    }

    public MysqlFmsMessageStore(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final String DEFAULT_TABLE_PREFIX = "fms_";

    private String tablePrefix = DEFAULT_TABLE_PREFIX;

    public void setPartionCount(int partionCount) {
        this.partionCount = partionCount;
    }


    private enum SQL {
        CREATE_MESSAGE_SQL("INSERT into fms_event_message(message_id,message_key,event_channel,MESSAGE_BYTES,partition_id)values(?,?,?,?,?)"),
        NOT_DEAL_MESSAGE_QUERY("select message_id,event_channel,MESSAGE_BYTES,message_status,create_time,send_count,partition_id,message_key from  fms_event_message where is_deleted=0 and message_status=0 and partition_id=?"),
        PATITION_LOCK_SQL("select partition_id from  fms_event_partiton_lock where partition_id=? for update"),
        UPDATE_MESSAGE_DEAL_SQL("update fms_event_message set send_count=send_count+1,message_status=2 where message_id=? and message_status!=2");

        private final String sql;

        private SQL(String sql){
            this.sql = sql;
        }

        public String getSql(){
            return this.sql;
        }
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public int addMessage(FmsEventMessage message){

        return jdbcTemplate.update(SQL.CREATE_MESSAGE_SQL.getSql(),message.getMessageId(),message.getMessageKey(),message.getEventChannel(),message.getMessageBytes(),message.getPartitionId());
    }


    public  List<FmsEventMessage> queryPartionNotDealMessage(int partitionId){
        jdbcTemplate.query(SQL.PATITION_LOCK_SQL.getSql(),new ColumnMapRowMapper(),partitionId);
        List<FmsEventMessage> list = jdbcTemplate.query(SQL.NOT_DEAL_MESSAGE_QUERY.getSql(),new MessageMapper(),partitionId);
        return list;
    }


    public int updateMessageDealed(String messageId){
        return jdbcTemplate.update(SQL.UPDATE_MESSAGE_DEAL_SQL.getSql(),messageId);
    }


    private class MessageMapper implements RowMapper<FmsEventMessage> {
        @Override
        public FmsEventMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            FmsEventMessage message = new FmsEventMessage();
            message.setPartitionId(rs.getInt("partition_id"));
            message.setMessageKey(rs.getString("message_key"));
            message.setMessageStatus(rs.getInt("message_status"));
            message.setMessageId(rs.getString("message_id"));
            message.setMessageBytes(rs.getBytes("MESSAGE_BYTES"));
            message.setEventChannel(rs.getString("event_channel"));
            return message;
        }
    }

}
