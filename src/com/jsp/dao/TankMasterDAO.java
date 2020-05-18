package com.jsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jsp.dto.TankMasterVO;

public class TankMasterDAO {
	private TankMasterDAO() {
	}
	
	private static TankMasterDAO instance = new TankMasterDAO();
	
	public static TankMasterDAO getInstance() {
		return instance;
	}

	Connection getConnection(){
		Connection conn = null;
		Context initContext;
		
		try{
			initContext = new InitialContext();
			DataSource ds = (DataSource)initContext.lookup("java:/comp/env/jdbc/myoracle");
			conn = ds.getConnection();
			
		}catch(NamingException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public int userCheck(String userid, String pwd, String lev){
		int result=1;
		Connection conn = null;
		String sql="SELECT * FROM employees WHERE id =?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn=getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				if(pwd.equals(rs.getString("PASS"))){
					if(lev.equals(rs.getString("LEV"))){
						result = 2; 
						if(lev.equals("B")){
							result=3;
						}
					}else{
						result =1;
					}
				}else{  
					result = 0;
				}
			}else{  
				result = -1;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public TankMasterVO getMember(String id){
		TankMasterVO member = null;
		
		Connection conn = null;
		String sql="select * from employees where id=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn=getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				member = new TankMasterVO();
				member.setId(rs.getString("ID"));
				member.setPass(rs.getString("PASS"));
				member.setName(rs.getString("NAME"));
				member.setLev(rs.getString("LEV"));				
				member.setEnter(rs.getDate("ENTER"));
				member.setGender(rs.getInt("GENDER"));
				member.setPhone(rs.getString("PHONE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return member;
	}
	
	public void insertMember(TankMasterVO member) {
		String sql = "insert into employees values(?,?,?,?,SYSDATE,?,?)";		
		Connection conn = null;

		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPass());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getLev());			
			pstmt.setInt(5, member.getGender());
			pstmt.setString(6, member.getPhone());
			System.out.println(pstmt.executeUpdate());			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int updateMember(TankMasterVO tVo) {
		int result = -1;
		String sql = "update employees set gender=?, pass=?, name=?, lev=?, phone=? where id=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tVo.getGender());
			pstmt.setString(2, tVo.getPass());
			pstmt.setString(3, tVo.getName());
			pstmt.setString(4, tVo.getLev());
			pstmt.setString(5, tVo.getPhone());
			pstmt.setString(6, tVo.getId());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}	
}
