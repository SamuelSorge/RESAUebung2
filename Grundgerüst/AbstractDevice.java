/******************************************************************************************************************
* File:AbstractDevice.java
* Course: 17655
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
* Methods:
*
*  public void InitDevice();
*  public void FunctionBeforeRead();
*  public void HandleMessage(Message msg);
*  public void FunctionAfterRead();
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

abstract class AbstractDevice
{
    abstract public void InitDevice();
    abstract public void FunctionBeforeRead();
    abstract public void HandleMessage(Message msg);
    abstract public void FunctionAfterRead();

    protected MessageQueue eq = null;				// Message Queue
	protected int MsgId = 0;						// User specified message ID
	protected MessageManagerInterface em = null;	// Interface object to the message manager
	protected boolean Done = false;				// Loop termination flag
    protected MessageWindow mw = null;

    public int mainLoop(String args[])
    {
		String MsgMgrIP;					// Message Manager IP address
		Message Msg = null;					// Message object
		int	Delay = 2500;					// The loop delay (2.5 seconds)

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// message manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is on the local machine

				em = new MessageManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} else {

			// message manager is not on the local system

			MsgMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + MsgMgrIP );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is NOT on the local machine

				em = new MessageManagerInterface( MsgMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (em != null)
		{
            // ********************************************************************************************************
            InitDevice();
            // ********************************************************************************************************

            if(mw == null)
            {
                System.out.println("Error in InitDevice, now MessageWindow created! Quit program.");
                return -1;
            }

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");


			while ( !Done )
			{
                // ********************************************************************************************************
                FunctionBeforeRead();
                // ********************************************************************************************************

				// Get the message queue
				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = -4, this means the the humidify or
				// dehumidifier has been turned on/off. Note that we get all the messages
				// from the queue at once... there is a 2.5 second delay between samples,..
				// so the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the humidity as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();
                    // ********************************************************************************************************
                    HandleMessage(Msg);
                    // ********************************************************************************************************

					// If the message ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if
				} // for

                // ********************************************************************************************************
                FunctionAfterRead();
                // ********************************************************************************************************

				// Here we wait for a 2.5 seconds before we start the next sample

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if
        return 0;
	} // main

} // AbstractDevice
