package magic.card;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReturnExiledUntilThisLeavesPlayAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

import java.util.Collection;

public class Realm_Razer {
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Exile all lands.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_LAND);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicExileUntilThisLeavesPlayAction(
                        event.getPermanent(),
                        target));
            }
        }
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent left) {
            if (permanent == left &&
                !permanent.getExiledCards().isEmpty()) {
                final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
                return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    clist.size() > 1 ?
                        "Return exiled cards to the battlefield tapped." :
                        "Return " + clist.get(0) + " to the battlefield tapped."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(
                    event.getPermanent(),
                    MagicLocationType.Play,
                    MagicPlayCardAction.TAPPED));
        }
    };
}
