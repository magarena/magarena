package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayerState;
import magic.model.action.ChangePlayerStateAction;

public class AscendTrigger extends OtherEntersBattlefieldTrigger {
    
    public AscendTrigger() {
        super(MagicTrigger.REPLACEMENT);
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
        return permanent.isFriend(played) &&
            !permanent.getController().hasState(MagicPlayerState.CitysBlessing) &&
            permanent.getController().getNrOfPermanents() >= 10;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
        game.doAction(new ChangePlayerStateAction(permanent.getController(), MagicPlayerState.CitysBlessing));
        return MagicEvent.NONE;
    }

}
