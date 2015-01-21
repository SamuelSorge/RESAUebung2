import InstrumentationPackage.Indicator;
import InstrumentationPackage.MessageWindow;
import MessagePackage.Message;


public class SprinklerController extends AbstractDevice{
	boolean SprinklerState = false; // Fire state: false == off, true == on
	Indicator sprinklerI = null;
	Thread thread = null;
	Boolean Handled = false;

	public static void main(String[] args) {

        deviceDescription = "This class simulates the SprinklerController.";
		SprinklerController controller = new SprinklerController();
		controller.mainLoop(args);
	}

	@Override
	public void InitDevice() {


			/*
			 * Now we create the temperature control status and message panel*
			 * We put this panel about 1/3 the way down the terminal, aligned to
			 * the left* of the terminal. The status indicators are placed
			 * directly under this panel
			 */

			float WinPosX = 0.0f; // This is the X position of the message
									// window in terms
			// of a percentage of the screen height
			float WinPosY = 0.3f; // This is the Y position of the message
									// window in terms
			// of a percentage of the screen height

			mw = new MessageWindow(
					"Sprinkler Controller Status Console", WinPosX, WinPosY);

			// Put the status indicators under the panel...

			sprinklerI = new Indicator("Sprinkler OFF", mw.GetX(), mw.GetY()
					+ mw.Height());

			mw.WriteMessage("Registered with the message manager.");

			try {
				mw.WriteMessage("   Participant id: " + em.GetMyId());
				mw.WriteMessage("   Registration Time: "
						+ em.GetRegistrationTime());

			} // try

			catch (Exception e) {
				System.out.println("Error:: " + e);

			} // catch

	}

	@Override
	public void FunctionBeforeRead() {
		// TODO Auto-generated method stub

	}

	@Override
	public void HandleMessage(Message msg) {
		// Force Sprinkler on // off  of the user input.
		if(msg.GetMessageId() == 88){
			if(thread != null){
				thread.interrupt();
			}
			Handled = true;
			boolean des = Boolean.valueOf(msg.GetMessage()).booleanValue();
			Message message;
			if(des){
				SprinklerState = true;
				mw.WriteMessage("User forced sprinkler on!");
				sprinklerI.SetLampColorAndMessage("SPRINKLER ON", 1);
				message = new Message(1337, Boolean.toString(true));
				mw.WriteMessage("Sprinkler turned on!");
			}else{
				SprinklerState = false;
				mw.WriteMessage("User forced sprinkler off!");
				sprinklerI.SetLampColorAndMessage("SPRINKLER OFF", 0);
				message = new Message(1337, Boolean.toString(false));
				mw.WriteMessage("Sprinkler turned off!");
			}
			try {
				em.SendMessage(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// Fire detected
		if(msg.GetMessageId() == -88){
			boolean fire = Boolean.valueOf(msg.GetMessage()).booleanValue();
			if(fire){
				mw.WriteMessage("Recieve Fire Signal!");
				if(thread == null && !SprinklerState && !Handled){
					thread = new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(10000);
								Message message = new Message(1337, Boolean.toString(true));
								SprinklerState = true;
								mw.WriteMessage("Turn sprinkler ON!");
								sprinklerI.SetLampColorAndMessage("Sprinkler ON", 1);
								em.SendMessage(message);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally{
								thread = null;
							}

						}

					});
					thread.start();
					mw.WriteMessage("Waiting 10s for user input.");
				}else{
					mw.WriteMessage("Still waiting!");
				}
			}else{
				mw.WriteMessage("Recieve No Fire Signal!");
				Handled = false;
			}
		}
	}

	@Override
	public void FunctionAfterRead() {
		// TODO Auto-generated method stub
		// Update the lamp status
	}

}
