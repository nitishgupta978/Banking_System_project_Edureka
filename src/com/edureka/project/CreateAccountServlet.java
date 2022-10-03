package com.edureka.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/create_account")
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected static Connection initializeDatabase() throws SQLException, ClassNotFoundException{

			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
	        return con;
	    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			
			Connection con = initializeDatabase();
			PreparedStatement pst = con.prepareStatement("insert into account_details (Name, DOB, Address, Email_ID, Account_Type, Username,Account_Number) values(?, ?, ?, ?, ?, ?,?)");
  
			
			
            //
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = formatter.parse(request.getParameter("dob"));
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            
            pst.setString(1, request.getParameter("name"));
            pst.setDate(2, sqlDate);
            pst.setString(3, request.getParameter("address"));
            pst.setString(4, request.getParameter("email"));
            pst.setString(5, request.getParameter("type"));
            pst.setString(6, request.getParameter("name"));
            pst.setString(7, randomAccountNo());
  
            // Execute the insert command using executeUpdate()
            // to make changes in database
            int count = pst.executeUpdate();
            if(count!=0) {
                PrintWriter out = response.getWriter();
                out.println("<html><body><b>Account created Successfully!" + "</b></body></html>");
            } else {
            	PrintWriter out = response.getWriter();
            	out.println("<html><body><b>User Account is not created!" + "</b></body></html>");
            }
            // Close all the connections
            pst.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	static String randomAccountNo() {
		return String.valueOf((new Date()).getTime());
	}
	
}
