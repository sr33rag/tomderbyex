package zetcode;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SelectAllCars extends HttpServlet {

  private Logger logger = Logger.getLogger(SelectAllCars.class.getName());
  private DataSource ds=null;
  private Context ctx=null;
  
  public void init() {
    try {
	  ctx = new InitialContext();
	  ds = (DataSource) ctx.lookup("java:comp/env/jdbc/carsdb");
    } catch (Exception ex) {
	  logger.log(Level.SEVERE,ex.getMessage(),ex);
	}
  }

  protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("text/html;charset=UTF-8");
    PrintWriter out=null;
    Connection con=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    try {
      out = res.getWriter();      
      con=ds.getConnection();
      ps = con.prepareStatement("SELECT c.id, c.name, c.price FROM cars c");
      rs=ps.executeQuery();
      out.println("<html>");
      out.println("<head><title>SimpleServlet</title></head>");
      out.println("<body><table>");
      out.println("<tr><td><b>Car Id</b></td><td><b>Car Name</b></td><td><b>Car Price</b></td></tr>");
      while(rs.next()) {
        out.println("<tr>");
        out.println("<td>"+rs.getInt(1)+"</td>");
        out.println("<td>"+rs.getString(2)+"</td>");
        out.println("<td>"+rs.getString(3)+"</td>");
        out.println("</tr>");
      }
      out.println("</table></body>");
      out.println("</html>");
    } catch(Exception ex) {     
      //logger.log(Level.SEVERE,ex.getMessage(),ex);
      ex.printStackTrace();
    } finally {
      try {
        if(rs!=null)
          rs.close();
        if(ps!=null)
          rs.close();
        if(con!=null)
          con.close();
      } catch(SQLException sqlex) {
        //logger.log(Level.WARNING, sqlex.getMessage(),sqlex);
        sqlex.printStackTrace();
      }
    }
  }
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    processRequest(req, res);
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    processRequest(req, res);
  }  
}

