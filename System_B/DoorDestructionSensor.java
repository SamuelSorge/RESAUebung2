/******************************************************************************************************************
 * File:DoorDestructionSensor.java
 * Course: 17655
 * Project: Assignment A3
 * Copyright: Copyright (c) 2009 Carnegie Mellon University
 * Versions:
 *	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
 *
 * Description:
 *
 * This class simulates a door destruction sensor. It polls the message manager for messages corresponding to changes in state
 * of the door destruction and reacts to them by using this value as current value. The current
 * door destruction state is posted to the message manager.
 *
 * Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
 * on the local machine.
 *
 * Internal Methods:
 *   void PostDoorDestructionState(MessageManagerInterface ei, boolean doorstate )
 *
 ******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class DoorDestructionSensor extends AbstractDevice {
	public static void main(String args[]) {
		deviceDescription = "This class simulates a door destruction state sensor.";
		DoorDestructionSensor s = new DoorDestructionSensor();
		s.mainLoop(args);
	}

	int doorDestructionState = 0; // Door destruction state: false ==
											// door is ok, true == door is not
											// ok

	public void InitDevice() {
		// We create a message window. Note that we place this panel about 1/2
		// across
		// and 2/3s down the screen

		// TODO use better WinPosX and WinPosY
		float WinPosX = 0.5f; // This is the X position of the message window in
								// terms
		// of a percentage of the screen height
		float WinPosY = 0.60f; // This is the Y position of the message window
								// in terms
		// of a percentage of the screen height

		mw = new MessageWindow("Door Destruction Sensor", WinPosX, WinPosY);

		mw.WriteMessage("Registered with the message manager.");

		try {
			mw.WriteMessage("   Participant id: " + em.GetMyId());
			mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime());

		} // try

		catch (Exception e) {
			mw.WriteMessage("Error:: " + e);

		} // catch

		mw.WriteMessage("\nInitializing Door Destruction State Simulation::");

		mw.WriteMessage("   Initial Door Destruction State:: "
				+ doorDestructionState);
	}

	public void FunctionBeforeRead() {
		// Post the current door destruction state
		PostDoorDestructionState(em, doorDestructionState);
	}

	public void HandleMessage(Message Msg) {

		if (Msg.GetMessageId() == -6) {
			if (Msg.GetMessage().equalsIgnoreCase("DS1")) // door is not ok
			{
				doorDestructionState = 1;

			} // if

			if (Msg.GetMessage().equalsIgnoreCase("DS0")) // door is ok
			{
				doorDestructionState = 0;

			} // if

		} // if
	}

	public void FunctionAfterRead() {
		// Now we trend the door destruction state according to the status of
		// the
		// security controller.

		mw.WriteMessage("   Current Door Destruction State:: "
				+ doorDestructionState);

	}

	/***************************************************************************
	 * CONCRETE METHOD:: PostDoorDestructionState Purpose: This method posts the
	 * specifieddoor destruction state to the specified message manager. This
	 * method assumes an message ID of 6.
	 *
	 * Arguments: MessageManagerInterface ei - this is the messagemanger
	 * interface where the message will be posted.
	 *
	 * boolean doorState - this is the door destruction state value.
	 *
	 * Returns: none
	 *
	 * Exceptions: None
	 *
	 ***************************************************************************/

	static private void PostDoorDestructionState(MessageManagerInterface ei,
			int doorState) {
		// Here we create the message.

		Message msg = new Message((int) 6, String.valueOf(doorState));

		// Here we send the message to the message manager.

		try {
			ei.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error Posting Door Destruction State:: " + e);

		} // catch

	} // PostDoorDestructionState

} // DoorDestructionSensor
