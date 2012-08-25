package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Tree_of_Redemption {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Main),
            "Life") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source,source.getController()},
                    this,
                    "Exchange your life total with "+ source + "'s toughness.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            final MagicPlayer player = (MagicPlayer)data[1];
            final int life = player.getLife();
            final int toughness = permanent.getToughness();
            // exchange life with toughness even when they are equal
            // because toughness can be modified in layer ModPT (7c)
            game.doAction(new MagicChangeLifeAction(player,toughness - life));
            game.doAction(new MagicAddStaticAction(permanent, new MagicStatic(
                    MagicLayer.SetPT) {
                @Override
                public void modPowerToughness(
                        final MagicPermanent source,
                        final MagicPermanent permanent,
                        final MagicPowerToughness pt) {
                    pt.setToughness(life);
                }   
            }));
        }
    };
}
