package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Dtos.MessageDto;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {

    public Message createMessage(MessageDto messageDto){
        Message savedMessage = null;

        try{
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?);",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,messageDto.getPosted_by());
            ps.setString(2,messageDto.getMessage_text());
            ps.setLong(3, messageDto.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet pKResultSet = ps.getGeneratedKeys();
            if(pKResultSet.next()){
                int generatedPKey = (int) pKResultSet.getLong(1);
                savedMessage = new Message(generatedPKey,messageDto.getPosted_by(),messageDto.getMessage_text(), messageDto.getTime_posted_epoch());
            }


        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

        return savedMessage;
    }



    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){ 
                messages.add(new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getInt("time_posted_epoch")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Message dbMessage = null;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message WHERE message_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
               dbMessage = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getInt("time_posted_epoch"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return dbMessage;
    }

    public int DeleteMessage(int id){
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM message WHERE message_id = ?");
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return 0;
    }



    public List<Message> getAccountMessages(int id) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message WHERE posted_by = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){ 
                messages.add(new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getInt("time_posted_epoch")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
        
    }



    public int updateMessage(int id, MessageDto messageDto) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            ps.setString(1, messageDto.getMessage_text());
            ps.setInt(2, id);
            return ps.executeUpdate();
            
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        return 0;
    }
}
