/*
 * Juliet.java
 *
 * Juliet class.  Implements the Juliet subsystem of the Romeo and Juliet ODE system.
 */



import javafx.util.Pair;

import java.io.*;
import java.lang.Thread;
import java.net.*;

public class Juliet extends Thread {

    private ServerSocket ownServerSocket = null; //Juliet's (server) socket
    private Socket serviceMailbox = null; //Juliet's (service) socket

    private double currentLove = 0;
    private double b = 0;

    //Class construtor
    public Juliet(double initialLove) {
        currentLove = initialLove;
        b = 0.01;
        try {
            
			//TO BE COMPLETED
            ownServerSocket = new ServerSocket(81);
			
            System.out.println("Juliet: Good pilgrim, you do wrong your hand too much, ...");
        } catch(Exception e) {
            System.out.println("Juliet: Failed to create own socket " + e);
        }
    }

    //Get acquaintance with lover;
    // Receives lover's socket information and share's own socket
    public Pair<InetAddress,Integer> getAcquaintance() {
        System.out.println("Juliet: My bounty is as boundless as the sea,\n" +
                "       My love as deep; the more I give to thee,\n" +
                "       The more I have, for both are infinite.");
            
			//TO BE COMPLETED
        try {
            return new Pair<>(InetAddress.getLocalHost(), ownServerSocket.getLocalPort());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }



    //Retrieves the lover's love
    public double receiveLoveLetter()
    {
        double tmp = 0;
			//TO BE COMPLETED
        try {
            serviceMailbox = ownServerSocket.accept();
            BufferedReader br_ifc = new BufferedReader(new InputStreamReader(serviceMailbox.getInputStream()));
            String msg_str = br_ifc.readLine();
            tmp = Double.parseDouble(msg_str.substring(0, msg_str.length() - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Juliet: Romeo, Romeo! Wherefore art thou Romeo? (<-" + tmp + ")");
        return tmp;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Juliet: Come, gentle night, come, loving black-browed night,\n" +
                "       Give me my Romeo, and when I shall die,\n" +
                "       Take him and cut him out in little stars.");
        currentLove = currentLove+(-b*partnerLove);
        return currentLove;
    }


    //Communicate love back to playwriter
    public void declareLove(){
            
			//TO BE COMPLETED
        try {
            System.out.println("Good night, good night!\n" +
                    "Parting is such sweet sorrow,\\n That I shall say good night till it be morrow.");
            PrintWriter pw_otc = new PrintWriter(serviceMailbox.getOutputStream(), true);
            pw_otc.println(String.valueOf(currentLove) + "J");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double RomeoLove = this.receiveLoveLetter();

                //Estimate new love value
                this.renovateLove(RomeoLove);

                //Communicate back to lover, Romeo's love
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Juliet: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Juliet: I will kiss thy lips.\n" +
                    "Haply some poison yet doth hang on them\n" +
                    "To make me die with a restorative.");
        }

    }

}
