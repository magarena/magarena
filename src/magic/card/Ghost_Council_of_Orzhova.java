package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Ghost_Council_of_Orzhova {
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{
                MagicConditionFactory.ManaCost("{1}"),
                MagicCondition.ONE_CREATURE_CONDITION
            },
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPlayer player=source.getController();
            return new MagicEvent[]{                    
                new MagicPayManaCostEvent(source,player,MagicManaCost.create("{1}")),
                new MagicPlayAbilityEvent(source),
                new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_CREATURE)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Exile SN. Return it to the battlefield under its owner's control at end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Target opponent$ loses 1 life and PN gains 1 life.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player,-1));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
                }
            });
        }        
    };
}
