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
import magic.model.trigger.MagicLandfallTrigger;

public class Calcite_Snapper {
    public static final MagicLandfallTrigger T = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer player = permanent.getController();
            return new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may switch " + permanent + "'s " +
                            "power and toughness until end of turn."),
                    MagicEvent.NO_DATA,
                    this,
                    player + " may$ switch " + permanent + "'s " +
                    "power and toughness until end of turn.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicAddStaticAction(event.getPermanent(), new MagicStatic(
                        MagicLayer.SwitchPT,
                        MagicStatic.UntilEOT) {
                    @Override
                    public void modPowerToughness(
                            final MagicPermanent source,
                            final MagicPermanent permanent,
                            final MagicPowerToughness pt) {
                        pt.set(pt.toughness(),pt.power());
                    }   
                }));
            }
        }        
    };
}
