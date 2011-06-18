package magic.model.event;

import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;

public abstract class MagicActivation implements MagicEventAction, Comparable<MagicActivation> {

	private final MagicCardDefinition cardDefinition;
	private final MagicCondition conditions[];
	private final MagicTargetChoice targetChoice;
	protected final MagicActivationHints hints;
	protected final int priority;
	protected final int id;

	/** Conditions can be null. */
	public MagicActivation(
            final MagicCardDefinition cardDefinition,
            final int index,
            final MagicCondition conditions[],
            final MagicActivationHints hints) {

		this.cardDefinition=cardDefinition;
		this.id=(cardDefinition.getIndex()<<16)+index;
		this.conditions=conditions;
		this.targetChoice=getTargetChoice();
		this.hints=hints;
		this.priority=hints.getTiming().getPriority();
		
		// Sets the activation for the condition.
		if (conditions!=null) {
			for (final MagicCondition condition : conditions) {
				if (condition instanceof MagicSingleActivationCondition) {
					final MagicSingleActivationCondition singleCondition=(MagicSingleActivationCondition)condition;
					singleCondition.setActivation(this);
				}
			}
		}
	}
	
	public MagicActivation(
            final String name,
            final int index,
            final MagicCondition conditions[],
            final MagicActivationHints hints) {
		this(CardDefinitions.getInstance().getCard(name),index,conditions,hints);		
	}
		
	public final MagicCardDefinition getCardDefinition() {
		return cardDefinition;
	}
		
	public final MagicCondition[] getConditions() {
		return conditions;
	}

	public final MagicActivationHints getActivationHints() {
		return hints;
	}
	
	public final int getId() {
		return id;
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
	
	public void changeActivationPriority(final MagicGame game,final MagicSource source) {
		final MagicActivationPriority actpri = source.getController().getActivationPriority();
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
		if (conditions!=null) {
			for (final MagicCondition condition : conditions) {
				if (!condition.accept(game,source)) {
					return false;
				}
			}
		}
		if (targetChoice==null) {
			return true;
		}
		// Check for legal targets.
		final boolean useTargetHints=useHints||GeneralConfig.getInstance().getSmartTarget();
		return game.hasLegalTargets(player,source,targetChoice,useTargetHints);
	}
	
	@Override
	public int compareTo(final MagicActivation other) {
		return id-other.id;
	}
	
	public abstract boolean usesStack();
	
	public abstract MagicEvent[] getCostEvent(final MagicSource source);
	
	public abstract MagicEvent getEvent(final MagicSource source);
	
	public abstract MagicTargetChoice getTargetChoice();	
}
