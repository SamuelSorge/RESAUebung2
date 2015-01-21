import InstrumentationPackage.Indicator;
import InstrumentationPackage.MessageWindow;
import MessagePackage.Message;
import MessagePackage.MessageManagerInterface;
import MessagePackage.MessageQueue;

public class FireController extends AbstractDevice {
	boolean FireState = false; // Fire state: false == off, true == on
	Indicator fireI = null;

	
	public static void main(String[] args) {
		
		FireController controller = new FireController();
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
					"Fire Controller Status Console", WinPosX, WinPosY);

			// Put the status indicators under the panel...

			fireI = new Indicator("Fire OFF", mw.GetX(), mw.GetY()
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
		// TODO Auto-generated method stub
		if(msg.GetMessageId() == 1){
			float temp = Float.valueOf(msg.GetMessage()).floatValue();
			if(temp < 200){
				FireState = false;
			}else{
				FireState = true;
			}
			
			
		}
	}

	@Override
	public void FunctionAfterRead() {
		// TODO Auto-generated method stub
		// Update the lamp status

		if(FireState){
			
			mw.WriteMessage("Burn Burn Burn!");
			fireI.SetLampColorAndMessage("Fire ON", 1);
			Message message = new Message(-88, Boolean.toString(true));
			try {
				em.SendMessage(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			mw.WriteMessage("No Fire detected!");
			fireI.SetLampColorAndMessage("Fire OFF", 0);
			Message message = new Message(-88, Boolean.toString(false));
			try {
				em.SendMessage(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // if
	}

}
