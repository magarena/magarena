package magic.model.event;

import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;

public abstract class MagicActivation implements MagicEventAction, Comparable<MagicActivation> {

    public static final MagicCondition[] NO_COND = new MagicCondition[0];
    
    private final int priority;
    private final int index;
    private final String text;
    private final MagicCondition conditions[];
    private final MagicActivationHints hints;
    
    private MagicCardDefinition cdef;
    private long id;

    MagicActivation(
        final int index,
        final MagicCondition conditions[],
        final MagicActivationHints hints,
        final String txt) {
        
        this.text = txt;
        this.index = index;
        this.conditions=conditions;
        this.hints=hints;
        this.priority=hints.getTiming().getPriority();
        
        // set the activation for the single activation condition
        for (final MagicCondition condition : conditions) {
            if (condition instanceof MagicSingleActivationCondition) {
                final MagicSingleActivationCondition singleCondition = (MagicSingleActivationCondition)condition;
                singleCondition.setActivation(this);
            }
        }
        
        //depends on the card
        this.cdef = null;
        this.id = -1;
    }
    
    public void setCardDefinition(final MagicCardDefinition cdef) {
        this.cdef = cdef;
        this.id = (cdef.hashCode() << 16) + index;
    }
    
    final MagicCardDefinition getCardDefinition() {
        return cdef;
    }
        
    private final MagicCondition[] getConditions() {
        return conditions;
    }

    public final MagicActivationHints getActivationHints() {
        return hints;
    }
    
    public final long getId() {
        return id;
    }

    public final String getText() {
        return text;
    }
    
    private final boolean checkActivationPriority(final MagicSource source) {
        final MagicActivationPriority actpri = source.getController().getActivationPriority();
        final int priorityDif = priority - actpri.getPriority();
        if (priorityDif > 0) {
            return true;
        } else if (priorityDif < 0) {
            return false;
        } 
        return id >= actpri.getActivationId();        
    }
    
    void changeActivationPriority(final MagicGame game,final MagicPlayer player) {
        final MagicActivationPriority actpri = player.getActivationPriority();
        actpri.setPriority(priority);
        actpri.setActivationId(id);
    }
    
    public final boolean canPlay(
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source,
            final boolean useHints) {
        
        if (useHints && 
            (!checkActivationPriority(source) || 
             !hints.getTiming().canPlay(game,source) || 
             hints.isMaximum(source)
            )
           ) {
            return false;
        }

        for (final MagicCondition condition : conditions) {
            if (!condition.accept(source)) {
                return false;
            }
        }
        
        if ((source instanceof MagicPermanent &&
            ((MagicPermanent)source).hasState(MagicPermanentState.LosesAllAbilities))
            ||
            (source.isSpell() &&
            player.hasState(MagicPlayerState.CantCastSpells))) {
            return false;
        }
        
        // Check for legal targets.
        final boolean useTargetHints = useHints || GeneralConfig.getInstance().getSmartTarget();
        final MagicTargetChoice targetChoice = getTargetChoice(source); 
        return game.hasLegalTargets(player,source,targetChoice,useTargetHints);
    }
    
    @Override
    public int compareTo(final MagicActivation other) {
        return Long.signum(id-other.id);
    }
    
    abstract boolean usesStack();
    
    protected abstract MagicEvent[] getCostEvent(final MagicSource source);
    
    abstract MagicEvent getEvent(final MagicSource source);
    
    abstract MagicTargetChoice getTargetChoice(final MagicSource source);    
}
