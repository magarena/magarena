package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicCopyTargetPicker;

public class Rite_of_Replication {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(
                            MagicTargetChoice.TARGET_CREATURE,
                            MagicManaCost.FIVE,
                            false),
                    MagicCopyTargetPicker.create(),
                    this,
                    "Put a token onto the battlefield that's a copy of target creature$. " + 
                    "If SN was kicked$, put five of those tokens onto the battlefield instead.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer player = event.getPlayer();
                    final MagicCardDefinition cardDefinition = creature.getCardDefinition();
                    int count = (Integer)choiceResults[1] > 0 ? 5:1;
                    for (;count>0;count--) {
                        game.doAction(new MagicPlayTokenAction(player,cardDefinition));
                    }
                }
            });
        }
    };
}
