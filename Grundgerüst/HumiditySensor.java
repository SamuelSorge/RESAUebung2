/******************************************************************************************************************
* File:HumiditySensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a humidity sensor. It polls the message manager for messages corresponding to changes in state
* of the humidifier or dehumidifier and reacts to them by trending the relative humidity up or down. The current
* relative humidity is posted to the message manager.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	float GetRandomNumber()
*	boolean CoinToss()
*   void PostHumidity(MessageManagerInterface ei, float humidity )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class HumiditySensor extends AbstractDevice
{
	public static int main(String args[])
	{
        HumiditySensor s = new HumiditySensor();
        return s.mainLoop(args);
    }

		boolean HumidifierState = false;	// Humidifier state: false == off, true == on
		boolean DehumidifierState = false;	// Dehumidifier state: false == off, true == on
		float RelativeHumidity;				// Current simulated ambient room humidity
		float DriftValue;					// The amount of humidity gained or lost

    public void InitDevice()
    {
			// We create a message window. Note that we place this panel about 1/2 across
			// and 2/3s down the screen

			float WinPosX = 0.5f; 	//This is the X position of the message window in terms
									//of a percentage of the screen height
			float WinPosY = 0.60f;	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height

			mw = new MessageWindow("Humidity Sensor", WinPosX, WinPosY);

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

			mw.WriteMessage("\nInitializing Humidity Simulation::" );

			RelativeHumidity = GetRandomNumber() * (float) 100.00;

			if ( CoinToss() )
			{
				DriftValue = GetRandomNumber() * (float) -1.0;

			} else {

				DriftValue = GetRandomNumber();

			} // if

			mw.WriteMessage("   Initial Humidity Set:: " + RelativeHumidity );
			mw.WriteMessage("   Drift Value Set:: " + DriftValue );
        }

        public void FunctionBeforeRead()
        {
            // Post the current relative humidity

				PostHumidity( em, RelativeHumidity );

				mw.WriteMessage("Current Relative Humidity:: " + RelativeHumidity + "%");
        }

        public void HandleMessage(Message Msg)
        {
					if ( Msg.GetMessageId() == -4 )
					{
						if (Msg.GetMessage().equalsIgnoreCase("H1")) // humidifier on
						{
							HumidifierState = true;

						} // if

						if (Msg.GetMessage().equalsIgnoreCase("H0")) // humidifier off
						{
							HumidifierState = false;

						} // if

						if (Msg.GetMessage().equalsIgnoreCase("D1")) // dehumidifier on
						{
							DehumidifierState = true;

						} // if

						if (Msg.GetMessage().equalsIgnoreCase("D0")) // dehumidifier off
						{
							DehumidifierState = false;

						} // if

					} // if
        }

        public void FunctionAfterRead()
        {
				// Now we trend the relative humidity according to the status of the
				// humidifier/dehumidifier controller.

				if (HumidifierState)
				{
					RelativeHumidity += GetRandomNumber();

				} // if humidifier is on

				if (!HumidifierState && !DehumidifierState)
				{
					RelativeHumidity += DriftValue;

				} // if both the humidifier and dehumidifier are off

				if (DehumidifierState)
				{
					RelativeHumidity -= GetRandomNumber();

				} // if dehumidifier is on

        }

    /***************************************************************************
	* CONCRETE METHOD:: GetRandomNumber
	* Purpose: This method provides the simulation with random floating point
	*		   humidity values between 0.1 and 0.9.
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
	* CONCRETE METHOD:: PostHumidity
	* Purpose: This method posts the specified relative humidity value to the
	* specified message manager. This method assumes an message ID of 2.
	*
	* Arguments: MessageManagerInterface ei - this is the messagemanger interface
	*			 where the message will be posted.
	*
	*			 float humidity - this is the humidity value.
	*
	* Returns: none
	*
	* Exceptions: None
	*
	***************************************************************************/

	static private void PostHumidity(MessageManagerInterface ei, float humidity )
	{
		// Here we create the message.

		Message msg = new Message( (int) 2, String.valueOf(humidity) );

		// Here we send the message to the message manager.

		try
		{
			ei.SendMessage( msg );
			//mw.WriteMessage( "Sent Humidity Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Relative Humidity:: " + e );

		} // catch

	} // PostHumidity

} // Humidity Sensor
