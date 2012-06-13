package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

// Set an ability until end of turn.
public class MagicSetAbilityAction extends MagicAction {

	private final MagicPermanent permanent;
	private final long abilities;
    private final boolean duration;
	
	public MagicSetAbilityAction(final MagicPermanent permanent,final long abilities, final boolean duration) {
		this.permanent=permanent;
		this.abilities=abilities;
        this.duration=duration;
	}
	
    public MagicSetAbilityAction(final MagicPermanent permanent,final long abilities) {
        this(permanent,abilities,MagicStatic.UntilEOT);
	}
    
    public MagicSetAbilityAction(final MagicPermanent permanent,final MagicAbility ability,final boolean duration) {
		this(permanent,ability.getMask(),duration);
	}
	
	public MagicSetAbilityAction(final MagicPermanent permanent,final MagicAbility ability) {
		this(permanent,ability.getMask(),MagicStatic.UntilEOT);
	}
		
	@Override
	public void doAction(final MagicGame game) {
        game.doAction(new MagicAddStaticAction(permanent, new MagicStatic(
                MagicLayer.Ability,
                duration) {
            @Override
            public long getAbilityFlags(
                    final MagicGame game,
                    final MagicPermanent permanent,
                    final long flags) {
                return flags | abilities;
            }   
        }));
	}

	@Override
	public void undoAction(final MagicGame game) {
	}
	
    @Override
	public String toString() {
        return super.toString() + " (" + permanent + "," + abilities + ')';
	}
}
