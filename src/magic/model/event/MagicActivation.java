package magic.model.event;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;

public abstract class MagicActivation<T extends MagicSource> implements MagicEventAction, Comparable<MagicActivation> {

    public static final MagicCondition[] NO_COND = new MagicCondition[0];
    
    private final int priority;
    private final long id;
    private final String text;
    private final MagicCondition[] conditions;
    private final MagicActivationHints hints;

    MagicActivation(
        final MagicCondition[] conditions,
        final MagicActivationHints hints,
        final String txt) {
        
        this.text = txt;
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
        
        //randomly assigned, used for ordering activations
        this.id = hashCode();
    }
    
    private final MagicCondition[] getConditions() {
        return conditions;
    }

    public final MagicActivationHints getActivationHints() {
        return hints;
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
            final T source,
            final boolean useHints) {
       
        if (useHints && !checkActivationPriority(source)) {
            return false;
        }

        if (useHints && !hints.getTiming().canPlay(game, source)) {
            return false;
        }

        if (useHints && hints.isMaximum(source)) {
            return false;
        }

        if (source.isPermanent() && ((MagicPermanent)source).hasState(MagicPermanentState.LosesActivatedAbilities)) {
            return false;
        }
        
        if (source.isSpell() && player.hasState(MagicPlayerState.CantCastSpells)) {
            return false;
        }

        // Check conditions for activation
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(source)) {
                return false;
            }
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

    abstract MagicEvent[] getCostEvent(final T source);
    
    abstract MagicEvent getEvent(final MagicSource source);
    
    abstract MagicTargetChoice getTargetChoice(final T source);    
}
