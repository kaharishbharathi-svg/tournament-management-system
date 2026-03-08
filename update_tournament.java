import java.sql.*;
import java.util.Scanner;



public class update_tournament {

    public static void main(String[] args) {
        
               String url = "jdbc:mysql://localhost:3306/"; 
               String user = "root";
               String pass = "";
               Scanner en= new Scanner(System.in);

               System.out.println("UPDATE TOURNAMENT");
                
               System.out.println("ENTER TOURNAMENT NAME:");
               String n=en.next();

               try (Connection con = DriverManager.getConnection(url, user, pass); Statement stmt = con.createStatement()) 
                  {

                   stmt.executeUpdate("USE "+ n +"");

                   //---------------------

                   ResultSet rs=stmt.executeQuery("SELECT SUM(match_played) AS total_played FROM tournament_group_stage");
                   rs.next();
                   int mp=(rs.getInt("total_played"))/2;


                   ResultSet rt=stmt.executeQuery("SELECT MAX(mn) AS total FROM schedule");
                   rt.next();
                   int tm=rt.getInt("total");
                                                                                               

                   ResultSet mn=stmt.executeQuery("SELECT team1,team2 FROM schedule WHERE mn= "+(mp+1)+" ");
                   mn.next();
                   String t1=mn.getString("team1");
                   String t2=mn.getString("team2");
                   //-------------------

                   System.out.println("MATCH "+(mp+1) +" of "+tm + "\n" +"1."+ t1 +"\t"+"VS" +"\t"+ "2."+ t2);

                //------------------------
                System.out.println("WINNER TEAM:");
                int wt=en.nextInt();
                 
                 System.out.println(t1+"\tPOINTS:");
                 float t1p=en.nextFloat();
                 System.out.println(t2+"\tPOINTS:");
                 float t2p=en.nextFloat();
                  //--------------------
                 //----------------


                  if (wt==1)
                  {
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1, won=won+1,points=points+2,spoints=spoints+ "+t1p+" WHERE name='"+t1+"' ");
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1,spoints=spoints+ "+t2p+" WHERE name='"+t2+"' ");
                  stmt.executeUpdate(" UPDATE schedule  SET winner='"+t1+"',spoints1="+t1p+",spoints2="+t2p+"  WHERE mn="+(mp+1)+" ");
                  }
                  else if(wt==2)
                  {
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1, won=won+1,points=points+2,spoints=spoints+ "+t2p+" WHERE name='"+t2+"' ");
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1,spoints=spoints+ "+t1p+" WHERE name='"+t1+"' ");
                   stmt.executeUpdate(" UPDATE schedule  SET winner='"+t2+"',spoints1="+t1p+",spoints2="+t2p+"  WHERE mn="+(mp+1)+" ");
                  }
                  else if(wt==0)
                  {
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1,draw=draw+1,points=points+1,spoints=spoints+ "+t1p+" WHERE name='"+t1+"' ");
                  stmt.executeUpdate("UPDATE tournament_group_stage SET match_played=match_played+1,draw=draw+1,points=points+1,spoints=spoints+ "+t2p+" WHERE name='"+t2+"' ");
                   stmt.executeUpdate(" UPDATE schedule  SET winner='NO_WINNER',spoints1="+t1p+",spoints2="+t2p+"  WHERE mn="+(mp+1)+" ");
                  }

                  }
                 catch (SQLException e) 
                 {   System.out.println("Setup Error: " + e.getMessage()); }


    }
    
}
