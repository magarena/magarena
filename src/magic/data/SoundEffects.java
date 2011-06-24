package magic.data;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import magic.MagicMain;
import magic.model.MagicGame;

public class SoundEffects {

	public static final String WIN_SOUND="win.au";
	public static final String LOSE_SOUND="lose.au";
	public static final String TURN_SOUND="turn.au";
	public static final String RESOLVE_SOUND="resolve.au";
	public static final String COMBAT_SOUND="combat.au";
	
	private static final File SOUNDS_PATH=new File(MagicMain.getGamePath(),"sounds");
	private static final SoundEffects INSTANCE=new SoundEffects();
	
	private SoundEffects() {
	
	}
	
	public void playClip(final String name) {
		if (GeneralConfig.getInstance().isSound()) {
			try {
				final Clip clip=AudioSystem.getClip();
		        final AudioInputStream inputStream=AudioSystem.getAudioInputStream(new File(SOUNDS_PATH,name));
		        clip.open(inputStream);
		        inputStream.close();
		        clip.start();
		    } catch (final Exception ex) {
                System.err.println("ERROR! Unable to load clip " + name);
                System.err.println(ex.getMessage());
                ex.printStackTrace();
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
