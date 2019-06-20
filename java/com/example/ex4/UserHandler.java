package com.example.ex4;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

public class UserHandler
{
    private String aileron_path;
    private String elevator_path;
    private volatile String connection_ip;
    private volatile int connection_port;
    private volatile boolean stop;
    private volatile String message;
    private Thread message_thread;
    //static instance to be returned by getInstance()
    private static UserHandler instance = null;
    //get instance of the this singleton UserHandler
    public static UserHandler getInstance()
    {
        if (instance == null)
        {
            instance = new UserHandler();
        }
        return instance;
    }
    //CTOR
    private UserHandler()
    {
        aileron_path = "/controls/flight/aileron";
        elevator_path = "/controls/flight/elevator";
        connection_ip = null;
        connection_port = 0;
        stop = false;
        message = null;
        message_thread = null;
    }
    //connect to FlightGear simulator
    public void connect(String ip, int port)
    {
        connection_ip = ip;
        connection_port = port;
        message_thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Socket sock = null;
                PrintWriter writer = null;
                try
                {
                    sock = new Socket(InetAddress.getByName(connection_ip), connection_port);
                    writer = new PrintWriter(new BufferedWriter((new OutputStreamWriter(sock.getOutputStream()))), true);
                    //wait for message to send...
                    while (!stop)
                    {
                        synchronized (this)
                        {
                            if (message != null) {
                                writer.print(message);
                                message = null; }
                        }
                    }
                    sock.close();
                    sock = null;
                    writer.close();
                    writer = null;
                }
                catch (Exception e)
                {
                    if (sock != null) { try { sock.close(); } catch (Exception ex) { ex.printStackTrace(); }}
                    if (writer != null) { writer.close(); }
                    e.printStackTrace();
                    return;
                }
            }
        });
        message_thread.start();
    }
    //disconnect from FlightGear simulator
    public void disconnect()
    {
        stop = true;
        try
        {
            message_thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //send message to FlightGear
    public void send_message(boolean aileron, float val)
    {
        String aileron_or_elevator = elevator_path;
        if (aileron)
        {
            aileron_or_elevator = aileron_path;
        }
        synchronized (this)
        {
            message = "set " + aileron_or_elevator + " " + Float.toString(val) + " \r\n";
        }
    }
}