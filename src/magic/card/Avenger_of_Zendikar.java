package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicLandfallTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Collection;

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
                    "PN puts a 0/1 green Plant creature token onto " +
                    "the battlefield for each land he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
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
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                    1,
                    MagicSimpleMayChoice.DEFAULT_YES),
                this,
                "PN may$ put a +1/+1 counter on each " +
                "Plant creature he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                final Collection<MagicPermanent> targets = game.filterPermanents(
                        event.getPlayer(),
                        MagicTargetFilter.TARGET_PLANT_YOU_CONTROL);
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicChangeCountersAction(
                        target,
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            }
        }        
    };
}
