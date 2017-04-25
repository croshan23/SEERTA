package seerta;
import java.io.File;
import javax.telephony.*;
import javax.telephony.events.*;
import javax.telephony.media.*;
import javax.telephony.media.events.*;
import java.net.*;
/**
 *
 * @author Roshan Chaudhary
 */
class IncomingCallObserver implements MediaCallObserver {

  public void callChangedEvent(CallEv evlist[]) {

    for (int i = 0; i < evlist.length; i++) {

        CallEv ev = evlist[i];
        TerminalConnection tc = ((TermConnEv)ev).getTerminalConnection();
        MediaTerminalConnection mtc = (MediaTerminalConnection)tc;

      if (ev instanceof TermConnCreatedEv) {
        try {
          System.out.println("Call Detected\n"); 
		  mtc.usePlayURL(new URL("file:./Audio/Greeting.wav"));  
          System.out.println("Loading Greeting Message\n"); 
        } catch (Exception excp) {
          excp.printStackTrace();
        }
      }

      else if (ev instanceof TermConnRingingEv) {
        try {
          tc.answer();
          System.out.println("Call Answered\n");
        } catch (Exception excp) {
          excp.printStackTrace();
        }
      }

      else if (ev instanceof MediaTermConnAvailableEv) {
        /* We want to start playing of the prompt. */
        try {
          mtc.setDtmfDetection(true);
          System.out.println("DTMF Detection True\n");
          mtc.startPlaying();
          System.out.println("Playing Welcome Message\n");
        } catch (Exception excp) {
          excp.printStackTrace();
        }
      }

      else if (ev instanceof MediaTermConnDtmfEv) {
            /* Print out the DTMF digits */
            char digit = ((MediaTermConnDtmfEv)ev).getDtmfDigit();
            System.out.println("Detected DTMF: " + digit);
			
			//initialising available password
            int test='5';
            
            if (digit == test){
                ConnectDB cdb = new ConnectDB (digit);
                
                for (int j = 1; j <= new File("Email\\").list().length; j++){
                        try{
                        MediaTerminalConnection mtcc = (MediaTerminalConnection)tc;
                        mtcc.usePlayURL(new URL("file:./Email/email"+j+".wav"));
                        mtcc.startPlaying();
                        System.out.println("Playing Email Message\n");
                        }
                        catch (Exception e){

                        }
                }

            }
            
            else{
                try{
                MediaTerminalConnection mtcn = (MediaTerminalConnection)tc;
                mtcn.usePlayURL(new URL("file:./Audio/badpass.wav"));
                mtcn.startPlaying();
                System.out.println("Wrong Password\n");
                }
                catch (Exception e){

                }
            }
      }
    

      else if (ev instanceof MediaTermConnUnavailableEv) {
	  /* When the channel on the telephone line becomes unavailable, then
         * stop all activity.
      */
        try {
          mtc.stopPlaying();
          System.out.println("Playing Stoped\n");
          mtc.setDtmfDetection(false);
          System.out.println("DTMF Detection False\n");
          //SEERTA s = new SEERTA();
          tc.getConnection().disconnect();
          System.out.println("Call Terminated\n");               
        } catch (Exception excp) {
          // Handle exceptions
        }
      }

    }//for
  }//callchangedevent
}//class

