package com.np.hrms.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import com.np.hrms.auth.PropertyPlaceholder;
import com.np.hrms.dto.UserInfo;
import com.np.hrms.entities.User;

@Repository
public class SqlDAO {
	

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PropertyPlaceholder env;
	
	public List<UserInfo> getUsers() {
		String sqlQuery = "select u.id, u.user_id, u.role, u.user_key, cast(aes_decrypt(password, '"+ env.SECRET_KEY + "') as char),"
				+ " u.username,  u.email_id, u.designation, u.report_manager,u.contact_no, u.profile_pic_url, u.gender"
				+ " from user u where u.active=true";
		
		
		
		List<UserInfo> users = jdbcTemplate.query(sqlQuery, new ResultSetExtractor<List<UserInfo>>(){
 
			public List<UserInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<UserInfo> usersList = new ArrayList<UserInfo>();
				while (rs.next()){
					UserInfo user = new UserInfo();
					user.setId(rs.getString(1));
					user.setUserId(rs.getString(2));
					user.setRole(rs.getString(3));
					user.setKey(rs.getString(4));
					user.setPassword(rs.getString(5));
					user.setUsername(rs.getString(6));
					user.setEmail_id(rs.getNString(7));
					user.setDesignation(rs.getNString(8));
					user.setReportManager(rs.getNString(9));
                    user.setContactNo(rs.getLong(10));
                    user.setProfilePicUrl(rs.getNString(11));
                    user.setGender(rs.getNString(12));
					usersList.add(user);
					
			}
			
				return usersList;
			}
			
	});
		return users;		
	
}		
	
	// For the Registration of the New User 
	public int saveUser(User user) {
        String query = "INSERT INTO user (id, user_id, username, password, designation, department, role, user_key, contact_no,"
        		+ "email_id, report_manager,birth_date, aniversary_date, profile_pic_url, gender,active) " +
                "VALUES (?, ?, ?, aes_encrypt(?,?), ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?) ";
       
     int    insertData = jdbcTemplate.update(query , user.getId(), user.getUserId(), user.getUsername(), user.getPassword(),env.SECRET_KEY,
    		user.getDesignation(), user.getDeparment(), user.getRole(), user.getUserKey(),
        	user.getContactNo(), user.getEmailId(), user.getReportManager(), user.getBirthDate(),
        	user.getAniversaryDate(), user.getProfilePicUrl(), user.getGender(), user.isActive());
       return insertData;
	}
}
	
