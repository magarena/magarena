package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class ProwessTrigger extends OtherSpellIsCastTrigger {

    private static final ProwessTrigger INSTANCE = new ProwessTrigger() ;

    public ProwessTrigger() {}

    public static final ProwessTrigger create() {
        return INSTANCE;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
        return spell.isFriend(permanent) && (!spell.hasType(MagicType.Creature));
    }

    @Override
    public MagicEvent executeTrigger(MagicGame game, MagicPermanent permanent, MagicCardOnStack data) {
        return new MagicEvent(
            permanent,
            this,
            "SN gets +1/+1 until end of turn."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeTurnPTAction(event.getPermanent(),1,1));
    }

}
