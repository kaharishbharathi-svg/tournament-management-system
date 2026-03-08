import java.sql.*;
import java.util.Scanner;



public class tournament_creation {

public static String[][] generateSchedule(
        int numberOfTeams,
        int timesEachPlay,
        String[] teamNames) {

    class Match {
        int a, b;
        Match(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    if (teamNames.length != numberOfTeams) {
        throw new IllegalArgumentException("Team names count must match numberOfTeams");
    }

    java.util.List<Match> allMatches = new java.util.ArrayList<>();

    // Generate matches for required cycles
    for (int t = 0; t < timesEachPlay; t++) {
        for (int i = 0; i < numberOfTeams; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {

                if (t % 2 == 0)
                    allMatches.add(new Match(i, j));
                else
                    allMatches.add(new Match(j, i));
            }
        }
    }

    int totalMatches = allMatches.size();

    String[] teamA = new String[totalMatches];
    String[] teamB = new String[totalMatches];

    int[] matchCount = new int[numberOfTeams];
    Match lastMatch = null;

    int index = 0;

    while (!allMatches.isEmpty()) {

        Match bestMatch = null;
        int minScore = Integer.MAX_VALUE;

        for (Match m : allMatches) {

            // Avoid consecutive team
            if (lastMatch != null &&
               (m.a == lastMatch.a || m.a == lastMatch.b ||
                m.b == lastMatch.a || m.b == lastMatch.b)) {
                continue;
            }

            int score = matchCount[m.a] + matchCount[m.b];

            if (score < minScore) {
                minScore = score;
                bestMatch = m;
            }
        }

        if (bestMatch == null) {
            bestMatch = allMatches.get(0);
        }

        teamA[index] = teamNames[bestMatch.a];
        teamB[index] = teamNames[bestMatch.b];

        matchCount[bestMatch.a]++;
        matchCount[bestMatch.b]++;

        lastMatch = bestMatch;
        allMatches.remove(bestMatch);

        index++;
    }

    return new String[][] { teamA, teamB };
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

     public static void main(String[] args) {

     String url = "jdbc:mysql://localhost:3306/"; 
     String user = "root";
     String pass = "";

     Scanner en= new Scanner(System.in);

     System.out.println("CREATE TOURNAMENT");
      
     System.out.println("CREATE TOURNAMENT NAME:");
     String n=en.next();
     System.out.println("ENTER TOTAL NO OF TEAMS:");
     int tt=en.nextInt();
     System.out.println("ENTER NO OF TIMES EACH TEAM PLAY WITH EACH OTHER:");
     int times=en.nextInt();
     System.out.println("ENTER ALL TEAM NAMES");
     String[] tn=new String[tt];
     for(int i=0;i<tt;i++)
     { tn[i]=en.next(); }

     String[][] schedule=generateSchedule(tt, times,tn);


     try (Connection con = DriverManager.getConnection(url, user, pass); Statement stmt = con.createStatement()) 
        {
         
         stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS "+ n +"");
         stmt.executeUpdate("USE "+ n +"");
        
         //------------------------------

         stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournament_group_stage (sn INT,name VARCHAR(20), match_played INT,won INT,draw INT,points INT, spoints FLOAT)");
         
         for(int i=0;i<tt;i++)
         {
           stmt.executeUpdate("INSERT INTO tournament_group_stage VALUES ("+(i+1)+", '"+tn[i]+"' , 0, 0, 0 ,0 ,0)"); 
         }

         //---------------------------------

         stmt.executeUpdate("CREATE TABLE IF NOT EXISTS schedule (mn INT,team1 VARCHAR(20),team2 VARCHAR(20),winner VARCHAR(20),spoints1 FLOAT,spoints2 FLOAT )");
         
         for(int i=0;i<schedule[0].length;i++)
          {
            stmt.executeUpdate("INSERT INTO schedule   VALUES ("+(i+1)+", '"+ schedule[0][i] +"' , '"+ schedule[1][i] +"', 'YET TO PLAY', 0,0 )"); 
          }

          //------------------------

         System.out.println("TOURNAMENT SUCCESSFULLY CREATED");

         } 

         catch (SQLException e) {
            
           System.out.println("Setup Error: " + e.getMessage());
         }
 }
}