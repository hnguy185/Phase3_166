/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print ((rs.getString (i)).trim() + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	//OUR FUNCTIONS
        //Function that returns the total COUNT in a query

        public int GetCountResult (String query) throws SQLException {

                Statement stmt = this._connection.createStatement ();
                ResultSet rs = stmt.executeQuery (query);
                rs.next();
                int count = rs.getInt(1);
                stmt.close ();
                return count;

        }

	 public String GetCharResult (String query) throws SQLException {

                Statement stmt = this._connection.createStatement ();
                ResultSet rs = stmt.executeQuery (query);
                rs.next();
                String count = rs.getString(1);
                stmt.close ();
                return count;

        }



        //Function that checks if the String is only Letters and within the range 'num'
        //Returns 0 for error, 1 for success
        public int CorrectStringInput (String name, int num) {

                int count = name.length();
                if (num < count)
                        return 0;
                else
                        {
                          if (name.matches("^[ a-zA-Z]+$"))
                                return 1;
                          else
                                return 0;
                        }

        }
//Function checks if the String input is a valid integer 
        //and if its within the range of min and max given
        public int CheckIntegerVal (String value, int min, int max) {

                if (value.matches("[0-9]+"))
                {
                    int num =Integer.parseInt(value);
                    if ((num >= min) && (num <= max))
                        return 1;
                    else
                        return 0;
                }
                else
                        return 0;

        }


        //END of OUR FUNCTIONS

	
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Ship");
				System.out.println("2. Add Captain");
				System.out.println("3. Add Cruise");
				System.out.println("4. Book Cruise");
				System.out.println("5. List number of available seats for a given Cruise.");
				System.out.println("6. List total number of repairs per Ship in descending order");
				System.out.println("7. Find total number of passengers with a given status");
				System.out.println("8. < EXIT");
				
				switch (readChoice()){
					case 1: AddShip(esql); break;
					case 2: AddCaptain(esql); break;
					case 3: AddCruise(esql); break;
					case 4: BookCruise(esql); break;
					case 5: ListNumberOfAvailableSeats(esql); break;
					case 6: ListsTotalNumberOfRepairsPerShip(esql); break;
					case 7: FindPassengersCountWithStatus(esql); break;
					case 8: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddShip(DBproject esql) {//1
	
	try{ 
			System.out.println("Please enter the ship details: ");
			String model, input;
			int age = 0;
			int seats = 0;
			int tmp;
			String make = "";
			
			String c = "SELECT COUNT(id) FROM Ship";
			int count = esql.GetCountResult(c);
			count++;						//this is the id for the next inserted ship since the id is sorted in asceding order
			 
			boolean flag = false;
			
			while(!flag){
				System.out.print("Enter make: ");
				make = in.readLine();
				tmp = CorrectStringInput(make,32);
			 	if(tmp == 0)
                                	System.out.println("Invalid input!!! Please enter valid a input");
                        	else
                                	break;
                        }
			
			 System.out.print("Enter model: ");
			model = in.readLine();
			
			while(!flag){
			 System.out.print("Enter ship make year: ");
			 input = in.readLine();
			 tmp = CheckIntegerVal(input,0,9999);
			if(tmp == 0 || Integer.parseInt(input) < 0)
                                System.out.println("Invalid input!!! Please enter valid a input");
                        else{
				age = Integer.parseInt(input);
				break;
				}
			}
			
			while(!flag){
			 System.out.print("Enter number of seats: ");
			input = in.readLine();
			tmp = CheckIntegerVal(input,0,500);
			 if(tmp == 0 || Integer.parseInt(input) < 1 || Integer.parseInt(input) >= 500)
                                System.out.println("Invalid input!!! Please enter valid a input");
                        else{
				seats = Integer.parseInt(input);
                                break;
				}
                        }
			

				String query = "INSERT INTO Ship (id, make, model, age, seats) VALUES\n"+
						"("+ count +",'"+make+"','"+model+"',"+age+","+seats+")";
				esql.executeUpdate(query);

				String check = "SELECT * FROM Ship WHERE make = '"+make+"' AND model = '"+model+"' AND age = "+age+" AND seats = "+seats;
				int tmp2 = esql.executeQueryAndPrintResult(check);
				System.out.print(tmp2);
				System.out.println(" Ship added");
				
			/*			
				PreparedStatement stmt = esql._connection.prepareStatement ("INSERT INTO Ship (id, make, model, age, seats) VALUES (?,?,?,?,?)");
				stmt.setInt(1,id);
				stmt.setString(2,make);
				stmt.setString(3,model);
				stmt.setInt(4,age);
				stmt.setInt(5,seats);

				stmt.executeUpdate();
		*/		
			
		}catch(Exception e){
	         System.err.println (e.getMessage());
		}
	
	
	}

	public static void AddCaptain(DBproject esql) {//2
	try {
                String name, nationality;
                String query2="SELECT COUNT(c.id) FROM Captain c;";
                int count=esql.GetCountResult (query2);
                System.out.print("\n\n\n");
                System.out.print("\tENTER DATA FOR NEW CAPTAIN:\n");
                System.out.print("\tEnter name: ");

                while(true)
                {
                name=in.readLine();
                int check = esql.CorrectStringInput (name, 128);
                if (check==0)
                    System.out.print("\tError! Please enter a correct name: ");
                else
                   break;
                }

                System.out.print("\tEnter nationality: ");

                while(true)
                {
                nationality=in.readLine();
                int check = esql.CorrectStringInput (nationality, 24);
                if (check==0)
                    System.out.print("\tError! Please enter a correct nationality: ");
                else
                   break;
                }
                String query = "INSERT INTO Captain (id,fullname,nationality) VALUES ("+count+", '"+name+"', '"+nationality+"');";
                esql.executeUpdate(query);
                System.out.println ("\n\tThe Following record was ADDED:\n ");
                String check2 = "SELECT * FROM Captain WHERE fullname = '"+name+"' AND id = "+count+" AND nationality = '"+nationality+"';";
                int tmp = esql.executeQueryAndPrintResult(check2);
                System.out.print("\t"+tmp+"\n");


         } catch (Exception e) {System.err.println (e.getMessage());}


        System.out.print("\n\n\n");

	
	}

	public static void AddCruise(DBproject esql) {//3
	 try{
                        
                        String dep_date, arr_date, arr_port, input;
			String dep_port ="";
                        int num_sold = 0;
			int num_stop = 0;
			int cost = 0;
			int tmp;

			String c = "SELECT COUNT(cnum) FROM Cruise";
                        int count = esql.GetCountResult(c);
                        count++;

			boolean flag = false;

			System.out.println("Please enter the cruise details: ");
			
			while(!flag){			
			 System.out.print("Enter ticket cost: ");
			input = in.readLine();
			tmp = CheckIntegerVal(input,0,99999);
			if(tmp == 0 || Integer.parseInt(input) <= 0)
				System.out.println("Invalid input!!! Please enter valid a input");
			else{
				cost = Integer.parseInt(input);
				break;
			    }
			}

			while(!flag){
			 System.out.print("Enter number of tickets sold: ");
			num_sold = Integer.parseInt(in.readLine());
			if(num_sold < 0)
				System.out.println("Invalid input!!! Please enter valid a input");
			else
				break;
			}

			while(!flag){
			 System.out.print("Enter number of cruise stops: ");
			num_stop = Integer.parseInt(in.readLine());
			 if(num_stop < 0)
                                System.out.println("Invalid input!!! Please enter valid a input");
                        else
                                flag = true;
                        }

			 System.out.print("Enter departure date in the form mm/dd/yyyy: ");
			dep_date = in.readLine();
			 System.out.print("Enter arrival date in the form mm/dd/yyyy: ");
			arr_date = in.readLine();
			
			while(!flag){
			 System.out.print("Enter departure port: ");
			dep_port = in.readLine();
			 tmp = CorrectStringInput(dep_port,5);
			 if(tmp == 0)
                                System.out.println("Invalid input!!! Please enter valid a input");
                        else
                                break;
                        }


			while(!false){
			 System.out.print("Enter arrival port: ");
			arr_port = in.readLine();
                        tmp = CorrectStringInput(arr_port,5);                                                                                                                                        if(tmp == 0)                                                                                                                                                                       System.out.println("Invalid input!!! Please enter valid a input");                                                                                                  else                                                                                                                                                                                break;
                        }

         

			 String query = "INSERT INTO Cruise  (cnum, cost, num_sold, num_stops,actual_departure_date,\n"+ 
					"actual_arrival_date, arrival_port, departure_port) VALUES \n"+
					"("+count+","+cost+","+num_sold+","+num_stop+",'"+dep_date+"','"+arr_date+"','"+arr_port+"','"+dep_port+"')";
			 
			esql.executeUpdate(query);
			
			String q2 = "SELECT * FROM Cruise WHERE cost = "+cost+" AND num_sold = "+num_sold+" AND num_stops = "+num_stop+"\n"+
					"AND actual_departure_date = '"+dep_date+"' AND actual_arrival_date = '"+arr_date+"' AND arrival_port = '"+arr_port+"'";
			int p = esql.executeQueryAndPrintResult(q2);
			System.out.println(p);
                        System.out.println(" Cruise added");  
    	
             }catch(Exception e){
                 System.err.println (e.getMessage());
                }                              
                                


	}


	public static void BookCruise(DBproject esql) {//4
		// Given a customer and a Cruise that he/she wants to book, add a reservation to the DB
	     String num="",num2="";
                int success=1;
         try {
                // String num,num2;
                String query2="SELECT COUNT(c.id) FROM Customer c;";
               int count=esql.GetCountResult(query2);
                 System.out.print("\n\n\n");
                System.out.print("\tNOTE: The existent number of customers is from 0 to "+(count-1)+"\n");
                System.out.print("\tENTER DATA:\n");
                System.out.print("\tEnter customer ID: ");
                 while(true)
                {
                num = in.readLine();
                int num3=esql.CheckIntegerVal(num, 0, (count -1));
                if (num3 == 0)
                   System.out.print("\tError! Customer ID does not exist. Please enter correct Customer ID: ");
                else
                   break;
                }

                query2="SELECT COUNT(c.cnum) FROM Cruise c;";
                count=esql.GetCountResult (query2);
                System.out.print("\tEnter cruise number: [0-"+(count-1)+"] ");
                 while(true)
                {
                num2 = in.readLine();
                int num3=esql.CheckIntegerVal(num2, 0, (count -1));
                if (num3 == 0)
                   System.out.print("\tError! Cruise number wrong. Please enter correct Cruise number: ");
                else
                   break;
                }
                String query = "SELECT r.status FROM Reservation r WHERE r.ccid = "+num+" AND r.cid = "+num2+";";
                //int status=esql.GetCountResult(query);
                String status=esql.GetCharResult (query);
                if (status.equals("W"))
                        {
                          query="UPDATE Reservation SET status = 'C' WHERE ccid ="+num+" AND cid="+num2+";";
                          esql.executeUpdate(query);
                          System.out.print("\n\tRESERVATION UPDATED FROM WAITLIST to CONFIRMED\n ");

                        }
                else if (status.equals("C"))
                        System.out.print("\n\tThe status of the reservation is CONFIRMED\n");

                else 
                        System.out.print("\n\tThe status of the reservation is RESERVED\n");
	
                } catch (Exception e) {
                                //      System.err.println ("test NOT RESERVATION Found it");
                                        success=0;
                                        }

                if (success == 0)
                {
                        try{
                         String query = "SELECT s.num_sold FROM Cruise s, Schedule c WHERE s.cnum = c.cruiseNum AND s.cnum = "+num2+" ;";
                         int ticketsSold=esql.GetCountResult(query);
                         query = "SELECT s.seats FROM Ship s, Cruise c, CruiseInfo f WHERE f.cruise_id = c.cnum AND s.id = f.ship_id AND c.cnum = "+num+";";
                         int seats=esql.GetCountResult(query);
                         int available= (seats - ticketsSold);
                         System.out.print("\n\tNumber of seats availabe for this cruise: "+available+"\n");
                         query="SELECT COUNT(r.rnum) FROM Reservation r;";
                         int count2=esql.GetCountResult (query);

                         if (available > 0)
                                {
                                        query="INSERT INTO Reservation (rnum, ccid, cid, status) VALUES ("+(count2)+","+num+","+num2+", 'R');";
                                        System.out.print("\n\tThe Cruise has tickets available. Reservation created and status of ticket: RESERVED\n");


                                }
                        else
                                {
                                        query="INSERT INTO Reservation (rnum, ccid, cid, status) VALUES ("+(count2)+","+num+","+num2+", 'W');";
                                        System.out.print("\n\tThe Cruise does not have tickets available. Reservation created and status of ticket: WAITLISTED\n");
                                }

                         esql.executeUpdate(query);
                         System.out.println ("\n\tThe Following record was ADDED:\n ");
                         String check2 = "SELECT * FROM Reservation WHERE ccid = '"+num+"' AND cid = "+num2+";";
                         int tmp = esql.executeQueryAndPrintResult(check2);
                         System.out.print("\t"+tmp+"\n");


                }catch (Exception e) {System.err.println (e.getMessage());}
                }
                 System.out.print("\n\n\n");


	}

	public static void ListNumberOfAvailableSeats(DBproject esql) {//5
		// For Cruise number and date, find the number of availalbe seats (i.e. total Ship capacity minus booked seats )
		
	  try {

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String date,num;
                String query2="SELECT COUNT(c.cnum) FROM Cruise c;";
                int count=esql.GetCountResult (query2);
                System.out.print("\n\n\n");
                System.out.print("\tNOTE: The existent number of cruises is from 0 to "+(count-1)+"\n");
                System.out.print("\t      For inserting a DATE please use the following format MM/DD/YYYY and don't use 0 at the begining. Thanks!");
                System.out.print("\n\n");
                System.out.print("\tPlease enter number of cruise: ");
                while(true)
                {
                num = in.readLine();
                int num2=esql.checkIntegerVal(num, 0, (count -1));
                if (num2 == 0)
                   System.out.print("\tError! Cruise Does not exist. Please enter correct cruise number: ");
                else
                   break;
                }
                System.out.print("\tEnter Departure date of cruise in the following format MM/DD/YYYY: ");
                while(true)
                {
                date=in.readLine();
                 if (date.matches("^(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((18|19|20|21)\\d\\d)$"))
                                break;
                          else
                                 System.out.print("\tError! Please enter a correct data of FORMAT MM/DD/YYYY: ");
                }
                String query = "SELECT s.num_sold FROM Cruise s, Schedule c WHERE s.cnum = c.cruiseNum AND s.cnum = "+num+" AND c.departure_time = '"+date+"%';";
                int ticketsSold=esql.GetCountResult(query);
                query = "SELECT s.seats FROM Ship s, Cruise c, CruiseInfo f WHERE f.cruise_id = c.cnum AND s.id = f.ship_id AND c.cnum = "+num+";";
                int seats=esql.GetCountResult(query);
                System.out.print("\n\tNumber of seats availabe for this cruise: "+ (seats - ticketsSold)+"\n");


        // } catch (Exception e) {System.err.println (e.getMessage());}
           } catch (Exception e) {System.err.println ("\n\t THIS CRUISE IS NOT SCHEDULED FOR THIS DATE. THANKS! ");}

                System.out.print("\n\n\n");
	
	}

	public static void ListsTotalNumberOfRepairsPerShip(DBproject esql) {//6
		// Count number of repairs per Ships and list them in descending order
	try{
			
			String query = "SELECT DISTINCT s.make, s.model, COUNT(s.id) \n"+
				"AS Total_Repairs FROM Ship s, Repairs r WHERE s.id=r.ship_id GROUP BY s.id ORDER BY Total_Repairs DESC";
			List<List<String>> result = new ArrayList<List<String>>(); 
			result = esql.executeQueryAndReturnResult(query);
			System.out.println("make \t\t\t\t model \t\t\t\t\t\t\t num of repairs");
			result.forEach(System.out::println);

		  }catch(Exception e){
                 System.err.println (e.getMessage());
                }
	
	}

	
	public static void FindPassengersCountWithStatus(DBproject esql) {//7
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
	
	try{
			int tmp; int cnum = 0; 
			char status = ' '; 
			String input;
			boolean flag = false;

			  String c = "SELECT COUNT(id) FROM Ship";
                        int count = esql.GetCountResult(c);
				

			System.out.println("Please enter the details: ");
			while(!flag){
                        System.out.print("Enter cruise ID: ");
			cnum = Integer.parseInt(in.readLine());
			if(cnum >= 0 && cnum <= count )
				break;
			else
				System.out.println("Invalid input!!! Please enter a valid input.");
			}
				
			while(!flag){
			System.out.print("Enter customer status (W/R/C): ");
			input = in.readLine();
			status = input.charAt(0);
			if(Character.toUpperCase(status) == 'W'|| Character.toUpperCase(status) == 'R' || Character.toUpperCase(status) == 'C') 
				break;
			else
				System.out.println("Invalid input!!! Please enter a valid input.");
			}
			
			String query = "SELECT COUNT(r.ccid) FROM Reservation r, Cruise c WHERE r.cid = c.cnum AND r.status = '"+status+"' AND c.cnum = "+cnum;
			tmp = esql.executeQueryAndPrintResult(query);
			System.out.println(tmp+" query executed");
			
	
		}catch(Exception e){
                 System.err.println (e.getMessage());
                }	
	}

}
