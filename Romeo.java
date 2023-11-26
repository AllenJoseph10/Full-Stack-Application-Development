/*
 * Romeo.java
 *
 * Romeo class.  Implements the Romeo subsystem of the Romeo and Juliet ODE system.
 */


import java.io.*;
import java.lang.Thread;
import java.net.*;

import javafx.util.Pair;

public class Romeo extends Thread {

    private ServerSocket ownServerSocket = null; //Romeo's (server) socket
    private Socket serviceMailbox = null; //Romeo's (service) socket


    private double currentLove = 0;
    private double a = 0; //The ODE constant

    //Class construtor
    public Romeo(double initialLove) {
        currentLove = initialLove;
        a = 0.02;
        try {
            
			//TO BE COMPLETED
            ownServerSocket = new ServerSocket(80);

            System.out.println("Romeo: What lady is that, which doth enrich the hand\n" +
                    "       Of yonder knight?");
        } catch(Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
   }

    //Get acquaintance with lover;
    public Pair<InetAddress,Integer> getAcquaintance() {
        System.out.println("Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");
            
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

			//TO BE COMPLETED
        double tmp = 0;
        try {
            serviceMailbox = ownServerSocket.accept();
            BufferedReader br_ifc = new BufferedReader(new InputStreamReader(serviceMailbox.getInputStream()));
            String sr_msg = br_ifc.readLine();
            tmp = Double.parseDouble(sr_msg.substring(0, sr_msg.length() - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Romeo: O sweet Juliet... (<-" + tmp + ")");
        return tmp;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Romeo: But soft, what light through yonder window breaks?\n" +
                "       It is the east, and Juliet is the sun.");
        currentLove = currentLove+(a*partnerLove);
        return currentLove;
    }


    //Communicate love back to playwriter
    public void declareLove(){
            
			//TO BE COMPLETED
        try {
            System.out.println("I would I were thy bird");
            PrintWriter pw_otc = new PrintWriter(serviceMailbox.getOutputStream(), true);
            //outToClient.println(renovateLove(receiveLoveLetter()) + "R");
            pw_otc.println(String.valueOf(currentLove) + "R");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double JulietLove = this.receiveLoveLetter();

                //Estimate new love value
                this.renovateLove(JulietLove);

                //Communicate love back to playwriter
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Romeo: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Romeo: Here's to my love. O true apothecary,\n" +
                    "Thy drugs are quick. Thus with a kiss I die." );
        }
    }

}
