package com.example.concurrentprogramming;/*
 /*
 * Juliet.java
 *
 * Juliet class.  Implements the Juliet subsystem of the Romeo and Juliet ODE system.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.util.Pair;

public class Juliet extends Thread {

    private ServerSocket ownServerSocket = null; // Juliet's (server) socket
    private Socket serviceMailbox = null; // Juliet's (service) socket
    private BufferedReader br;
    private PrintWriter pw;
    private double currentLove = 0;
    private double b = 0;

    // Class constructor
    public Juliet(double initialLove) {
        currentLove = initialLove;
        b = 0.01;
        try {
            ownServerSocket = new ServerSocket(7779);

            System.out.println("Juliet: Good pilgrim, you do wrong your hand too much, ...");
        } catch (Exception e) {
            System.out.println("Juliet: Failed to create own socket " + e);
        }
    }

    // Get acquaintance with lover;
    // Receives lover's socket information and share's own socket
    public Pair<InetAddress, Integer> getAcquaintance() {
        System.out.println(
                "Juliet: My bounty is as boundless as the sea,\n" + "       My love as deep; the more I give to thee,\n"
                        + "       The more I have, for both are infinite.");

        try {
            serviceMailbox = ownServerSocket.accept();
            pw = new PrintWriter(serviceMailbox.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(serviceMailbox.getInputStream()));
            return new Pair<>(serviceMailbox.getInetAddress(), ownServerSocket.getLocalPort());
        } catch (Exception e) {
            System.out.println("Juliet: Failed to get lover's socket information " + e);
            System.exit(1);
        }
        return null;

    }

    // Retrieves the lover's love
    public double receiveLoveLetter() {

        double tmp = -1;
        try {

            tmp = Double.parseDouble(br.readLine());
            System.out.println("Juliet: Letter received");

        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("Juliet: Failed to receive lover's love " + e);
            System.exit(1);
        }

        System.out.println("Juliet: Romeo, Romeo! Wherefore art thou Romeo? (<-" + tmp + ")");
        return tmp;
    }

    // Love (The ODE system)
    // Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove) {
        System.out.println("Juliet: Come, gentle night, come, loving black-browed night,\n"
                + "       Give me my Romeo, and when I shall die,\n"
                + "       Take him and cut him out in little stars.");
        currentLove = currentLove + (-b * partnerLove);
        return currentLove;
    }

    // Communicate love back to playwriter
    public void declareLove() {

        try {
            pw.println(currentLove);
        } catch (Exception e) {
            System.out.println("Juliet: Failed to declare love " + e);
            System.exit(1);
        }

    }

    // Execution
    public void run() {

        try {

            while (!this.isInterrupted()) {
                // Retrieve lover's current love
                System.out.println("Juliet: Awaiting letter");
                double RomeoLove = this.receiveLoveLetter();

                // Estimate new love value
                this.renovateLove(RomeoLove);

                // Communicate back to lover, Romeo's love
                this.declareLove();

                System.out.println("Juliet: Good night, good night! Parting is such sweet sorrow,\n"
                        + "\tThat I shall say good night till it be morrow. (->" + currentLove + "J)");
            }

        } catch (Exception e) {
            System.out.println("Juliet: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Juliet: I will kiss thy lips.\n" + "Haply some poison yet doth hang on them\n"
                    + "To make me die with a restorative.");
        }

    }

}

