package seerta;
import java.sql.*;
import java.text.*;
/**
 *
 * @author Roshan Chaudhary
 */
public class ConnectDB {

	private String url,db,driver,user,pass;
	Connection con;
	ResultSet rst;
	PreparedStatement pstmt;
	Statement stmt;
        String emailDomain;
        String sysPass;
        String emailID;
        String emailPasss;

        public ConnectDB(char inputChar){
		
        System.out.println("Database Connection Created\n");   
        url="jdbc:mysql://localhost:3306/";
        db="seerta system";
        driver="com.mysql.jdbc.Driver";
        user="root";
        pass=""; //password could be added
        con=null;
        rst=null;
        pstmt=null;
        stmt=null;
      
        String userInput= Character.toString(inputChar);

        try{
			Class.forName(driver);
			con=DriverManager.getConnection(url+db,user,pass);
			try{
				stmt=con.createStatement();
                                try{
                                rst=stmt.executeQuery("SELECT Email_Domain, User_Access_Password"
                                        + ", Email_ID, Email_Password FROM Seerta_User_Information WHERE User_Access_Password "
                                        + " = '"+ userInput + "'");
                            }
                                catch(Exception e){
                                    System.out.println(" error in syntax: "+ e);
                                }

				while(rst.next()){
                                    emailDomain = rst.getString("Email_Domain");
                                    sysPass = rst.getString("User_Access_Password");
                                    emailID = rst.getString("Email_ID");
                                    emailPasss = rst.getString("Email_Password");
                                    System.out.println("Username="+emailID+" "+"Password= "+emailPasss+" "+"Email Domain= "+emailDomain+"");
                                    new ReadEmail (emailDomain, emailID, emailPasss);
				}

			}catch(SQLException s){

                            System.out.println(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(SQLException s){}
		}
		
	}
}
