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

class SecurityController extends AbstractDevice
{
    public static void main(String args[])
    {
        deviceDescription = "This class simulates a device that controls a humidifier and dehumidifier.";
        SecurityController c = new SecurityController();
        c.mainLoop(args);
    }
    boolean isArmed = false;
    boolean doorState = false;
    boolean windowState = false;
    boolean motionState = false;

    Indicator aci; // alarm system
    Indicator dai; // door
    Indicator wai; // window
    Indicator mai; // motion

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

        mw = new MessageWindow("Security Controller Status Console", WinPosX, WinPosY);

        // Now we put the indicators directly under the security status and control panel

        aci = new Indicator ("Alarm System OFF", mw.GetX(), mw.GetY()+mw.Height());
        dai = new Indicator ("Door Alarm OFF", mw.GetX()+(aci.Width()*2), mw.GetY()+mw.Height());
        wai = new Indicator ("Window Alarm OFF", mw.GetX()+(dai.Width()*2), mw.GetY()+mw.Height());
        mai = new Indicator ("Motion Alarm OFF", mw.GetX()+(wai.Width()*2), mw.GetY()+mw.Height());
        

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
        if ( Msg.GetMessageId() == 16 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("DS1")) // door alarm on
            {
                doorState = true;
                mw.WriteMessage("Received door destruction state not ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "DS1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("DS0")) // door alarm off
            {
                doorState = false;
                mw.WriteMessage("Received door destruction state ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "DS0" );

            } // if

        } // if
        
        if ( Msg.GetMessageId() == 17 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("WS1")) // window alarm on
            {
                windowState = true;
                mw.WriteMessage("Received window destruction state not ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "WS1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("WS0")) // window alarm off
            {
                windowState = false;
                mw.WriteMessage("Received window destruction state ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "WS0" );

            } // if

        } // if
        
        if ( Msg.GetMessageId() == 18 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("MS1")) // door alarm on
            {
                motionState = true;
                mw.WriteMessage("Received motion detection state not ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "MS1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("MS0")) // door alarm off
            {
                motionState = false;
                mw.WriteMessage("Received motion detection state ok message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "MS0" );

            } // if

        } // if
        
        if ( Msg.GetMessageId() == 12 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("AS1")) // alarm system on
            {
                isArmed = true;
                mw.WriteMessage("Received alarm system state on message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "AS1" );

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("AS0")) // alarm system off
            {
                isArmed = false;
                mw.WriteMessage("Received alarm system state off message" );

                // Confirm that the message was recieved and acted on

                ConfirmMessage( em, "AS0" );

            } // if

        } // if
        
    }

    public void FunctionAfterRead()
    {
        // Update the lamp status

        if (isArmed)
        {
            // Set to green, humidifier is on

            aci.SetLampColorAndMessage("Alarm System ON", 1);

        } else {

            // Set to black, humidifier is off
            aci.SetLampColorAndMessage("Alarm System OFF", 0);

        } // if

        if (doorState && isArmed)
        {
            // Set to green, dehumidifier is on

            dai.SetLampColorAndMessage("DOOR ALARM ON", 1);

        } else {

            // Set to black, dehumidifier is off

            dai.SetLampColorAndMessage("DOOR ALARM OFF", 0);

        } // if
        
        if (windowState && isArmed)
        {
            // Set to green, dehumidifier is on

            wai.SetLampColorAndMessage("WINDOW ALARM ON", 1);

        } else {

            // Set to black, dehumidifier is off

            wai.SetLampColorAndMessage("WINDOW ALARM OFF", 0);

        } // if
        
        if (motionState && isArmed)
        {
            // Set to green, dehumidifier is on

            mai.SetLampColorAndMessage("MOTION ALARM ON", 1);

        } else {

            // Set to black, dehumidifier is off

            mai.SetLampColorAndMessage("MOTION ALARM OFF", 0);

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

        Message msgIsArmed = new Message( (int) -12, m );
        Message msgDoorState = new Message( (int) -16, m );
        Message msgWindowState = new Message( (int) -17, m );
        Message msgMotionState = new Message( (int) -18, m );

        // Here we send the message to the message manager.

        try
        {
        	for (int i=0;i<4;i++){
        	ei.SendMessage( msgIsArmed );
            ei.SendMessage( msgDoorState );
            ei.SendMessage( msgWindowState );
            ei.SendMessage( msgMotionState );}

        } // try

        catch (Exception e)
        {
            System.out.println("Error Confirming Message:: " + e);

        } // catch

    } // PostMessage

} // HumidityControllers
