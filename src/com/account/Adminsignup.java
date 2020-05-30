package com.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Adminsignup extends HttpServlet 
{
	private final static String connectionURL = "jdbc:postgresql://localhost:5432/Organization";
	private final static String userName = "postgres";
	private final static String password = "aravinth";

	@Override
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
          String userName = req.getParameter("username");
          String password = req.getParameter("password");
          int shopId = Integer.parseInt(req.getParameter("shopId"));
          
          Admin admin=new Admin();
          admin.setUserName(userName);
          admin.setPassword(password);
          admin.setShopId(shopId);
          
         String result = "";
          try {
			result = adminAccess(admin);
          } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
          }
          if(result.equals("success"))
          {
        	  res.sendRedirect("index.html");
          }
          else
          {
        	  res.sendRedirect("index.html");
          }
    }
    
    public String adminAccess(Admin admin) throws ClassNotFoundException, SQLException
	{
		PreparedStatement st;
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from Admin";
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getInt("Orga_id")==(admin.getShopId()))
					{
						flag = false;
						return "already";
					}
			}
			if(flag)
			{
				String query1 = "insert into Admin(orga_id,username,password)values(?,?,?)";
				st = connection.prepareStatement(query1);
				st.setInt(1,admin.getShopId());
				st.setString(2,admin.getUserName());
				st.setString(3,admin.getPassword());
				st.executeUpdate();
				st.close();
		
				return "success";
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
}