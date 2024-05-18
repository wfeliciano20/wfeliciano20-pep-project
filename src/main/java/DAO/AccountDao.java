package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Dtos.AccountDto;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {
    public Account registerAccount(AccountDto accountDto){
        Account savedAccount = null;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO account(username, password) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,accountDto.getUsername());
            ps.setString(2, accountDto.getPassword());
            ps.executeUpdate();

            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int generatedPKey = (int) pKeyResultSet.getLong(1);
                savedAccount = new Account(generatedPKey, accountDto.getUsername(), accountDto.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedAccount;
    }

    public boolean accountExists(String username){

        boolean exists = true;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                exists = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    public boolean accountExists(int id){

        boolean exists = true;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM account WHERE account_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                exists = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    public Account getAccountByUsername(String username){
        Account dbAccount = null;
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                dbAccount = new Account(rs.getInt("account_id"),rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbAccount;
    }
}
