import java.sql.*;
import java.io.*;
import java.util.*;
import java.lang.*;
public class UniDB {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "strangehat";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      stmt.executeUpdate("DROP DATABASE IF EXISTS university");    
      stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS university");
      stmt.executeUpdate("use university");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS semester " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "year INTEGER NOT NULL, " +
			 "season TEXT NOT NULL, " +
                         "PRIMARY KEY (id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS department " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "name TEXT NOT NULL, " +
			 "building TEXT NOT NULL, " +
                         "PRIMARY KEY (id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS location " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "building TEXT NOT NULL, " +
			 "room INT NOT NULL, " +
			 "purpose TEXT, " +
                         "PRIMARY KEY (id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS course " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "department INT NOT NULL, " +
                         "abbreviation TEXT NOT NULL, " +
                         "number INT NOT NULL, " +
                         "title TEXT NOT NULL, " +
			 "credits INT NOT NULL, " +
                         "PRIMARY KEY (id), " +
                         "FOREIGN KEY (department) REFERENCES department(id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS major " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "department INTEGER NOT NULL, " +
			 "name TEXT NOT NULL, " +
                         "PRIMARY KEY (id), " +
			 "FOREIGN KEY (department) REFERENCES department(id))");


      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS faculty " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "name TEXT NOT NULL, " +
                         "department INT NOT NULL, " +
                         "startDate INT NOT NULL, " +
                         "endDate INT, " +
			 "office INT NOT NULL, " +
                         "PRIMARY KEY (id), " +
                         "FOREIGN KEY (startDate) REFERENCES semester(id), " +
			 "FOREIGN KEY (endDate) REFERENCES semester(id), " +
                         "FOREIGN KEY (office) REFERENCES location(id), " +
                         "FOREIGN KEY (department) REFERENCES department(id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS student " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "name TEXT NOT NULL, " +
                         "graduationDate INT NOT NULL, " +
                         "major INT, " +
                         "adviser INT NOT NULL, " +
                         "PRIMARY KEY (id), " +
                         "FOREIGN KEY (adviser) REFERENCES faculty(id), " +
                         "FOREIGN KEY (major) REFERENCES major(id), " +
                         "FOREIGN KEY (graduationDate) REFERENCES semester(id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS section " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "course INT NOT NULL, " +
                         "instructor INT NOT NULL, " +
                         "offered INT NOT NULL, " +
                         "location INT NOT NULL, " +
			 "startHour TIME NOT NULL, " +
                         "PRIMARY KEY (id), " +
                         "FOREIGN KEY (instructor) REFERENCES faculty(id), " +
                         "FOREIGN KEY (course) REFERENCES course(id), " +
			 "FOREIGN KEY (location) REFERENCES location(id), " +
                         "FOREIGN KEY (offered) REFERENCES semester(id))");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS enrollment " +
                         "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                         "student INTEGER NOT NULL, " +
		         "section INTEGER NOT NULL, " +
			 "grade TEXT, " +
                         "PRIMARY KEY (id), " +
            		 "FOREIGN KEY (section) REFERENCES section(id), " +
			 "FOREIGN KEY (student) REFERENCES student(id))");

     String[] smtr = {"Summer","Fall", "Spring"};
     for(int i=2015; i<2020; i++){
          for(int j=0; j < smtr.length; j++){
	      String sql = "INSERT INTO semester(year, season) VALUES(?, ?)";
              PreparedStatement statement_prepared = conn.prepareStatement(sql);
              statement_prepared.setInt(1, i);
              statement_prepared.setString(2, smtr[j]);
              statement_prepared.executeUpdate();
	      }
     }


     String[][] dept = new String[29][];
     // The name of the file to open.
        String fileName = "sources/lc_departments.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            int i = 0;

            while((line = bufferedReader.readLine()) != null) {
                i++;
                String[] ln = line.split("\\|");
                dept[i] = ln;
                
                
            }   
            // Always close files.
            bufferedReader.close();       
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
                }                  
            // Or we could just do this: 
            // ex.printStackTrace();

      for(int i=1; i<dept.length; i++){
        String sql = "INSERT INTO department(name, building) VALUES(?, ?)";
        PreparedStatement statement_prepared = conn.prepareStatement(sql);
        statement_prepared.setString(1, dept[i][0]);
        statement_prepared.setString(2, dept[i][3]);
        statement_prepared.executeUpdate();
      }

      Map<String, Integer> dictionary = new HashMap<String, Integer>();
      dictionary.put("ACCTG",7);
      dictionary.put("ECON",7);
      dictionary.put("MGT",7);
      dictionary.put("ANTH",26);
      dictionary.put("AFRS",1);
      dictionary.put("ART",27);
      dictionary.put("ARTH",12);
      dictionary.put("ATHTR",11);
      dictionary.put("BIO",2);
      dictionary.put("CHEM",3);
      dictionary.put("CHIN",16);
      dictionary.put("CLAS",4);
      dictionary.put("COMS",5);
      dictionary.put("CS",6);
      dictionary.put("DAN",27);
      dictionary.put("DS",6);
      dictionary.put("EDUC",8);
      dictionary.put("ENG",9);
      dictionary.put("ENVS",10);
      dictionary.put("FCUL",13);
      dictionary.put("FREN",16);
      dictionary.put("GS",19);
      dictionary.put("GER",16);
      dictionary.put("GRK",16);
      dictionary.put("HLTH",11);
      dictionary.put("HEB",16);
      dictionary.put("HIST",12);
      dictionary.put("IS",13);
      dictionary.put("JOUR",5);
      dictionary.put("LAT",16);
      dictionary.put("LING",16);
      dictionary.put("MATH",15);
      dictionary.put("MUST",12);
      dictionary.put("MUS",17);
      dictionary.put("NEUR",23);
      dictionary.put("NURS",18);
      dictionary.put("PAID",19);
      dictionary.put("PHIL",20);
      dictionary.put("PE",11);
      dictionary.put("PHYS",21);
      dictionary.put("POLS",22);
      dictionary.put("PSYC",23);
      dictionary.put("REL",24);
      dictionary.put("RUS",16);
      dictionary.put("SCST",26);
      dictionary.put("SW",26);
      dictionary.put("SCI",21);
      dictionary.put("SOC",26);
      dictionary.put("SPAN",16);
      dictionary.put("THE",27);
      dictionary.put("WGST",28);



      String[][] courses = new String[850][];
     // The name of the file to open.
        String fileName2 = "sources/lc_courses.txt";

        // This will reference one line at a time
        String line2 = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName2);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            int i = 0;

            while((line2 = bufferedReader.readLine()) != null) {
                i++;
                String[] ln = line2.split("\\|");
                courses[i] = ln;
                
                
            }   
            // Always close files.
            bufferedReader.close();
                  
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
                }                  
            // Or we could just do this: 
            // ex.printStackTrace();

      for(int i=1; i<courses.length; i++){
        String sql = "INSERT INTO course(department, abbreviation, number, title, credits) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement statement_prepared = conn.prepareStatement(sql);
        String abbr = courses[i][0].split(" ")[0];
        String number = courses[i][0].split(" ")[1];
        statement_prepared.setInt(1, dictionary.get(abbr));
        statement_prepared.setString(2, abbr);
        statement_prepared.setInt(3, Integer.parseInt(number));
        statement_prepared.setString(4,courses[i][1]);
        statement_prepared.setInt(5, Integer.parseInt(courses[i][2]));
        statement_prepared.executeUpdate();
      }
    
      ResultSet results;
      results = stmt.executeQuery("SELECT DISTINCT building FROM department");
      while (results.next()) {
        for(int i=101; i<321; i++){
        String sql = "INSERT INTO location(building, room) VALUES(?, ?)";
        PreparedStatement statement_prepared = conn.prepareStatement(sql);
        statement_prepared.setString(1, results.getString("building"));
        statement_prepared.setInt(2, i);
        statement_prepared.executeUpdate();
        }
        
      }
      results.close();
      String[] deptlist =new String[28];
      int i = 0;
      results = stmt.executeQuery("SELECT id,name FROM department order by id");
      while(results.next()){
        deptlist[i] = results.getString("name");
        i++;
      }
      results.close();
      String fileName3 = "sources/lc_faculty.txt";

        // This will reference one line at a time
        String line3 = null;
        Random strdate = new Random();
        Random offc = new Random();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName3);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            int a = 0;
            line3 = bufferedReader.readLine();
            while(line3 != null){
          
              if(line3.equalsIgnoreCase(deptlist[a])){
                line3=bufferedReader.readLine();
              }
              else if(line3.length() == 0){
                line3=bufferedReader.readLine();
              }
              else if ((int)line3.charAt(1)>96){
                String sql = "INSERT INTO faculty(name, department, startDate, office) VALUES(?, ?, ?, ?)";
                PreparedStatement statement_prepared = conn.prepareStatement(sql);
                String name = line3.split("\\|")[0];
                int j = strdate.nextInt(14) +1;
                int k = offc.nextInt(3079) +1;
                statement_prepared.setString(1, name);
                statement_prepared.setInt(2, a+1);
                statement_prepared.setInt(3, j);
                statement_prepared.setInt(4,k);
                
                statement_prepared.executeUpdate();
                line3= bufferedReader.readLine();

              } else {
                a++;

              }
              // System.out.println(line3);
              // System.out.println(a);
            }
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
                }

        String fileName4 = "sources/lc_majors.txt";

        // This will reference one line at a time
        String line4 = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName4);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            Statement[] stmtList = new Statement[50];
            int c=0;
            
            while((line4 = bufferedReader.readLine()) != null) {
                ResultSet result;
                String majorname = line4.split("\\|")[0];
                String deptname = line4.split("\\|")[1];
                stmtList[c] = conn.createStatement();
                result = stmtList[c].executeQuery("SELECT id FROM department where name = '" + deptname + "'");
                result.next();
                int deptnum= result.getInt("id");
                String sql = "INSERT INTO major(department, name) VALUES(?, ?)";
                PreparedStatement statement_prepared = conn.prepareStatement(sql);
                statement_prepared.setInt(1, deptnum);
                statement_prepared.setString(2, majorname);
                statement_prepared.executeUpdate();
                c++;

                
            }   
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
                }
        String fileName5 = "sources/lc_students.txt";

        // This will reference one line at a time
        String line5 = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName5);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            Random date = new Random();
            Random advis= new Random();
            Random maj = new Random();
            while((line5 = bufferedReader.readLine()) != null) {
                String name = line5.replaceAll("\t"," ");
                String sql = "INSERT INTO student(name, graduationDate, major, adviser) VALUES(?, ?, ?, ?)";
                PreparedStatement statement_prepared = conn.prepareStatement(sql);
                statement_prepared.setString(1, name);
                statement_prepared.setInt(2, date.nextInt(15)+1);
                statement_prepared.setInt(3, maj.nextInt(49)+1);
                statement_prepared.setInt(4, advis.nextInt(251)+1);
                statement_prepared.executeUpdate();

                
                
            }   
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
                }
        String[] time = {"07:00:00","08:00:00","11:00:00","10:30:00","09:15:00","12:30:00","14:00:00","18:00:00","15:15:00" };
        Random indx = new Random();
        ResultSet rs;
        rs = stmt.executeQuery("select c.id,f.id from course c join faculty f on c.department = f.department where rand()<=0.2");
        while(rs.next()){
          String sql = "INSERT INTO section(course, instructor, offered, location, startHour) VALUES(?, ?, ?, ?,?)";
          PreparedStatement statement_prepared = conn.prepareStatement(sql);
          statement_prepared.setInt(1, rs.getInt("c.id"));
          statement_prepared.setInt(2, rs.getInt("f.id"));
          statement_prepared.setInt(3, indx.nextInt(15)+1);
          statement_prepared.setInt(4, indx.nextInt(3080)+1);
          statement_prepared.setString(5, time[indx.nextInt(9)]);
          statement_prepared.executeUpdate();

        }
        
        rs = stmt.executeQuery("select s.id, sc.id from student s join section sc  where rand()<= 0.04");
        while(rs.next()){
          String sql = "INSERT INTO enrollment(student,section) VALUES(?, ?)";
          PreparedStatement statement_prepared = conn.prepareStatement(sql);
          statement_prepared.setInt(1, rs.getInt("s.id"));
          statement_prepared.setInt(2, rs.getInt("sc.id"));
          statement_prepared.executeUpdate();

        }
        stmt.executeUpdate("drop view if exists RED_ALERT");
        stmt.executeUpdate("create view RED_ALERT AS select section, c.title, f.name, count(student) from enrollment e join section sc on e.section = sc.id join faculty f on sc.instructor=f.id join course c on c.id =sc.course group by(section) having count(student) < 9");
        stmt.executeUpdate("drop view if exists seniors");
        stmt.executeUpdate("create view seniors AS select s.id, s.name,s.major, s.adviser from student s join semester sm where (s.graduationDate = sm.id and sm.year = YEAR(CURDATE()))");
        stmt.executeUpdate("drop view if exists currentlyOfferedCourse");
        stmt.executeUpdate("create view currentlyOfferedCourse AS select c.id, c.abbreviation, c.title, sc.instructor, sc.startHour, sm.year, sm.season from course c join section sc on c.id = sc.course join semester sm on sm.id = sc.offered where sm.year = year(curdate())");
        stmt.executeUpdate("drop view if exists rosters");
        stmt.executeUpdate("create view rosters as select sc.id as sectionId, c.title, s.id as studentId, s.name, s.major from student s join enrollment e on s.id = e.student join section sc on e.section = sc.id join course c on c.id = sc.course order by(c.title)");
        stmt.executeUpdate("drop view if exists studentView");
        stmt.executeUpdate("create view studentView AS select s.id, s.name, m.name as major,c.title as courseTaken, sc.id as sectionId, e.grade as grade, f.name as adviser, concat(sm.season, ' ', sm.year) as graduationDate from student s join enrollment e on e.student = s.id join section sc on sc.id = e.section join major m on m.id = s.major join semester sm on sm.id = s.graduationDate join faculty f on s.adviser = f.id join course c on c.id = sc.course");
   
        stmt.executeUpdate("drop trigger if exists new_faculty");
        stmt.executeUpdate("create trigger new_faculty before insert on faculty for each row set new.endDate= NULL");
        stmt.executeUpdate("drop trigger if exists new_student");
        stmt.executeUpdate("create trigger new_student before insert on student for each row set new.major= NULL");
        stmt.executeUpdate("drop trigger if exists new_enroll");
        stmt.executeUpdate("create trigger new_enroll before insert on enrollment for each row set new.grade= NULL");








   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
}//end main
}
