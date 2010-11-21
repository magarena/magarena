package magic.model.target;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicDefaultTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicDefaultTargetPicker();
	
	private MagicDefaultTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		return 0;
	}

	@Override
	public Collection<Object> pickTargets(final MagicGame game,final MagicPlayer player,final Collection<Object> options) {

		return options;
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}