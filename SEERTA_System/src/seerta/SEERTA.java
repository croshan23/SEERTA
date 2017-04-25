package seerta;
import javax.telephony.*;
import javax.telephony.events.*;
import javax.telephony.media.*;
import javax.telephony.media.events.*;
/**
 *
 * @author Roshan Chaudhary
 */
public class SEERTA {

    public static void main(String[] args) {

    /*
     * Create a provider by first obtaining the default implementation of
     * JTAPI and then the default provider of that implementation.
     */
    System.out.println("System Started\n");
    Provider myprovider = null;

    try {
      JtapiPeer peer = JtapiPeerFactory.getJtapiPeer("net.xtapi.XJtapiPeer");
      myprovider = peer.getProvider("MSTAPI");
    } catch (Exception excp) {
      System.out.println("Can't get Provider: " + excp.toString());
      System.exit(0);
    }

    /*
     * Locate the Terminal associated with our near end and place a call
     * observer on it. This will instruct the implementation to add a call
     * observer once a telephone call comes to the Terminal. We are assuming
     * Terminals are named after a primary telephone number on that Terminal.
     */

    Terminal terminal;

    try {
      terminal = myprovider.getTerminal("0");
      terminal.addCallObserver(new IncomingCallObserver());
    } catch (Exception excp) {
      excp.printStackTrace();
    }
    }

}
