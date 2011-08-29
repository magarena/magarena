package magic.data;

import magic.MagicMain;
import magic.model.MagicGame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffects {

	public static final String WIN_SOUND="win.au";
	public static final String LOSE_SOUND="lose.au";
	public static final String TURN_SOUND="turn.au";
	public static final String RESOLVE_SOUND="resolve.au";
	public static final String COMBAT_SOUND="combat.au";
	
	private static final File SOUNDS_PATH=new File(MagicMain.getGamePath(),"sounds");
	private static final SoundEffects INSTANCE=new SoundEffects();
	
	private SoundEffects() {}
	
	public void playClip(final String name) {
		if (GeneralConfig.getInstance().isSound()) {
            Clip clip = null;
            AudioInputStream ins = null;
			try { //load sound clip
                clip = AudioSystem.getClip();
                ins = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH,name));
                clip.open(ins);
		        clip.start();
		    } catch (final LineUnavailableException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
		    } catch (final UnsupportedAudioFileException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
		    } catch (final IOException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
            } finally {
                magic.data.FileIO.close(ins);
            }
		}
	}

	public void playClip(final MagicGame game,final String name) {
		if (game.isSound()) {
			playClip(name);
		}
	}

	public static SoundEffects getInstance() {
		return INSTANCE;
	}
} 
