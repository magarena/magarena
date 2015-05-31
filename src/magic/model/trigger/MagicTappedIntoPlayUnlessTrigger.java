package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.TapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayUnlessTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicSubType subType1;
    private final MagicSubType subType2;

    public MagicTappedIntoPlayUnlessTrigger(final MagicSubType subType1,final MagicSubType subType2) {
        super(MagicTrigger.REPLACEMENT);
        this.subType1=subType1;
        this.subType2=subType2;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        final MagicPlayer player = permanent.getController();
        return (!player.controlsPermanent(subType1) &&
                !player.controlsPermanent(subType2)) ?
            new MagicEvent(
                permanent,
                this,
                "SN enters the battlefield tapped."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(TapAction.Enters(event.getPermanent()));
    }
}
