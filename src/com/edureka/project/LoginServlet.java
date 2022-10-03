package com.edureka.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

private static PreparedStatement pst;
	
	public void init(ServletConfig config) throws ServletException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
			
			String sql = "select * from users where username = ? and password = ?";
			pst = con.prepareStatement(sql);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			pst.setString(1, user);
			pst.setString(2, password);
			
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				//store the username in a cookie
				Cookie c1 = new Cookie("username", user);		
				c1.setMaxAge(600000000); 	//1s0000000000000000
				response.addCookie(c1);
				RequestDispatcher rd = request.getRequestDispatcher("menu.html");
				rd.forward(request, response);
				
				
			}else {
				PrintWriter out = response.getWriter();
				out.println("<p style=font-size:20px;color:yellow;text-align:center>Invalid Username/Password ... Try again</p>");
				RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.include(request, response);
			}
			
			Cookie[] cookies = request.getCookies();
			 
			if(cookies!=null)
			for (Cookie aCookie : cookies) {
			    String name = aCookie.getName();
			    String value = aCookie.getValue();
			 
			    System.out.println("Inside com.edureka.project.LoginServlet.java\n" + name + " = " + value);
			}
				
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}