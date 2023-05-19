package com.example.concurrentprogramming;

/*
 * Romeo.java
 *
 * Romeo class.  Implements the Romeo subsystem of the Romeo and Juliet ODE system.
 */

import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;

import javafx.util.Pair;

public class Romeo extends Thread {

    private ServerSocket ownServerSocket = null; // Romeo's (server) socket
    private Socket serviceMailbox = null; // Romeo's (service) socket
    private BufferedReader br;
    private PrintWriter pw;
    private double currentLove = 0;
    private double a = 0; // The ODE constant

    // Class construtor
    public Romeo(double initialLove) {
        currentLove = initialLove;
        a = 0.02;
        try {
            ownServerSocket = new ServerSocket(7778);
            System.out.println("Romeo: What lady is that, which doth enrich the hand\n" + "       Of yonder knight?");
        } catch (Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
    }

    // Get acquaintance with lover;
    public Pair<InetAddress, Integer> getAcquaintance() {
        System.out.println(
                "Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");
        try {
            serviceMailbox = ownServerSocket.accept();
            pw = new PrintWriter(serviceMailbox.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(serviceMailbox.getInputStream()));

            return new Pair<>(serviceMailbox.getInetAddress(), ownServerSocket.getLocalPort());
        } catch (Exception e) {
            System.out.println("Romeo: Failed to get lover's socket information " + e);
            System.exit(1);
        }
        return null;

    }

    // Retrieves the lover's love
    public double receiveLoveLetter() {

        double tmp = -1;
        try {
            tmp = Double.parseDouble(br.readLine());
            System.out.println("Romeo: Letter received");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Romeo: Failed to receive lover's love " + e);
            System.exit(1);
        }

        System.out.println("Romeo: O sweet Juliet... (<-" + tmp + ")");
        return tmp;
    }

    // Love (The ODE system)
    // Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove) {
        System.out.println("Romeo: But soft, what light through yonder window breaks?\n"
                + "       It is the east, and Juliet is the sun.");
        currentLove = currentLove + (a * partnerLove);
        return currentLove;
    }

    // Communicate love back to playwriter
    public void declareLove() {

        try {
            pw.println(currentLove);
        } catch (Exception e) {
            System.out.println("Romeo: Failed to declare love " + e);
            System.exit(1);
        }

    }

    // Execution
    public void run() {
        try {
            while (!this.isInterrupted()) {
                System.out.println("Romeo: Entering service iteration.");

                // Retrieve lover's current love
                System.out.println("Romeo: Awaiting letter");
                double JulietLove = this.receiveLoveLetter();

                // Estimate new love value
                this.renovateLove(JulietLove);

                // Communicate love back to playwriter
                this.declareLove();

                System.out.println("Romeo: I would I were thy bird. (->" + currentLove + "R)");
                System.out.println("Romeo: Exiting service iteration.");
            }
        } catch (Exception e) {
            System.out.println("Romeo: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println(
                    "Romeo: Here's to my love. O true apothecary,\n" + "Thy drugs are quick. Thus with a kiss I die.");
        }


    }

}
