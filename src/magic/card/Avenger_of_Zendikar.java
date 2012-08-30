package magic.card;

import java.util.Collection;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicLandfallTrigger;

public class Avenger_of_Zendikar {
    public static final MagicWhenComesIntoPlayTrigger T1 =new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    player + " puts a 0/1 green Plant creature token onto " +
                    "the battlefield for each land he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            int amount = player.getNrOfPermanentsWithType(MagicType.Land);
            for (;amount>0;amount--) {
                game.doAction(new MagicPlayTokenAction(
                        player,
                        TokenCardDefinitions.get("Plant")));
            }
        }        
    };
    
    public static final MagicLandfallTrigger T2 = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer player = permanent.getController();
            return new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a +1/+1 counter on each " +
                                "Plant creature he or she controls.",
                                MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        MagicEvent.NO_DATA,
                        this,
                        player + " may$ put a +1/+1 counter on each " +
                        "Plant creature he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final Collection<MagicTarget> targets = game.filterTargets(
                        event.getPlayer(),
                        MagicTargetFilter.TARGET_PLANT_YOU_CONTROL);
                for (final MagicTarget target : targets) {
                    game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)target,
                            MagicCounterType.PlusOne,
                            1,
                            true));
                }
            }
        }        
    };
}
