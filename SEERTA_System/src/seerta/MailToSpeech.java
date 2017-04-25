package seerta;
import javax.speech.*;
import javax.speech.synthesis.*;
import java.util.Locale;
import java.io.*;
import javax.sound.sampled.*;
import com.cloudgarden.audio.*;
import com.cloudgarden.speech.*;
/**
 *
 * @author Roshan Chaudhary
 */
public class MailToSpeech {

    static int x = 0;

	public MailToSpeech (int snn, int nm, int tm, String datee, String fromm, String subjectt, String messages) {

	System.out.println("Saving Email into Speech\n");
	Synthesizer synth = null;

	try {
	    synth = Central.createSynthesizer(null);
	    ((com.cloudgarden.speech.CGEngineProperties)
	    synth.getSynthesizerProperties()).setEventsInNewThread(false);

	    CGAudioManager audioMan = (CGAudioManager)synth.getAudioManager();

	    synth.allocate();
	    synth.waitEngineState(synth.ALLOCATED);

	    synth.resume();

	    SynthesizerProperties props = synth.getSynthesizerProperties();
	    props.setSpeakingRate(120);

	    AudioFormat fmt;
	    //could set the file's format to that of the synthesizer (better results) or a specific format
            fmt = new AudioFormat(8000,16,1,false,true);// best suited for MSTAPI

	    System.out.println("File Format = "+fmt);
	    System.out.println("Synth Format = "+audioMan.getAudioFormat());

	    AudioFileFormat ff;
	    ff = new AudioFileFormat(AudioFileFormat.Type.WAVE, fmt, AudioSystem.NOT_SPECIFIED);

	    try {
		AudioFileSink sink = new AudioFileSink(new File("Email\\email"+snn+".wav"),ff);

		//connect synthesizer to sink with an AudioFormatConverter (if formats different)
		new AudioFormatConverter(audioMan, sink, true);

		audioMan.startSending();

                synth.speak("You have"+nm+"New Emails and"+tm+"Total Emails",null);
                synth.speak("Following are your Email, with newest first",null);
                synth.speak("From"+fromm,null);
				synth.speak("Received Date"+datee,null);
                synth.speak("Subject"+subjectt,null);
                synth.speak("The Message is as follows",null);

               for(int i=0; i < messages.length(); i++) //FILTERING NON-PLAINTEXT MESSAGES
                {
                    if ((messages.charAt(i))== '<' || (messages.charAt(i))== '>')
                    {
                        x = 1;
                       break;
                    }
                    else
                    {
                        x = 0;
                    }

                }

                if (x == 1) {
                    System.out.println("Alert! Non-Plaintext Message");
                    synth.speak("Sorry! this email contains non plain text. So, we move to next email",null);
                }
                else
                {
                    System.out.println("PLAINTEXT Message");
                    synth.speak(messages,null);
                }

		synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
		System.out.println("\nqueue empty");
		//need to close the output file otherwise the results are uncertain
		audioMan.closeOutput();
		sink.drain();

		System.out.println("\nflushed");
	    } catch(Exception e) {
		//If the file cannot be opened (eg if it is already opened by
		//another application) we will get an IOException
		e.printStackTrace();
	    }
    }

	     catch(Exception e) {
		//If the file cannot be opened (eg if it is already opened by
		//another application) we will get an IOException
		e.printStackTrace();
	    }
}
}

