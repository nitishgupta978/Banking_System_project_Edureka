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

@WebServlet("/operation")
public class PerformOperationServlet extends HttpServlet {
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
		PreparedStatement pst1 = con.prepareStatement("insert into statements (Date, Description, Cheque_No, Withdraw, Deposit, Available_Balance, Username) values(?, ?, ?, ?, ?, ?, ?)");
        
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		String accnumber = request.getParameter("accnumber");
		String description = null;
		Float withdraw = 0f, deposit = 0f, balance = 0f, solde = 0f;
		Float amount = Float.parseFloat(request.getParameter("amount"));
		
		String sql = "select Available_Balance from statements where Username = (select Username from account_details where Account_Number = ? ) and Date = (select max(Date) from statements) and Trans_No = (select max(Trans_No) from statements)"; 
		PreparedStatement pst2 = con.prepareStatement(sql);
		pst2.setString(1, accnumber);
		ResultSet rs = pst2.executeQuery();
		
		if(request.getParameter("withdraw") != null) {
			description = "Withdraw from ATM";
			withdraw = amount;
			if(rs.next())
				balance = Float.parseFloat(rs.getString("Available_Balance"));
				
			solde = balance - withdraw;
			
		} else if(request.getParameter("deposit") != null) {
			description = "Deposit";
			deposit = amount;
			if(rs.next())
				balance = Float.parseFloat(rs.getString("Available_Balance"));
				
			solde = balance + deposit;
		} 
		
        pst1.setDate(1, sqlDate);
        pst1.setString(2, description);
        pst1.setString(3, accnumber);
        pst1.setFloat(4, withdraw);
        pst1.setFloat(5, deposit);
        pst1.setFloat(6, solde);
        pst1.setString(7, user);
        
        // Execute the insert command using executeUpdate() to make changes in database
        int count = pst1.executeUpdate();
        
        if( request.getParameter("withdraw")!= null && count!=0)   
            out.println("<html><body><b>Amount Debited Successfully!" + "</b></body></html>");
        else if(request.getParameter("deposit") != null && count!=0)
        	out.println("<html><body><b>Amount Credited Successfully!" + "</b></body></html>");
        else 
        	out.println("<html><body><b>Transaction denied!" + "</b></body></html>");
        // Close all the connections
        pst1.close();
        pst2.close();
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
