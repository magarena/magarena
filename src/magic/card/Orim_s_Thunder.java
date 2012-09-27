package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;

public class Orim_s_Thunder {
                            
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(
                        MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                        MagicManaCost.RED,
                        false),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target artifact or enchantment$." + 
                    "If SN was kicked$, " + 
                    "it deals damage equal to that permanent's converted mana cost to target creature.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent target) {
                    game.doAction(new MagicDestroyAction(target));
                }
            });
            final boolean kicked = (Integer)choiceResults[1] > 0;
            if (!kicked) {
                return;
            }
            final int amount = ((MagicPermanent)choiceResults[0]).getCardDefinition().getConvertedCost();
            game.addEvent(new MagicEvent(
                event.getSource(),
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(amount),
                new MagicEventAction() {
                    @Override
                    public void executeEvent(
                        final MagicGame game,
                        final MagicEvent event,
                        final Object[] data,
                        final Object[] choiceResults) {
                        event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                            public void doAction(final MagicTarget target) {
                                final MagicDamage damage = new MagicDamage(event.getSource(),target,amount,false);
                                game.doAction(new MagicDealDamageAction(damage));
                            }
                        });
                    }
                },
                "SN deals " + amount + " damage to target creature$."
            ));
        }
    };
}
