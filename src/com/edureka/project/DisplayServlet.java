package com.edureka.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/display")
public class DisplayServlet extends HttpServlet {
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
		Cookie ck[] = request.getCookies();  
		String user = ck[0].getValue();
		
		Connection con = initializeDatabase();
		
		String sql = "select * from statements where Date between ? and ? and username = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, request.getParameter("from"));
		pst.setString(2, request.getParameter("to"));
		pst.setString(3, user);

		ResultSet rs = pst.executeQuery();
		
		out.println("<html><body style="+"background-color:MediumSeaGreen"+"><h2 style=text-align:center>Display Statement</h2> <form action=\"display\" method=\"post\">\r\n"
				+ "	  <label for=\"date\">Date Range:</label>\r\n"
				+ "	  <br>\r\n"
				+ "	  <label for=\"from\">From:</label>\r\n"
				+ "	  <input type=\"date\" id=\"from\" name=\"from\">\r\n"
				+ "	  <label for=\"to\">To:</label>\r\n"
				+ "	  <input type=\"date\" id=\"to\" name=\"to\">\r\n"
				+ "	  <input type=\"submit\" value=\"Display\">\r\n"
				+ "	</form> <br> <br> <table border=1> <tr> <th>Trans_No</th> <th>Date</th> <th>Description</th> <th>Cheque_No/Acc_No</th> <th>Withdraw</th> <th>Deposit</th> <th>Available_Balance</th> </tr>");
		
		while(rs.next()) {
			
			String trans_No = rs.getString("Trans_No");
			String date = rs.getString("Date");
			String desc = rs.getString("Description");
			String cheq = rs.getString("Cheque_No");
			String withdraw = rs.getString("Withdraw");
			String deposit = rs.getString("Deposit");
			String bal = rs.getString("Available_Balance");
			
			out.println("<tr><td>"+ trans_No +"</td><td>"+ date +"</td><td>"+ desc + "</td><td>"+ cheq +"</td><td>"+ withdraw +"</td><td>"+ deposit + "</td><td>"+ bal +"</td></tr>");
		}
		
		out.println("</table></body></html>");
		
		pst.close();
        con.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
