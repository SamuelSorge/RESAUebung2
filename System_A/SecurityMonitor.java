/******************************************************************************************************************
 * File:ECSMonitor.java
 * Course: 17655
 * Project: Assignment A3
 * Copyright: Copyright (c) 2009 Carnegie Mellon University
 * Versions:
 *	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
 *
 * Description:
 *
 * This class monitors the environmental control systems that control museum temperature and humidity. In addition to
 * monitoring the temperature and humidity, the ECSMonitor also allows a user to set the humidity and temperature
 * ranges to be maintained. If temperatures exceed those limits over/under alarm indicators are triggered.
 *
 * Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
 * on the local machine.
 *
 * Internal Methods:
 *	static private void Heater(MessageManagerInterface ei, boolean ON )
 *	static private void Chiller(MessageManagerInterface ei, boolean ON )
 *	static private void Humidifier(MessageManagerInterface ei, boolean ON )
 *	static private void Dehumidifier(MessageManagerInterface ei, boolean ON )
 *
 ******************************************************************************************************************/



class SecurityMonitor extends Thread {
	private MessageManagerInterface em = null; // Interface object to the
												// message manager
	private String MsgMgrIP = null; // Message Manager IP address
	MessageWindow mw = null; // This is the message window
	boolean Registered = true; // Signifies that this class is registered with
	// an message manager.
	
	/**
	private float TempRangeHigh = 100; // These parameters signify the
										// temperature and humidity ranges in
										// terms
	private float TempRangeLow = 0; // of high value and low values. The
									// ECSmonitor will attempt to maintain
	private float HumiRangeHigh = 100; // this temperature and humidity.
										// Temperatures are in degrees
										// Fahrenheit
	private float HumiRangeLow = 0; // and humidity is in relative humidity
									// percentage.

	
	Indicator ti; // Temperature indicator
	Indicator hi; // Humidity indicator
	*/
	
	Indicator doorIndicator;
	Indicator windowIndicator;
	Indicator motionIndicator;
	
	
	
	
	public SecurityMonitor() {
		// message manager is on the local system

		try {
			// Here we create an message manager interface object. This assumes
			// that the message manager is on the local machine

			em = new MessageManagerInterface();

		}

		catch (Exception e) {
			System.out
					.println("ECSMonitor::Error instantiating message manager interface: "
							+ e);
			Registered = false;

		} // catch

	} // Constructor

	public SecurityMonitor(String MsgIpAddress) {
		// message manager is not on the local system

		MsgMgrIP = MsgIpAddress;

		try {
			// Here we create an message manager interface object. This assumes
			// that the message manager is NOT on the local machine

			em = new MessageManagerInterface(MsgMgrIP);
		}

		catch (Exception e) {
			System.out
					.println("ECSMonitor::Error instantiating message manager interface: "
							+ e);
			Registered = false;

		} // catch

	} // Constructor

