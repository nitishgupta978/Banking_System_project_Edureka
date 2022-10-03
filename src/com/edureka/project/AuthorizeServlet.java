package com.edureka.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/authorize")
public class AuthorizeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected static Connection initializeDatabase() throws SQLException, ClassNotFoundException{

		Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
        return con;
    }

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//response.getWriter().append("Served at: ").append(request.getContextPath());
	try {
		PrintWriter out = response.getWriter();
		
		Connection con = initializeDatabase();
		Float amount = Float.parseFloat(request.getParameter("amount"));
		
		String sql = "select * from credit_card where card_no = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, request.getParameter("card"));
		System.out.println("amount: "+request.getParameter("amount")+
				" card: "+request.getParameter("card"));
		ResultSet rs = pst.executeQuery();
		
		if(rs.next() && amount > 1) {
			out.println("<p style=font-size:20px;color:yellow;text-align:center>Transaction approved!</p>");
			RequestDispatcher rd = request.getRequestDispatcher("authorize_creditcard_amount.html");
			rd.include(request, response);
		}	
		else {
			out.println("<p style=font-size:20px;color:yellow;text-align:center>Transaction denied, Invalid card ... Try again!</p>");
			RequestDispatcher rd = request.getRequestDispatcher("authorize_creditcard_amount.html");
			rd.include(request, response);
		}
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

}
