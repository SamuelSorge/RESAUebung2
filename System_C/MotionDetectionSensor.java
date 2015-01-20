/******************************************************************************************************************
 * File:motionDetectionSensor.java
 * Course: 17655
 * Project: Assignment A3
 * Copyright: Copyright (c) 2009 Carnegie Mellon University
 * Versions:
 *	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
 *
 * Description:
 *
 * This class simulates a motion destruction sensor. It polls the message manager for messages corresponding to changes in state
 * of the motion detection and reacts to them by using this value as current value. The current
 * motion destruction state is posted to the message manager.
 *
 * Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
 * on the local machine.
 *
 * Internal Methods:
 *   void PostmotionDetectionState(MessageManagerInterface ei, boolean motionState )
 *
 ******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class MotionDetectionSensor extends AbstractDevice {
	public static void main(String args[]) {
		deviceDescription = "This class simulates a motion detection state sensor.";
		MotionDetectionSensor s = new MotionDetectionSensor();
		s.mainLoop(args);
	}

	int motionDetectionState = 0; // motion destruction state: false ==
											// motion detected nothing (is ok), true == motion detected sth. (is not ok)

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

		mw = new MessageWindow("motion Destruction Sensor", WinPosX, WinPosY);

		mw.WriteMessage("Registered with the message manager.");

		try {
			mw.WriteMessage("   Participant id: " + em.GetMyId());
			mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime());

		} // try

		catch (Exception e) {
			mw.WriteMessage("Error:: " + e);

		} // catch

		mw.WriteMessage("\nInitializing Motion Detection State Simulation::");

		mw.WriteMessage("   Initial Motion Detection State:: "
				+ motionDetectionState);
	}

	public void FunctionBeforeRead() {
		// Post the current motion detection state
		PostMotionDetectionState(em, motionDetectionState);
	}

	public void HandleMessage(Message Msg) {

		if (Msg.GetMessageId() == -8) {
			if (Msg.GetMessage().equalsIgnoreCase("MS1")) // motion detection is not ok
			{
				motionDetectionState = 1;

			} // if

			if (Msg.GetMessage().equalsIgnoreCase("MS0")) // motion detection is ok
			{
				motionDetectionState = 0;

			} // if

		} // if
	}

	public void FunctionAfterRead() {
		// Now we trend the motion detection state according to the status of
		// the
		// security controller.

		mw.WriteMessage("   Current Motion Detection State:: "
				+ motionDetectionState);

	}

	/***************************************************************************
	 * CONCRETE METHOD:: PostmotionDetectionState Purpose: This method posts the
	 * specified motion detection state to the specified message manager. This
	 * method assumes an message ID of 6.
	 *
	 * Arguments: MessageManagerInterface ei - this is the messagemanager
	 * interface where the message will be posted.
	 *
	 * boolean motionState - this is the motion destruction state value.
	 *
	 * Returns: none
	 *
	 * Exceptions: None
	 *
	 ***************************************************************************/

	static private void PostMotionDetectionState(MessageManagerInterface ei,
			int motionState) {
		// Here we create the message.

		Message msg = new Message((int) 8, String.valueOf(motionState));

		// Here we send the message to the message manager.

		try {
			ei.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error Posting Motion Detection State:: " + e);

		} // catch

	} // PostMotionDetectionState

} // motionDetectionSensor
