package templates;/*
 * BooksDatabaseService.java
 *
 * The service threads for the books database server.
 * This class implements the database access service, i.e. opens a JDBC connection
 * to the database, makes and retrieves the query, and sends back the result.
 *
 * author: <2549623>
 *
 */

import java.io.*;
//import java.io.OutputStreamWriter;

import java.net.Socket;

import java.util.StringTokenizer;

import java.sql.*;
import javax.sql.rowset.*;
//Direct import of the classes CachedRowSet and CachedRowSetImpl will fail becuase
//these clasess are not exported by the module. Instead, one needs to impor
//javax.sql.rowset.* as above.



public class BooksDatabaseService extends Thread{

    private Socket serviceSocket = null;
    private String[] requestStr  = new String[2]; //One slot for author's name and one for library's name.
    private ResultSet outcome   = null;

    //JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL      = Credentials.URL;



    //Class constructor
    public BooksDatabaseService(Socket aSocket){

        //TO BE COMPLETED
        serviceSocket = aSocket;
        start();
    }


    //Retrieve the request from the socket
    public String[] retrieveRequest()
    {
        this.requestStr[0] = ""; //For author
        this.requestStr[1] = ""; //For library

        String temp1 = "";
        try {

            InputStream fromcli = serviceSocket.getInputStream();
            int charac;
            while ((charac = fromcli.read()) != '#') {
                temp1 += (char) charac;
            }

            String[] req = temp1.split(";");
            this.requestStr[0] = req[0];
            this.requestStr[1] = req[1];



            //TO BE COMPLETED

        }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        return this.requestStr;

    }


    //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;

        this.outcome = null;

        String sql = "SELECT title, publisher, genre, rrp, COUNT(copyid) AS number_of_copies FROM book, bookcopy, author, library WHERE library.city='" + requestStr[1] + "' AND author.familyname = '" + requestStr[0] + "' AND author.authorid = book.authorid AND library.libraryid = bookcopy.libraryid and book.bookid = bookcopy.bookid GROUP BY (title, publisher, genre, rrp);"; //TO BE COMPLETED- Update this line as needed.


        try {
            //Connect to the database
            //TO BE COMPLETED
            Connection connect = DriverManager.getConnection(Credentials.URL,Credentials.USERNAME,Credentials.PASSWORD);
            //Make the query
            //TO BE COMPLETED


            Statement sment = connect.createStatement();
            ResultSet reset = sment.executeQuery(sql);
            //Process query
            //TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.
            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet cachedrs = aFactory.createCachedRowSet();
            cachedrs.populate(reset);



            while (cachedrs.next())
                System.out.println(cachedrs.getObject(1) + " ..... 1111");

            cachedrs.beforeFirst();

            this.outcome = cachedrs;
            //Clean up
            //TO BE COMPLETED
            reset.close();
            sment.close();
            connect.close();

        } catch (Exception e)
        { System.out.println(e); }

        return flagRequestAttended;
    }



    //Wrap and return service outcome
    public void returnServiceOutcome(){
        try {
            //Return outcome
            //TO BE COMPLETED
            ObjectOutputStream osWriter = new ObjectOutputStream(serviceSocket.getOutputStream());
            osWriter.writeObject(outcome);

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket
            //TO BE COMPLETED
            serviceSocket.close();

        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run()
    {
        try {
            System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
                    + "author->" + this.requestStr[0] + "; library->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        }catch (Exception e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
