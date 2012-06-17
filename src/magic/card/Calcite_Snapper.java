package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicAddStaticAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Calcite_Snapper {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            return (player == played.getController() && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may switch " + permanent + "'s " +
                            "power and toughness until end of turn."),
                    new Object[]{permanent},
                    this,
                    player + " may$ switch " + permanent + "'s " +
                    "power and toughness until end of turn."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicPermanent permanent = (MagicPermanent)data[0];
                game.doAction(new MagicAddStaticAction(permanent, new MagicStatic(
                        MagicLayer.SwitchPT,
                        MagicStatic.UntilEOT) {
                    @Override
                    public void modPowerToughness(
                            final MagicGame game,
                            final MagicPermanent permanent,
                            final MagicPowerToughness pt) {
                        pt.set(pt.toughness(),pt.power());
                    }   
                }));
            }
        }        
    };
}
