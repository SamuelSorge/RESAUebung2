

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

class SecurityController extends AbstractDevice {
	public static int main(String args[]) {
		SecurityController c = new SecurityController();
		return c.mainLoop(args);
	}

	boolean doorBreakState = false;
	boolean windowBreakState = false;
	boolean motionDetectionState = false;
	boolean isArmed = false;
	
	MessageManagerInterface em = null; // Interface object to the message
										// manager
	Indicator doorIndicator;
	Indicator windowIndicator;
	Indicator motionIndicator;
	Indicator alarmSystemIndicator;
	Indicator alarmIndicator;

	public void InitDevice() {
		System.out.println("Registered with the message manager.");

		/*
		 * Now we create the humidity control status and message panel* We put
		 * this panel about 2/3s the way down the terminal, aligned to the left*
		 * of the terminal. The status indicators are placed directly under this
		 * panel
		 */

		float WinPosX = 0.0f; // This is the X position of the message window in
								// terms
		// of a percentage of the screen height
		float WinPosY = 0.60f; // This is the Y position of the message window
								// in terms
		// of a percentage of the screen height

		mw = new MessageWindow("Humidity Controller Status Console", WinPosX,
				WinPosY);

		// Now we put the indicators directly under the humitity status and
		// control panel

		doorIndicator = new Indicator("Door OFF", mw.GetX(), mw.GetY() + mw.Height());
		windowIndicator = new Indicator("Window OFF", mw.GetX() + (doorIndicator.Width() * 2),
				mw.GetY() + mw.Height());
		motionIndicator = new Indicator("Motion OFF", mw.GetX() + (windowIndicator.Width() * 2),
				mw.GetY() + mw.Height());
		alarmSystemIndicator = new Indicator("AlarmSysten OFF", mw.GetX() + (motionIndicator.Width() * 2),
				mw.GetY() + mw.Height());
		alarmIndicator = new Indicator("Alarm", mw.GetX() + (alarmSystemIndicator.Width() * 2),
				mw.GetY() + mw.Height());


		mw.WriteMessage("Registered with the message manager.");

		try {
			mw.WriteMessage("   Participant id: " + em.GetMyId());
			mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime());

		} // try

		catch (Exception e) {
			System.out.println("Error:: " + e);

		} // catch
	}

	public void FunctionBeforeRead() {
		; // Dummy
	}

	public void HandleMessage(Message Msg) {
		if (Msg.GetMessageId() == 7) {
			if (Msg.GetMessage().equalsIgnoreCase("D1")) // humidifier on
			{
				doorBreakState = true;
				mw.WriteMessage("Received door state alarm.");

				// Confirm that the message was recieved and acted on

				ConfirmMessage(em, "D1");

			} // if

			if (Msg.GetMessage().equalsIgnoreCase("D0")) // humidifier off
			{
				doorBreakState = false;
				mw.WriteMessage("Received door state safe.");

				// Confirm that the message was recieved and acted on

				ConfirmMessage(em, "D0");

			} // if

		} // if
		
		/**
		 * ID = 11 Alarm
		 */
		if (Msg.GetMessageId() == 11) {
			if (Msg.GetMessage().equalsIgnoreCase("A1")) // humidifier on
			{
				isArmed = true;
				mw.WriteMessage("Received alarm system is on.");

				// Confirm that the message was recieved and acted on

				ConfirmMessage(em, "A1");

			} // if

			if (Msg.GetMessage().equalsIgnoreCase("A0")) // humidifier off
			{
				isArmed = false;
				mw.WriteMessage("Received alarm system is off.");

				// Confirm that the message was recieved and acted on

				ConfirmMessage(em, "A0");

			} // if

		} // if
		
	}

	public void FunctionAfterRead() {
		// Update the lamp status

		if (doorBreakState) {
			// Set to green, humidifier is on
			doorIndicator.SetLampColorAndMessage("Door is broken", 0);
		} else {
			// Set to black, humidifier is off
			doorIndicator.SetLampColorAndMessage("Door is safe", 1);
		} // if
		
		if (windowBreakState) {
			// Set to green, humidifier is on
			windowIndicator.SetLampColorAndMessage("Window is broken", 0);
		} else {
			// Set to black, humidifier is off
			windowIndicator.SetLampColorAndMessage("Window is safe", 1);
		} // if
		
		if (motionDetectionState) {
			// Set to green, humidifier is on
			motionIndicator.SetLampColorAndMessage("Motion detected", 0);
		} else {
			// Set to black, humidifier is off
			motionIndicator.SetLampColorAndMessage("No Motion detected", 1);
		} // if
		
		if (isArmed) {
			alarmSystemIndicator.SetLampColorAndMessage("Alarmsystem on", 0);
		} else {
			alarmSystemIndicator.SetLampColorAndMessage("Alarmsystem off", 1);
		}

	}
	/***************************************************************************
	 * CONCRETE METHOD:: ConfirmMessage Purpose: This method posts the specified
	 * message to the specified message manager. This method assumes an message
	 * ID of -4 which indicates a confirma- tion of a command.
	 *
	 * Arguments: MessageManagerInterface ei - this is the messagemanger
	 * interface where the message will be posted.
	 *
	 * string m - this is the received command.
	 *
	 * Returns: none
	 *
	 * Exceptions: None
	 *
	 ***************************************************************************/

	static private void ConfirmMessage(MessageManagerInterface ei, String m) {
		// Here we create the message.

		// TODO: added Message for door, motion, window
//		Message msg = new Message((int) -4, m);
		
		Message msgDoor = new Message((int) -7, m);
		Message msgWindow = new Message((int) -8, m);
		Message msgMotion = new Message((int) -9, m);
		Message msgAlarm = new Message((int) -11, m);

		// Here we send the message to the message manager.

		// TODO: überprüfen ob alle messages gesendet werden
		try {
			ei.SendMessage(msgDoor);
			ei.SendMessage(msgWindow);
			ei.SendMessage(msgMotion);
			ei.SendMessage(msgAlarm);
		} // try

		catch (Exception e) {
			System.out.println("Error Confirming Message:: " + e);

		} // catch

	} // PostMessage

} // HumidityControllers
