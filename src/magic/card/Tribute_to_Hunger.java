package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicSacrificeTargetPicker;

public class Tribute_to_Hunger {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_OPPONENT,
                    new Object[]{cardOnStack},
                    this,
                    "Target opponent$ sacrifices a creature. " + player + 
                    " gains life equal to that creature's toughness.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                        game.addEvent(new MagicEvent(
                            cardOnStack.getCard(),
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE,
                            MagicSacrificeTargetPicker.create(),
                            new Object[]{event.getPlayer()},
                            EVENT_ACTION,
                            "Choose a creature to sacrifice$."));
                    }
                }
            });
        }
    };
    
    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicSacrificeAction(permanent));
                    final int toughness = permanent.getToughness();
                    game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],toughness));
                }
            });
        }
    };
}
