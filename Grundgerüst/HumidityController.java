/******************************************************************************************************************
* File:HumidityController.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a device that controls a humidifier and dehumidifier. It polls the message manager for message
* ids = 4 and reacts to them by turning on or off the humidifier/dehumidifier. The following command are valid
* strings for controlling the humidifier and dehumidifier:
*
*	H1 = humidifier on
*	H0 = humidifier off
*	D1 = dehumidifier on
*	D0 = dehumidifier off
*
* The state (on/off) is graphically displayed on the terminal in the indicator. Command messages are displayed in
* the message window. Once a valid command is recieved a confirmation message is sent with the id of -5 and the command in
* the command string.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	static private void ConfirmMessage(MessageManagerInterface ei, String m )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class HumidityController extends AbstractDevice
{
    public static int main(String args[])
    {
        HumidityController c = new HumidityController();
        return c.mainLoop(args);
    }
    MessageManagerInterface em = null;	// Interface object to the message manager
    boolean HumidifierState = false;	// Heater state: false == off, true == on
    boolean DehumidifierState = false;	// Dehumidifier state: false == off, true == on

    Indicator hi;
    Indicator di;

    public void InitDevice()
    {
        System.out.println("Registered with the message manager." );

        /* Now we create the humidity control status and message panel
         ** We put this panel about 2/3s the way down the terminal, aligned to the left
         ** of the terminal. The status indicators are placed directly under this panel
         */

        float WinPosX = 0.0f; 	//This is the X position of the message window in terms
        //of a percentage of the screen height
        float WinPosY = 0.60f;	//This is the Y position of the message window in terms
        //of a percentage of the screen height

        mw = new MessageWindow("Humidity Controller Status Console", WinPosX, WinPosY);

        // Now we put the indicators directly under the humitity status and control panel

        hi = new Indicator ("Humid OFF", mw.GetX(), mw.GetY()+mw.Height());
        di = new Indicator ("DeHumid OFF", mw.GetX()+(hi.Width()*2), mw.GetY()+mw.Height());

        mw.WriteMessage("Registered with the message manager." );

        try
        {
            mw.WriteMessage("   Participant id: " + em.GetMyId() );
            mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

        } // try

        catch (Exception e)
        {
            System.out.println("Error:: " + e);

        } // catch
    }

    public void FunctionBeforeRead()
    {
        ; //Dummy
    }

    public void HandleMessage(Message Msg)
    {
        if ( Msg.GetMessageId() == 4 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("H1")) // humidifier on
            {
                HumidifierState = true;
                mw.WriteMessage("Received humidifier on message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "H1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("H0")) // humidifier off
            {
                HumidifierState = false;
                mw.WriteMessage("Received humidifier off message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "H0" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("D1")) // dehumidifier on
            {
                DehumidifierState = true;
                mw.WriteMessage("Received dehumidifier on message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "D1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("D0")) // dehumidifier off
            {
                DehumidifierState = false;
                mw.WriteMessage("Received dehumidifier off message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "D0" );

            } // if

        } // if
    }

    public void FunctionAfterRead()
    {
        // Update the lamp status

        if (HumidifierState)
        {
            // Set to green, humidifier is on

            hi.SetLampColorAndMessage("HUMID ON", 1);

        } else {

            // Set to black, humidifier is off
            hi.SetLampColorAndMessage("HUMID OFF", 0);

        } // if

        if (DehumidifierState)
        {
            // Set to green, dehumidifier is on

            di.SetLampColorAndMessage("DEHUMID ON", 1);

        } else {

            // Set to black, dehumidifier is off

            di.SetLampColorAndMessage("DEHUMID OFF", 0);

        } // if
    }

    /***************************************************************************
     * CONCRETE METHOD:: ConfirmMessage
     * Purpose: This method posts the specified message to the specified message
     * manager. This method assumes an message ID of -4 which indicates a confirma-
     * tion of a command.
     *
     * Arguments: MessageManagerInterface ei - this is the messagemanger interface
     *			 where the message will be posted.
     *
     *			 string m - this is the received command.
     *
     * Returns: none
     *
     * Exceptions: None
     *
     ***************************************************************************/

    static private void ConfirmMessage(MessageManagerInterface ei, String m )
    {
        // Here we create the message.

        Message msg = new Message( (int) -4, m );

        // Here we send the message to the message manager.

        try
        {
            ei.SendMessage( msg );

        } // try

        catch (Exception e)
        {
            System.out.println("Error Confirming Message:: " + e);

        } // catch

    } // PostMessage

} // HumidityControllers
