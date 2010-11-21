package magic.model.phase;

import magic.model.MagicGame;

public class MagicBeginOfCombatPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicBeginOfCombatPhase();
	
	private MagicBeginOfCombatPhase() {
		
		super(MagicPhaseType.BeginOfCombat);	
	}
	
	public static MagicPhase getInstance() {
		
		return INSTANCE;
	}
	
	@Override
	public void executeBeginStep(final MagicGame game) {

		game.setStep(MagicStep.ActivePlayer);
	}
}