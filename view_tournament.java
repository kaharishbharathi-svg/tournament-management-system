
import java.sql.*;
import java.util.Scanner;


public class view_tournament {

    public static void main(String[] args) {
         
        String url = "jdbc:mysql://localhost:3306/"; 
        String user = "root";
        String pass = "";
        Scanner en=new Scanner(System.in);
        System.out.println("VIEW TOURNAMENT");
                        
        System.out.println("ENTER TOURNAMENT NAME:");
        String n=en.next();

        try (Connection con = DriverManager.getConnection(url, user, pass);
         Statement stmt = con.createStatement()) 
  {
  
            
                  stmt.executeUpdate("USE "+ n +"");
            //-----------------------------------
           ResultSet rc=stmt.executeQuery("SELECT SUM(match_played) AS total_played FROM tournament_group_stage");
           rc.next();
           int mp=(rc.getInt("total_played"))/2;
               
           ResultSet rs=stmt.executeQuery("SELECT MAX(sn) AS total_played FROM tournament_group_stage");
           rs.next();
           int t=rs.getInt("total_played");
          
           String[] name=new String[t];
           int[] points=new int[t];
           float[] spoints=new float[t];
           int[] won=new int[t];
           int[] draw=new int[t];
           int[] match=new int[t];
           ResultSet rt=stmt.executeQuery("SELECT name,points,spoints,won,draw,match_played FROM tournament_group_stage ORDER BY points DESC, spoints DESC");
           int i=0;
           while(rt.next())
           {
             name[i]=rt.getString("name");
             points[i]=rt.getInt("points");
             spoints[i]=rt.getFloat("spoints");
             won[i]=rt.getInt("won");
             draw[i]=rt.getInt("draw");
             match[i]=rt.getInt("match_played");
             i++;
           }
        
            System.out.println("AFTER MATCH NUMBER"+"\t"+mp);
            System.out.printf("%-5s %-15s %-15s %-15s %-15s %-15s %-15s %n","RANK","NAME","TOTAL MATCHES","MATCHES WON","DRAW","POINTS","SECONDARY POINTS");
            for( i=0;i<t;i++)
            {
             System.out.printf("%-5d %-15s %-15d %-15d %-15d %-15d %-15.2f %n",(i+1),name[i],match[i],won[i],draw[i],points[i],spoints[i]);
            }                      
                           
  }

    catch (SQLException e)
   {
        System.out.println("Read Error: " + e.getMessage());
    }
    
}
}