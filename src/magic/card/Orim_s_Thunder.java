package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;

public class Orim_s_Thunder {
                            
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(
                            MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                            MagicManaCost.RED,false),
                    new MagicDestroyTargetPicker(false),
                    new Object[]{cardOnStack},
                    this,
                    "Destroy target artifact or enchantment$." + 
                    "If " + card + " was kicked$, " + 
                    "it deals damage equal to that permanent's converted mana cost to target creature.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final int kickerCount = (Integer)choiceResults[1];
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];        
            game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent target) {
                    game.doAction(new MagicDestroyAction(target));
                    if (kickerCount > 0) {
                        final MagicSource source = cardOnStack.getSource();
                        final int amount = target.getCardDefinition().getConvertedCost();
                        game.addEvent(new MagicEvent(
                            source,
                            cardOnStack.getController(),
                            MagicTargetChoice.NEG_TARGET_CREATURE,
                            new MagicDamageTargetPicker(amount),
                            new Object[]{cardOnStack,amount},
                            new MagicEventAction() {
                                @Override
                                public void executeEvent(
                                    final MagicGame game,
                                    final MagicEvent event,
                                    final Object[] data,
                                    final Object[] choiceResults) {
                                    event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                                        public void doAction(final MagicTarget target) {
                                            final MagicDamage damage = new MagicDamage((MagicCard)data[0],target,(Integer)data[1],false);
                                            game.doAction(new MagicDealDamageAction(damage));
                                        }
                                    });
                                }
                            },
                            source + " deals " + amount + " damage to target creature$."
                        ));
                    }
                }
            });
        }
    };
}