	public void run() {
		Message Msg = null; // Message object
		MessageQueue eq = null; // Message Queue
		int MsgId = 0; // User specified message ID
		
//		float CurrentTemperature = 0; // Current temperature as reported by the
										// temperature sensor
//		float CurrentHumidity = 0; // Current relative humidity as reported by
									// the humidity sensor
		boolean currentDoorState = false;
		
		int Delay = 1000; // The loop delay (1 second)
		boolean Done = false; // Loop termination flag
		boolean ON = true; // Used to turn on heaters, chillers, humidifiers,
							// and dehumidifiers
		boolean OFF = false; // Used to turn off heaters, chillers, humidifiers,
								// and dehumidifiers

		if (em != null) {
			// Now we create the ECS status and message panel
			// Note that we set up two indicators that are initially yellow.
			// This is
			// because we do not know if the temperature/humidity is high/low.
			// This panel is placed in the upper left hand corner and the status
			// indicators are placed directly to the right, one on top of the
			// other

			mw = new MessageWindow("Security Console", 0, 0);
			doorIndicator = new Indicator("Door UNK", mw.GetX() + mw.Width(), 0);
			windowIndicator = new Indicator("Window UNK", mw.GetX() + mw.Width(),
					(int) (mw.Height() / 2), 2);

			mw.WriteMessage("Registered with the message manager.");

			try {
				mw.WriteMessage("   Participant id: " + em.GetMyId());
				mw.WriteMessage("   Registration Time: "
						+ em.GetRegistrationTime());

			} // try

			catch (Exception e) {
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			 ** Here we start the main simulation loop
			 *********************************************************************/

			while (!Done) {
				// Here we get our message queue from the message manager

				try {
					eq = em.GetMessageQueue();

				} // try

				catch (Exception e) {
					mw.WriteMessage("Error getting message queue::" + e);

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 1 or 2. Message IDs of 1 are
				// temperature
				// readings from the temperature sensor; message IDs of 2 are
				// humidity sensor
				// readings. Note that we get all the messages at once... there
				// is a 1
				// second delay between samples,.. so the assumption is that
				// there should
				// only be a message at most. If there are more, it is the last
				// message
				// that will effect the status of the temperature and humidity
				// controllers
				// as it would in reality.

				int qlen = eq.GetSize();

				for (int i = 0; i < qlen; i++) {
					Msg = eq.GetMessage();

					if (Msg.GetMessageId() == 7) // Door state reading
					{
						try {
							currentDoorState = Boolean.valueOf(Msg.GetMessage()).booleanValue();
//							CurrentTemperature = Float
//									.valueOf(Msg.GetMessage()).floatValue();

						} // try

						catch (Exception e) {
							mw.WriteMessage("Error reading temperature: " + e);

						} // catch

					} // if

					// If the message ID == 99 then this is a signal that the
					// simulation
					// is to end. At this point, the loop termination flag is
					// set to
					// true and this process unregisters from the message
					// manager.

					if (Msg.GetMessageId() == 99) {
						Done = true;

						try {
							em.UnRegister();

						} // try

						catch (Exception e) {
							mw.WriteMessage("Error unregistering: " + e);

						} // catch

						mw.WriteMessage("\n\nSimulation Stopped. \n");

						// Get rid of the indicators. The message panel is left
						// for the
						// user to exit so they can see the last message posted.

						doorIndicator.dispose();
						windowIndicator.dispose();

					} // if

				} // for

				//TODO: add other states
				mw.WriteMessage("Door state:: " + currentDoorState);


				// This delay slows down the sample rate to Delay milliseconds

				try {
					Thread.sleep(Delay);

				} // try

				catch (Exception e) {
					System.out.println("Sleep error:: " + e);

				} // catch

			} // while

		} else {

			System.out
					.println("Unable to register with the message manager.\n\n");

		} // if

	} // main

	/***************************************************************************
	 * CONCRETE METHOD:: IsRegistered Purpose: This method returns the
	 * registered status
	 *
	 * Arguments: none
	 *
	 * Returns: boolean true if registered, false if not registered
	 *
	 * Exceptions: None
	 *
	 ***************************************************************************/

	public boolean IsRegistered() {
		return (Registered);

	} // SetTemperatureRange



	/***************************************************************************
	 * CONCRETE METHOD:: Halt Purpose: This method posts an message that stops
	 * the environmental control system.
	 *
	 * Arguments: none
	 *
	 * Returns: none
	 *
	 * Exceptions: Posting to message manager exception
	 *
	 ***************************************************************************/

	public void Halt() {
		mw.WriteMessage("***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***");

		// Here we create the stop message.

		Message msg;

		msg = new Message((int) 99, "XXX");

		// Here we send the message to the message manager.

		try {
			em.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt

	/***************************************************************************
	 * CONCRETE METHOD:: Heater Purpose: This method posts messages that will
	 * signal the temperature controller to turn on/off the heater
	 *
	 * Arguments: boolean ON(true)/OFF(false) - indicates whether to turn the
	 * heater on or off.
	 *
	 * Returns: none
	 *
	 * Exceptions: Posting to message manager exception
	 *
	 ***************************************************************************/

	private void Heater(boolean ON) {
		// Here we create the message.

		Message msg;

		if (ON) {
			msg = new Message((int) 7, "D1");

		} else {

			msg = new Message((int) 7, "D0");

		} // if

		// Here we send the message to the message manager.

		try {
			em.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error sending heater control message:: " + e);

		} // catch

	} // Heater

	/***************************************************************************
	 * CONCRETE METHOD:: Chiller Purpose: This method posts messages that will
	 * signal the temperature controller to turn on/off the chiller
	 *
	 * Arguments: boolean ON(true)/OFF(false) - indicates whether to turn the
	 * chiller on or off.
	 *
	 * Returns: none
	 *
	 * Exceptions: Posting to message manager exception
	 *
	 ***************************************************************************/

	private void Chiller(boolean ON) {
		// Here we create the message.

		Message msg;

		if (ON) {
			msg = new Message((int) 5, "C1");

		} else {

			msg = new Message((int) 5, "C0");

		} // if

		// Here we send the message to the message manager.

		try {
			em.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error sending chiller control message:: " + e);

		} // catch

	} // Chiller

	/***************************************************************************
	 * CONCRETE METHOD:: Humidifier Purpose: This method posts messages that
	 * will signal the humidity controller to turn on/off the humidifier
	 *
	 * Arguments: boolean ON(true)/OFF(false) - indicates whether to turn the
	 * humidifier on or off.
	 *
	 * Returns: none
	 *
	 * Exceptions: Posting to message manager exception
	 *
	 ***************************************************************************/

	private void Humidifier(boolean ON) {
		// Here we create the message.

		Message msg;

		if (ON) {
			msg = new Message((int) 4, "H1");

		} else {

			msg = new Message((int) 4, "H0");

		} // if

		// Here we send the message to the message manager.

		try {
			em.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error sending humidifier control message::  "
					+ e);

		} // catch

	} // Humidifier

	/***************************************************************************
	 * CONCRETE METHOD:: Deumidifier Purpose: This method posts messages that
	 * will signal the humidity controller to turn on/off the dehumidifier
	 *
	 * Arguments: boolean ON(true)/OFF(false) - indicates whether to turn the
	 * dehumidifier on or off.
	 *
	 * Returns: none
	 *
	 * Exceptions: Posting to message manager exception
	 *
	 ***************************************************************************/

	private void Dehumidifier(boolean ON) {
		// Here we create the message.

		Message msg;

		if (ON) {
			msg = new Message((int) 4, "D1");

		} else {

			msg = new Message((int) 4, "D0");

		} // if

		// Here we send the message to the message manager.

		try {
			em.SendMessage(msg);

		} // try

		catch (Exception e) {
			System.out.println("Error sending dehumidifier control message::  "
					+ e);

		} // catch

	} // Dehumidifier

} // ECSMonitor