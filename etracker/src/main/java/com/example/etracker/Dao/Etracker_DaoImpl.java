package com.example.etracker.Dao;


import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.etracker.Model.User;





@Repository
public class Etracker_DaoImpl implements Etracker_Dao {
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	PasswordAuthentication passwordAuthentication=new PasswordAuthentication();
	
    private final String G1 = "SELECT SUM(CASE WHEN TRANSACTION_TYPE=0 THEN amount END) AS TotalExpense, SUM(CASE WHEN TRANSACTION_TYPE=1 THEN amount END) AS TotalIncome FROM T_TRANSACTION WHERE year(TRANSACTION_DATE) = year( current_date()) and USER_ID= ? ";
    private final String G2 = "SELECT SUM(CASE WHEN TRANSACTION_TYPE=0 THEN amount END) AS TotalExpense, SUM(CASE WHEN TRANSACTION_TYPE=1 THEN amount END) AS TotalIncome FROM T_TRANSACTION WHERE year(TRANSACTION_DATE) = year( current_date()) and month(TRANSACTION_DATE) = month(current_date())and USER_ID=?";
    private final String G3 = "SELECT monthname(TRANSACTION_DATE) as Month, SUM(CASE WHEN TRANSACTION_TYPE=0 THEN amount END) AS TotalExpense, SUM(CASE WHEN TRANSACTION_TYPE=1 THEN amount END) AS TotalIncome FROM T_TRANSACTION WHERE year(TRANSACTION_DATE) = year(current_date()) and USER_ID=? AND TRANSACTION_TYPE =0 GROUP BY monthname(TRANSACTION_DATE)";
    private final String G4 = "SELECT dayofmonth(TRANSACTION_DATE) as Day, SUM(CASE WHEN TRANSACTION_TYPE=0 THEN amount END) AS TotalExpense, SUM(CASE WHEN TRANSACTION_TYPE=1 THEN amount END) AS TotalIncome FROM T_TRANSACTION WHERE (month(TRANSACTION_DATE) = month(current_date()) and year(TRANSACTION_DATE) = year(current_date()) and USER_ID=? and TRANSACTION_TYPE =0) GROUP BY dayofmonth(TRANSACTION_DATE)";
    private final String ADDINCOME = "INSERT INTO T_TRANSACTION(TRANSACTION_TYPE,USER_ID,ITEM,CATEGORY_ID,AMOUNT,TRANSACTION_DATE) VALUES (1,?,?,?,?,?)";
    private final String ADDEXPENSE = "INSERT INTO T_TRANSACTION(TRANSACTION_TYPE,USER_ID,ITEM,CATEGORY_ID,AMOUNT,TRANSACTION_DATE) VALUES (0,?,?,?,?,?)";
    private final String HME = "SELECT C.CATEGORY_NAME, SUM(AMOUNT) SUMAMOUNT FROM T_TRANSACTION T INNER JOIN T_CATEGORY C ON T.CATEGORY_ID = C.ID WHERE T.TRANSACTION_TYPE = 0 AND T.USER_ID = ? AND MONTH(T.TRANSACTION_DATE) = month(current_date()) AND YEAR(T.TRANSACTION_DATE) = year(current_date()) GROUP BY C.CATEGORY_NAME"; 
    private final String HYE = "SELECT C.CATEGORY_NAME, SUM(AMOUNT) SUMAMOUNT FROM T_TRANSACTION T INNER JOIN T_CATEGORY C ON T.CATEGORY_ID = C.ID WHERE T.TRANSACTION_TYPE = 0 AND T.USER_ID = ? AND YEAR(T.TRANSACTION_DATE) = year(current_date()) GROUP BY C.CATEGORY_NAME"; 

    private final String LCE = "SELECT * FROM T_CATEGORY WHERE TRANSACTION_TYPE = 0";
    private final String LCI = "SELECT * FROM T_CATEGORY WHERE TRANSACTION_TYPE = 1";
    private final String FETCH_INCOME = "SELECT T_TRANSACTION.ITEM AS ITEM,T_TRANSACTION.AMOUNT AS AMOUNT, T_TRANSACTION.TRANSACTION_DATE AS DATE, T_CATEGORY.CATEGORY_NAME AS CATEGORY FROM T_TRANSACTION JOIN T_CATEGORY ON T_TRANSACTION.CATEGORY_ID=T_CATEGORY.ID WHERE T_TRANSACTION.TRANSACTION_TYPE=0 AND T_TRANSACTION.USER_ID= ? ORDER BY T_TRANSACTION.TRANSACTION_DATE DESC";
    private final String FETCH_EXPENSE = "SELECT T_TRANSACTION.ITEM AS ITEM,T_TRANSACTION.AMOUNT AS AMOUNT, T_TRANSACTION.TRANSACTION_DATE AS DATE, T_CATEGORY.CATEGORY_NAME AS CATEGORY FROM T_TRANSACTION JOIN T_CATEGORY ON T_TRANSACTION.CATEGORY_ID=T_CATEGORY.ID WHERE T_TRANSACTION.TRANSACTION_TYPE=1 AND T_TRANSACTION.USER_ID= ? ORDER BY T_TRANSACTION.TRANSACTION_DATE DESC";
    private final String FETCH_INCOME_EXPENSE = "SELECT T_TRANSACTION.ITEM AS ITEM,T_TRANSACTION.AMOUNT AS AMOUNT, T_TRANSACTION.TRANSACTION_DATE AS DATE, T_CATEGORY.CATEGORY_NAME AS CATEGORY FROM T_TRANSACTION JOIN T_CATEGORY ON T_TRANSACTION.CATEGORY_ID=T_CATEGORY.ID WHERE T_TRANSACTION.USER_ID= ? ORDER BY T_TRANSACTION.TRANSACTION_DATE DESC";
	private final String update_sql="UPDATE T_USER SET PASSWORD=? WHERE EMAILID=?";

 
    
