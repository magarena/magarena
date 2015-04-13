package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicProwessTrigger extends MagicWhenOtherSpellIsCastTrigger {

    private static final MagicProwessTrigger INSTANCE = new MagicProwessTrigger() ;

    public MagicProwessTrigger() {}

    public static final MagicProwessTrigger create() {
        return INSTANCE;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
        return spell.isFriend(permanent) && (spell.hasType(MagicType.Creature) == false);
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
