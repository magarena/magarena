package magic.card;

import java.util.Collection;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicReturnExiledUntilThisLeavesPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Day_of_the_Dragons {
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Exile all creatures you control. Then put that many 5/5 " +
                    "red Dragon creature tokens with flying onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicExileUntilThisLeavesPlayAction(
                        event.getPermanent(),
                        (MagicPermanent)target));
            }
            int amount = targets.size();
            for (;amount>0;amount--) {
                game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Dragon5")));
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
                                "Sacrifice all Dragons. Return exiled cards to the battlefield." :
                                "Sacrifice all Dragons. Return " + clist.get(0) + " to the battlefield.");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_DRAGON_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicSacrificeAction((MagicPermanent)target));
            }
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(
                    permanent,
                    MagicLocationType.Play,
                    event.getPlayer()));
        }
    };
}