    public Map<String, java.lang.Object> graph1(int uSER_ID) {
		
		return jdbcTemplate.queryForMap(G1,uSER_ID);
	}
    
	public Map<String, java.lang.Object> graph2(int uSER_ID) {
		
		return jdbcTemplate.queryForMap(G2,uSER_ID);
	}
	
    public Collection<Map<String,java.lang.Object>> graph3(int uSER_ID) {
		
		return jdbcTemplate.queryForList(G3,uSER_ID);
	}
    
    public Collection<Map<String,java.lang.Object>> graph4(int uSER_ID) {
		
		return jdbcTemplate.queryForList(G4,uSER_ID);
	}

	public void addincome(int uSER_ID, String iTEM, int cATEGORY_ID, double aMOUNT, String tRANSACTION_DATE) {
		 Object[] incomearray = {uSER_ID,  iTEM,  cATEGORY_ID,  aMOUNT,tRANSACTION_DATE};
		 int[] incometype = {Types.BIGINT,Types.VARCHAR,Types.BIGINT,Types.DOUBLE,Types.DATE};
		 jdbcTemplate.update(ADDINCOME,incomearray,incometype);
		
	}
	public void addexpense(int uSER_ID, String iTEM, int cATEGORY_ID, double aMOUNT, String tRANSACTION_DATE) {
		 Object[] expensearray = {uSER_ID,  iTEM,  cATEGORY_ID,  aMOUNT,tRANSACTION_DATE};
		 int[] expensetype = {Types.BIGINT,Types.VARCHAR,Types.BIGINT,Types.DOUBLE,Types.DATE};
		 jdbcTemplate.update(ADDEXPENSE,expensearray,expensetype);
		
	}

	public List<Map<String, Object>> monthlycategorysum(int uSER_ID) {
		return jdbcTemplate.queryForList(HME,uSER_ID);

	}

	public List<Map<String, Object>> yearlycategorysum(int uSER_ID) {
		return jdbcTemplate.queryForList(HYE,uSER_ID);

	}
	
	

	public Collection<Map<String, Object>> getIncome(int uSER_ID) {
		return jdbcTemplate.queryForList(FETCH_INCOME, uSER_ID);
	}


	@Override
	public Collection<Map<String, Object>> getExpense(int uSER_ID) {
		return jdbcTemplate.queryForList(FETCH_EXPENSE, uSER_ID);
	}


	@Override
	public Collection<Map<String, Object>> getIncomeExpense(int uSER_ID) {
		return jdbcTemplate.queryForList(FETCH_INCOME_EXPENSE, uSER_ID);
	}
	

	@Override
	public int addUser(long Id, String Name, String Email_Id, String Password ) {
		String password =Password;
		System.out.println(Password);
		char[] ch = new char[password.length()]; 
		for (int i = 0; i < password.length(); i++) { 
            ch[i] = password.charAt(i); 
        } 
		String pass=passwordAuthentication.hash(ch);
		System.out.println("Hashed Password = " + pass);
		
		String sql="INSERT INTO T_USER (EMAILID, NAME,PASSWORD) VALUES(?,?,?)";
		int update = jdbcTemplate.update(sql,Email_Id,Name,pass);
		if(update==1) {
			System.out.println("User is created");
		}
		return 1;
		
	}



	


	@Override
	public List<User> selectUser(String Email_Id, String Password) {
		String check_sql = "SELECT EXISTS(SELECT * from T_USER WHERE EMAILID=?)";
		Object[] input = new Object[] {Email_Id};
		int status = jdbcTemplate.queryForObject(check_sql,input,Integer.class);
		
		if(status==1) {
			String get_pass_sql = "SELECT PASSWORD FROM T_USER WHERE EMAILID=?";
			Object[] in = new Object[] {Email_Id};
			String dbPass=jdbcTemplate.queryForObject(get_pass_sql,in, String.class);
			
			char[] ch = new char[Password.length()]; 
			for (int i = 0; i < Password.length(); i++) { 
	            ch[i] = Password.charAt(i); 
	        } 
			String pass=passwordAuthentication.hash(ch);
			System.out.println("Hashed Password = " + pass);
			
			boolean check = passwordAuthentication.authenticate(ch, dbPass);
			System.out.println("Check similarity = " + check);
			if(!check) {
				
				return Collections.emptyList();
			}
			else {
				String fetch_sql="SELECT ID,NAME FROM T_USER WHERE EMAILID=?";
				Object[] inputs = new Object[] {Email_Id};
				return jdbcTemplate.query(fetch_sql,inputs, new RegisterMapper());
			}
		}
		else {
			return Collections.emptyList();
		}
	}

	



	@Override
	public int resetPassword(String EmailId, String Password) {
		
		System.out.println(EmailId+","+Password);
		String update_sql="UPDATE T_USER SET PASSWORD=? WHERE EMAILID=?";
		Object[] inputs = new Object[] {Password,EmailId};
		int result= jdbcTemplate.update(update_sql,inputs);
		return result;
	}

	@Override
	public List<Map<String, Object>> liscategoryexpense() {
		return jdbcTemplate.queryForList(LCE);

	}

	@Override
	public List<Map<String, Object>> liscategoryincome() {
		return jdbcTemplate.queryForList(LCI);

	}
}


class RegisterMapper implements RowMapper<User>{
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User register = new User();
		register.setId(rs.getLong("ID"));
		register.setName(rs.getString("NAME"));
		return register;
	}


}
