package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;


public class Argent_Sphinx {
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{
                MagicConditionFactory.ManaCost("{U}"),
                MagicCondition.METALCRAFT_CONDITION
            },
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{                    
                new MagicPayManaCostEvent(source,"{U}"),
                new MagicPlayAbilityEvent(source)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile SN. Return it to the battlefield " +
                "under your control at the end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
        }
    };
}
