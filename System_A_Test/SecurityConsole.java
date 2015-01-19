/******************************************************************************************************************
 * File:ECSConsole.java Course: 17655 Project: Assignment 3 Copyright: Copyright
 * (c) 2009 Carnegie Mellon University Versions: 1.0 February 2009 - Initial
 * rewrite of original assignment 3 (ajl).
 *
 * Description: This class is the console for the museum environmental control
 * system. This process consists of two threads. The ECSMonitor object is a
 * thread that is started that is responsible for the monitoring and control of
 * the museum environmental systems. The main thread provides a text interface
 * for the user to change the temperature and humidity ranges, as well as shut
 * down the system.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class SecurityConsole {
	public static void main(String args[]) {

		Termio UserInput = new Termio(); // Termio IO Object
		boolean Done = false; // Main loop flag
		String Option = null; // Menu choice from user
		Message Msg = null; // Message object
		boolean Error = false; // Error flag
		SecurityMonitor securityMonitor = null;

		boolean doorBreakState = false;
		boolean windowBreakState = false;
		boolean motionDetectionState = false;
		boolean isArmed = false;


		// ///////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		// ///////////////////////////////////////////////////////////////////////////////

		if (args.length != 0) {
			// message manager is not on the local system
			securityMonitor = new SecurityMonitor(args[0]);

		} else {

			securityMonitor = new SecurityMonitor();

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (securityMonitor.IsRegistered()) {
			securityMonitor.start(); // Here we start the monitoring and control
										// thread

			while (!Done) {
				// Here, the main thread continues and provides the main menu

				System.out.println("\n\n\n\n");
				System.out.println("Security Command Console: \n");

				if (args.length != 0)
					System.out.println("Using message manger at: " + args[0]
							+ "\n");
				else
					System.out.println("Using local message manger \n");

				System.out
						.println("Set Door state: 0 - false (false = everything is ok) \n1 - true (true = door is broken) \n---------------------");
				System.out
						.println("Set Window state: 0 - false (false = everything is ok) \n1 - true (true = window is broken) \n---------------------");
				System.out
						.println("Set Motion Detection state: 0 - false (false = everything is ok) \n1 - true (true = motion is detected) \n---------------------");
				System.out
						.println("Arm/Disarm Alarmsystem: 0 - false (false = off) \n1 - true (true = on) \n---------------------");

				System.out.println("Select an Option: \n");
				System.out.println("1: Set Door state");
				System.out.println("2: Set Window state");
				System.out.println("3: Set Motion state\n");
				System.out.println("4: Arm/Disarm Alarmsystem\n");
				System.out.println("X: Stop System\n");
				System.out.print("\n>>>> ");
				Option = UserInput.KeyboardReadString();

				// ////////// option 1 ////////////

				if (Option.equals("1")) {
					// Here we get the door state

					Error = true;

					while (Error) {
						// Here we get the low temperature range

						while (Error) {
							System.out.print("\nEnter door state>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								int dummyDoorState = Integer.valueOf(Option)
										.intValue();

								if (dummyDoorState == 1) {
									doorBreakState = true;
									Error = false;
								} else if (dummyDoorState == 0) {
									doorBreakState = false;
									Error = false;
								} else {
									System.out
											.println("Not a valid value, please enter 0 or 1 see descritption on top.");
									System.out.println(doorBreakState);
									Error = true;
								}
								securityMonitor.setDoorState(doorBreakState);

							} else {
								System.out
										.println("Not a integer, please try again...");
								Error = true;
							} // if

						} // while

					} // while

				} // if

				
				// ////////// option 2 ////////////
				if (Option.equals("2")) {
					// Here we get the window state

					Error = true;

					while (Error) {
						// Here we get the low temperature range

						while (Error) {
							System.out.print("\nEnter door state>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								int dummyDoorState = Integer.valueOf(Option)
										.intValue();

								if (dummyDoorState == 1) {
									doorBreakState = true;
									Error = false;
									System.out.println(doorBreakState);
								} else if (dummyDoorState == 0) {
									doorBreakState = false;
									System.out.println(doorBreakState);
									Error = false;
								} else {
									System.out
											.println("Not a valid value, please enter 0 or 1 see descritption on top.");
									System.out.println(doorBreakState);
									Error = true;
								}

							} else {
								System.out
										.println("Not a integer, please try again...");
								Error = true;
							} // if

						} // while

					} // while

				} // if
				
				// ////////// option 3 ////////////
				if (Option.equals("3")) {
					// Here we get the temperature ranges

					Error = true;

					while (Error) {
						// Here we get the low temperature range

						while (Error) {
							System.out.print("\nEnter door state>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								int dummyDoorState = Integer.valueOf(Option)
										.intValue();

								if (dummyDoorState == 1) {
									doorBreakState = true;
									securityMonitor.setDoorState(doorBreakState);
									System.out.println(doorBreakState);
									Error = false;
									
								} else if (dummyDoorState == 0) {
									doorBreakState = false;
									securityMonitor.setDoorState(doorBreakState);
									System.out.println(doorBreakState);
									Error = false;
								} else {
									System.out
											.println("Not a valid value, please enter 0 or 1 see descritption on top.");
									System.out.println(doorBreakState);
									Error = true;
								}
								
								

							} else {
								System.out
										.println("Not a integer, please try again...");
								Error = true;
							} // if
							
							securityMonitor.setDoorState(doorBreakState);

						} // while

					} // while

				} // if
				
				// ////////// option 4 ////////////
				if (Option.equals("4")) {
					// Here we arm or disarm the alarmsystem

					Error = true;

					while (Error) {
						while (Error) {
							System.out.print("\nArm Alarmsystem>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								int dummyIsArmed = Integer.valueOf(Option)
										.intValue();

								if (dummyIsArmed == 1) {
									isArmed = true;
									Error = false;
								} else if (dummyIsArmed == 0) {
									isArmed = false;
									Error = false;
								} else {
									System.out
											.println("Not a valid value, please enter 0 or 1 see descritption on top.");
									System.out.println(isArmed);
									Error = true;
								}
								
								securityMonitor.setAlarmSystem(isArmed);

							} else {
								System.out
										.println("Not a integer, please try again...");
								Error = true;
							} // if

						} // while

					} // while

				} // if

				// ////////// option X ////////////

				if (Option.equalsIgnoreCase("X")) {
					// Here the user is done, so we set the Done flag and halt
					// the environmental control system. The monitor provides a
					// method
					// to do this. Its important to have processes release their
					// queues
					// with the message manager. If these queues are not
					// released these
					// become dead queues and they collect messages and will
					// eventually
					// cause problems for the message manager.

					securityMonitor.Halt();
					Done = true;
					System.out
							.println("\nConsole Stopped... Exit monitor mindow to return to command prompt.");
					securityMonitor.Halt();

				} // if

			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n");

		} // if

	} // main

} // ECSConsole
