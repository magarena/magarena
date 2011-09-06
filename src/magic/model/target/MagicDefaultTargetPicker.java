package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.Collection;

public class MagicDefaultTargetPicker extends MagicTargetPicker<MagicTarget> {

	private static final MagicTargetPicker INSTANCE=new MagicDefaultTargetPicker();
	
	private MagicDefaultTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
		return 0;
	}

	@Override
	public Collection<MagicTarget> pickTargets(
            final MagicGame game,
            final MagicPlayer player,
            final Collection<MagicTarget> options) {
		return options;
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
