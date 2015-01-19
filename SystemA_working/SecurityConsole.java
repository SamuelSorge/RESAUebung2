/******************************************************************************************************************
 * File:SecurityConsole.java
 * Course: 17655
 * Project: Assignment 3
 * Copyright: Copyright (c) 2009 Carnegie Mellon University
 * Versions:
 *	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
 *
 * Description: This class is the console for the museum security control system. This process consists of two
 * threads. The ECSMonitor object is a thread that is started that is responsible for the monitoring and control of
 * the museum environmental systems. The main thread provides a text interface for the user to change the temperature
 * and humidity ranges, as well as shut down the system.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
import TermioPackage.*;
import MessagePackage.*;

public class SecurityConsole {
	public static void main(String args[]) {
		Termio UserInput = new Termio(); // Termio IO Object
		boolean Done = false; // Main loop flag
		String Option = null; // Menu choice from user
		Message Msg = null; // Message object
		boolean Error = false; // Error flag
		SecurityMonitor Monitor = null; // The security control system monitor
		int isArmed = 0; // 0: alarm system disarmed; 1: alarm system armed
		int simDoorDestruction = 0; // 0: door is ok; 1: door is not ok

		// ///////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		// ///////////////////////////////////////////////////////////////////////////////

		if (args.length != 0) {
			// message manager is not on the local system

			Monitor = new SecurityMonitor(args[0]);

		} else {

			Monitor = new SecurityMonitor();

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (Monitor.IsRegistered()) {
			Monitor.start(); // Here we start the monitoring and control thread

			while (!Done) {
				// Here, the main thread continues and provides the main menu

				System.out.println("\n\n\n\n");
				System.out
						.println("Security Control System Command Console: \n");

				if (args.length != 0)
					System.out.println("Using message manager at: " + args[0]
							+ "\n");
				else
					System.out.println("Using local message manager \n");

				System.out
						.println("Set alarm system state: " + isArmed + "%\n");
				System.out.println("Set door destruction state: "
						+ simDoorDestruction + "%\n");
				System.out.println("Select an Option: \n");
				System.out.println("1: Set door destruction state");
				System.out.println("4: Set alarm system state");
				System.out.println("X: Stop System\n");
				System.out.print("\n>>>> ");
				Option = UserInput.KeyboardReadString();

				// ////////// option 1 ////////////

				if (Option.equals("1")) {
					// Here we get the door destruction state

					Error = true;

					while (Error) {
						// Here we get the door destruction state

						while (Error) {
							System.out
									.print("\nEnter the door destruction state>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								simDoorDestruction = Integer.valueOf(Option)
										.intValue();

							} else {

								System.out
										.println("Not a number, please try again...");

							} // if

						} // while

						if (simDoorDestruction > 1 || simDoorDestruction < 0) {
							System.out
									.println("\nThe door destruction state must be 1 (true) or false (0)...");
							System.out.println("Please try again...\n");
							Error = true;

						} else {

							Monitor.SetSimDoorDestructionState(simDoorDestruction);

						} // if

					} // while

				} // if

				// ////////// option 4 ////////////

				if (Option.equals("4")) {
					// Here we get the alarm system state

					Error = true;

					while (Error) {
						// Here we get the alarm system state

						while (Error) {
							System.out
									.print("\nEnter the alarm system state>>> ");
							Option = UserInput.KeyboardReadString();

							if (UserInput.IsNumber(Option)) {
								Error = false;
								isArmed = Integer.valueOf(Option).intValue();

							} else {

								System.out
										.println("Not a number, please try again...");

							} // if

						} // while

						if (isArmed > 1 || isArmed < 0) {
							System.out
									.println("\nThe alarm system state must be be 1 (true) or false (0)...");
							System.out.println("Please try again...\n");
							Error = true;

						} else {

							Monitor.SetAlarmSystemState(isArmed);

						} // if

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

					Monitor.Halt();
					Done = true;
					System.out
							.println("\nConsole Stopped... Exit monitor mindow to return to command prompt.");
					Monitor.Halt();

				} // if

			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n");

		} // if

	} // main

} // ECSConsole
