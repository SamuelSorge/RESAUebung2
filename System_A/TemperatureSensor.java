/******************************************************************************************************************
* File:TemperatureSensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a temperature sensor. It polls the message manager for messages corresponding to changes in state
* of the heater or chiller and reacts to them by trending the ambient temperature up or down. The current ambient
* room temperature is posted to the message manager.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	float GetRandomNumber()
*	boolean CoinToss()
*   void PostTemperature(MessageManagerInterface ei, float temperature )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class TemperatureSensor extends AbstractDevice
{
    public static void main(String args[])
    {
        deviceDescription = "This class simulates a temperature sensor";
        TemperatureSensor s = new TemperatureSensor();
        s.mainLoop(args);
    }

    boolean HeaterState = false;	// Heater state: false == off, true == on
    boolean ChillerState = false;	// Chiller state: false == off, true == on
    float CurrentTemperature;		// Current simulated ambient room temperature
    float DriftValue;				// The amount of temperature gained or lost


    public void InitDevice()
    {
        // We create a message window. Note that we place this panel about 1/2 across
        // and 1/3 down the screen

        float WinPosX = 0.5f; 	//This is the X position of the message window in terms
        //of a percentage of the screen height
        float WinPosY = 0.3f; 	//This is the Y position of the message window in terms
        //of a percentage of the screen height

        mw = new MessageWindow("Temperature Sensor", WinPosX, WinPosY );

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

        mw.WriteMessage("\nInitializing Temperature Simulation::" );

        CurrentTemperature = GetRandomNumber() * (float) 100.00;

        if ( CoinToss() )
        {
            DriftValue = GetRandomNumber() * (float) -1.0;

        } else {

            DriftValue = GetRandomNumber();

        } // if

        mw.WriteMessage("   Initial Temperature Set:: " + CurrentTemperature );
        mw.WriteMessage("   Drift Value Set:: " + DriftValue );
    }

    public void FunctionBeforeRead()
    {
        // Post the current temperature

        PostTemperature( em, CurrentTemperature );

        mw.WriteMessage("Current Temperature::  " + CurrentTemperature + " F");
    }
    public void HandleMessage(Message Msg)
    {
        if ( Msg.GetMessageId() == -5 )
        {
            if (Msg.GetMessage().equalsIgnoreCase("H1")) // heater on
            {
                HeaterState = true;

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("H0")) // heater off
            {
                HeaterState = false;

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("C1")) // chiller on
            {
                ChillerState = true;

            } // if

            if (Msg.GetMessage().equalsIgnoreCase("C0")) // chiller off
            {
                ChillerState = false;

            } // if

        } // if
    }


    public void FunctionAfterRead()
    {
        // Now we trend the temperature according to the status of the
        // heater/chiller controller.

        if (HeaterState)
        {
            CurrentTemperature += GetRandomNumber();

        } // if heater is on

        if (!HeaterState && !ChillerState)
        {
            CurrentTemperature += DriftValue;

        } // if both the heater and chiller are off

        if (ChillerState)
        {
            CurrentTemperature -= GetRandomNumber();

        } // if chiller is on

        // Here we wait for a 2.5 seconds before we start the next sample
    }

    /***************************************************************************
     * CONCRETE METHOD:: GetRandomNumber
     * Purpose: This method provides the simulation with random floating point
     *		   temperature values between 0.1 and 0.9.
     *
     * Arguments: None.
     *
     * Returns: float
     *
     * Exceptions: None
     *
     ***************************************************************************/

    static private float GetRandomNumber()
    {
        Random r = new Random();
        Float Val;

        Val = Float.valueOf((float)-1.0);

        while( Val < 0.1 )
        {
            Val = r.nextFloat();
        }

        return( Val.floatValue() );

    } // GetRandomNumber

    /***************************************************************************
     * CONCRETE METHOD:: CoinToss
     * Purpose: This method provides a random true or false value used for
     * determining the positiveness or negativeness of the drift value.
     *
     * Arguments: None.
     *
     * Returns: boolean
     *
     * Exceptions: None
     *
     ***************************************************************************/

    static private boolean CoinToss()
    {
        Random r = new Random();

        return(r.nextBoolean());

    } // CoinToss

    /***************************************************************************
     * CONCRETE METHOD:: PostTemperature
     * Purpose: This method posts the specified temperature value to the
     * specified message manager. This method assumes an message ID of 1.
     *
     * Arguments: MessageManagerInterface ei - this is the messagemanger interface
     *			 where the message will be posted.
     *
     *			 float temperature - this is the temp value.
     *
     * Returns: none
     *
     * Exceptions: None
     *
     ***************************************************************************/

    static private void PostTemperature(MessageManagerInterface ei, float temperature )
    {
        // Here we create the message.

        Message msg = new Message( (int) 1, String.valueOf(temperature) );

        // Here we send the message to the message manager.

        try
        {
            ei.SendMessage( msg );
            //System.out.println( "Sent Temp Message" );

        } // try

        catch (Exception e)
        {
            System.out.println( "Error Posting Temperature:: " + e );

        } // catch

    } // PostTemperature

} // TemperatureSensor
