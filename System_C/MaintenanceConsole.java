/******************************************************************************************************************
* File:MaintenanceConsole.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This is the service maintenance console, which just shows all devices
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import TermioPackage.*;
import java.util.*;

class MaintenanceConsoleThread extends Thread
{
    private MaintenanceConsole console;
    MaintenanceConsoleThread(MaintenanceConsole c)
    {
        console = c;
    }

    public void run()
    {
    	Termio UserInput = new Termio();	// Termio IO Object
		String Option = null;				// Menu choice from user
        while(!console.Done)
        {
            System.out.println( "Select an Option: \n" );
            System.out.println( "1: Show devices" );
            System.out.println( "Q: Quit\n" );
            System.out.print( "\n>>>> " );
            Option = UserInput.KeyboardReadString();

            if ( Option.equals( "1" ) )
            {
                System.out.println("");
                System.out.println("---------------");
                System.out.println("Online devices:");
                for (Map.Entry<Long,Device> entry : console.devices.entrySet())
                {
                    if(entry.getValue().lastSeen + 5000 > System.currentTimeMillis())
                    {
                        entry.getValue().print();
                    }
                }

                System.out.println("");
                System.out.println("Offline devices:");
                for (Map.Entry<Long,Device> entry : console.devices.entrySet())
                {
                    if(entry.getValue().lastSeen + 5000 <= System.currentTimeMillis())
                    {
                        entry.getValue().print();
                    }
                }
            }

            if ( Option.equals( "Q" ) )
            {
                console.Done = true;
            }
        }
    }
}


class Device
{
    String name;
    String desc;
    long id;
    long lastSeen;

    void print()
    {
        System.out.println("Device ID:"+id+"\tName:"+name+"\tDesc:\n\t"+desc);
    }
}

class MaintenanceConsole extends AbstractDevice
{
    public void doConsoleWork()
    {
        consoleThread.start();
        Message msg = new Message( (int) 100, "ping" );
        while(!Done)
        {
            try
            {
                em.SendMessage( msg );
                Thread.sleep( 100 );
                doWork();
                Thread.sleep( 2000 );
            } // try
            catch (Exception e)
            {
                mw.WriteMessage("Error: " + e);
            } // catch
        }
        mw.Close();
    }

    MaintenanceConsoleThread consoleThread;

    MaintenanceConsole()
    {
        consoleThread = new MaintenanceConsoleThread(this);
    }

    public static void main(String args[])
    {
        deviceDescription = "This class is the maintainance console.";
        MaintenanceConsole s = new MaintenanceConsole();
        s.InitDeviceLocal(args);
        s.doConsoleWork();
    }

    Map<Long,Device> devices;

    public void InitDevice()
    {
        devices = new HashMap<Long,Device>();
        // We create a message window. Note that we place this panel about 1/2 across
        // and 2/3s down the screen

        float WinPosX = 0.5f; 	//This is the X position of the message window in terms
        //of a percentage of the screen height
        float WinPosY = 0.60f;	//This is the Y position of the message window in terms
        //of a percentage of the screen height

        mw = new MessageWindow("Maintainanvce Console", WinPosX, WinPosY);

        mw.WriteMessage("Registered with the message manager." );

        try
        {
            mw.WriteMessage("   Participant id: " + em.GetMyId() );
            mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

        } // try

        catch (Exception e)
        {
            mw.WriteMessage("Error:: " + e);

        } // catch

        mw.WriteMessage("\nInitializing Maintainance Console::" );
    }

    public void FunctionBeforeRead()
    {
    }

    public void HandleMessage(Message Msg)
    {
        if ( Msg.GetMessageId() == 101 )
        {
            String[] parts = Msg.GetMessage().split("\\|");
            Device d = new Device();
            d.id = Msg.GetSenderId();
            d.name = parts[0];
            if(parts.length >= 2)
                d.desc = parts[1];
            d.lastSeen = System.currentTimeMillis();
            devices.put(d.id, d);
        } // if
    }

    public void FunctionAfterRead()
    {
    }
}
