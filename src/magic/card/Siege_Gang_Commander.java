package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Siege_Gang_Commander {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{1}{R}"),MagicCondition.CONTROL_GOBLIN_CONDITION},
            new MagicActivationHints(MagicTiming.Removal,true),
            "Damage") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPlayer player=source.getController();
            return new MagicEvent[]{                    
                new MagicPayManaCostEvent(source,player,MagicManaCost.create("{1}{R}")),
                new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_GOBLIN)
            };
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature or player$."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T =new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts three 1/1 red Goblin creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            for (int count=3;count>0;count--) {
                game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Goblin1")));
            }
        }        
    };
}
