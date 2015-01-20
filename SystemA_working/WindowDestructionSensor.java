/******************************************************************************************************************
 * File:windowDestructionSensor.java
 * Course: 17655
 * Project: Assignment A3
 * Copyright: Copyright (c) 2009 Carnegie Mellon University
 * Versions:
 *	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
 *
 * Description:
 *
 * This class simulates a window destruction sensor. It polls the message manager for messages corresponding to changes in state
 * of the window destruction and reacts to them by using this value as current value. The current
 * window destruction state is posted to the message manager.
 *
 * Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
 * on the local machine.
 *
 * Internal Methods:
 *   void PostwindowDestructionState(MessageManagerInterface ei, boolean windowstate )
 *
 ******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class WindowDestructionSensor extends AbstractDevice {
	public static void main(String args[]) {
		deviceDescription = "This class simulates a window destruction state sensor.";
		WindowDestructionSensor s = new WindowDestructionSensor();
		s.mainLoop(args);
	}

	int windowDestructionState = 0; // window destruction state: false ==
											// window is ok, true == window is not
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

		mw = new MessageWindow("window Destruction Sensor", WinPosX, WinPosY);

		mw.WriteMessage("Registered with the message manager.");

		try {
			mw.WriteMessage("   Participant id: " + em.GetMyId());
			mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime());

		} // try

		catch (Exception e) {
			mw.WriteMessage("Error:: " + e);

		} // catch

		mw.WriteMessage("\nInitializing window Destruction State Simulation::");

		mw.WriteMessage("   Initial window Destruction State:: "
				+ windowDestructionState);
	}

	public void FunctionBeforeRead() {
		// Post the current window destruction state
		PostWindowDestructionState(em, windowDestructionState);
	}

	public void HandleMessage(Message Msg) {

		if (Msg.GetMessageId() == -7) {
			if (Msg.GetMessage().equalsIgnoreCase("WS1")) // window is not ok
			{
				windowDestructionState =1;

			} // if

			if (Msg.GetMessage().equalsIgnoreCase("WS0")) // window is ok
			{
				windowDestructionState = 0;

			} // if

		} // if
	}

	public void FunctionAfterRead() {
		// Now we trend the window destruction state according to the status of
		// the
		// security controller.

		mw.WriteMessage("   Current window Destruction State:: "
				+ windowDestructionState);

	}

	/***************************************************************************
	 * CONCRETE METHOD:: PostwindowDestructionState Purpose: This method posts the
	 * specifiedwindow destruction state to the specified message manager. This
	 * method assumes an message ID of 6.
	 *
	 * Arguments: MessageManagerInterface ei - this is the messagemanger
	 * interface where the message will be posted.
	 *
	 * boolean windowState - this is the window destruction state value.
	 *
	 * Returns: none
	 *
	 * Exceptions: None
	 *
	 ***************************************************************************/

	static private void PostWindowDestructionState(MessageManagerInterface ei,
			int windowState) {
		// Here we create the message.

		Message msg = new Message((int) 7, String.valueOf(windowState));

		// Here we send the message to the message manager.

		try {
			ei.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error Posting window Destruction State:: " + e);

		} // catch

	} // PostWindowDestructionState

} // windowDestructionSensor
