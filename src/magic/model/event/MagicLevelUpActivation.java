package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;

public class MagicLevelUpActivation extends MagicPermanentActivation {

    private static final MagicActivationHints ACTIVATION_HINTS = 
        new MagicActivationHints(MagicTiming.Main);
        
    private final MagicManaCost cost;
    
    public MagicLevelUpActivation(final MagicManaCost cost,final int maximum) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                new MagicArtificialCondition(
                    MagicCondition.NONE,
                    new MaximumCondition(maximum)),
                    cost.getCondition()
            },
            ACTIVATION_HINTS,
            "Level");
        this.cost=cost;
    }
    
    @Override
    public MagicEvent[] getCostEvent(final MagicSource source) {
        return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),cost)};
    }
    
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
                source,
                source.getController(),
                new Object[]{source},
                this,
                "Put a level counter on "+source+'.');
    }

    @Override
    public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
        game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
    }
    
    private static final class MaximumCondition implements MagicCondition {
        
        private final int maximum;
        
        public MaximumCondition(final int maximum) {
            this.maximum=maximum;
        }
        
        @Override
        public boolean accept(final MagicSource source) {
            return ((MagicPermanent)source).getCounters(MagicCounterType.Charge)<maximum;
        }        
    };
}
