package ser321.assign6.ckovacs1.java.server;

/*
* Copyright 2020 Curtis Kovacs,
*
* This software is the intellectual property of the author, and can not be
distributed, used, copied, or
* reproduced, in whole or in part, for any purpose, commercial or otherwise.
The author grants the ASU
* Software Engineering program the right to copy, execute, and evaluate this
work for the purpose of
* determining performance of the author in coursework, and for Software
Engineering program evaluation,
* so long as this copyright and right-to-use statement is kept in-tact in such
use.
* All other uses are prohibited and reserved to the author.
*
* Purpose: An class that acts as a controller in a MVC system
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/

import java.net.*;
import java.io.*;
import java.util.*;



public class SeriesSeasonLibraryTCPJsonRPCServer extends Thread {


    private Socket conn;
    private int id;
    private SeriesSeasonLibrarySkeleton skeleton;

    public SeriesSeasonLibraryTCPJsonRPCServer(Socket sock, int id, SeriesLibrary sl){

    	this.conn = sock;
        this.id = id;
        skeleton = new SeriesSeasonLibrarySkeleton(sl);
    }

    //run() is executed when a new thread is started
    public void run() {
      try {
         OutputStream outSock = conn.getOutputStream();
         InputStream inSock = conn.getInputStream();
         byte clientInput[] = new byte[2048]; // up to 1024 bytes in a message.
         int numr = inSock.read(clientInput,0,2048);
         if (numr != -1) {
            System.out.println("read "+numr+" bytes");
            String request = new String(clientInput,0,numr);
            System.out.println("request is: "+request);
            String response = skeleton.callMethod(request);
            byte clientOut[] = response.getBytes();
	        outSock.write(clientOut,0,clientOut.length);
            System.out.println("response is: "+response);
         }
         inSock.close();
         outSock.close();
         conn.close();
      } catch (IOException e) {
         System.out.println("I/O exception occurred for the connection:\n"+e.getMessage());
      }
   }

   public static void main (String args[]) {
      Socket sock;
      SeriesLibrary sl = new SeriesLibraryImpl();
      int id=0;
      try {
         if (args.length != 1) {
            System.out.println("Usage: ant server"+
                               "[host] [portNum]");
            System.exit(0);
         }
         int portNo = Integer.parseInt(args[0]);
         if (portNo <= 1024) portNo=8888;
         ServerSocket serv = new ServerSocket(portNo);
         // accept client requests. For each request create a new thread to handle
         while (true) { 
            System.out.println("SeriesLibrary server waiting for connects on port "
                               +portNo);
            sock = serv.accept();
            System.out.println("SeriesLibrary server connected to client: " + id);
            SeriesSeasonLibraryTCPJsonRPCServer myServerThread =
               new SeriesSeasonLibraryTCPJsonRPCServer(sock, id++, sl);
            myServerThread.start();
         }
      } catch(Exception e) {e.printStackTrace();}
   }









}