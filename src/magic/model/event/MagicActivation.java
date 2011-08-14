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

	private int cardIndex;
	private final MagicCondition conditions[];
	private final MagicTargetChoice targetChoice;
    private final String text;
	protected final MagicActivationHints hints;
	protected final int priority;
	protected long id;
    private final int index;

	/** Conditions can be null. */
	public MagicActivation(
            final int index,
            final MagicCondition conditions[],
            final MagicActivationHints hints,
            final String txt
            ) {

        this.text = txt;
        this.index = index;
        this.cardIndex = -1;
        this.id = -1;
		this.conditions=conditions;
		this.targetChoice=getTargetChoice();
		this.hints=hints;
		this.priority=hints.getTiming().getPriority();
		
		// set the activation for the single activation condition.
		if (conditions!=null) {
			for (final MagicCondition condition : conditions) {
				if (condition instanceof MagicSingleActivationCondition) {
					final MagicSingleActivationCondition singleCondition=(MagicSingleActivationCondition)condition;
					singleCondition.setActivation(id);
				}
			}
		}
	}
		
	public final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
    
    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
        this.id = (cardIndex << 16) + index;
    }
		
	public final MagicCondition[] getConditions() {
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
	
	public void changeActivationPriority(final MagicGame game,final MagicSource source) {
		assert game != null;
        assert source != null;
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
		if (conditions != null) {
			for (final MagicCondition condition : conditions) {
				if (!condition.accept(game,source)) {
					return false;
				}
			}
		}
		if (targetChoice == null) {
			return true;
		}
		// Check for legal targets.
		final boolean useTargetHints = useHints || GeneralConfig.getInstance().getSmartTarget();
		return game.hasLegalTargets(player,source,targetChoice,useTargetHints);
	}
	
	@Override
	public int compareTo(final MagicActivation other) {
		return Long.signum(id-other.id);
	}
	
	public abstract boolean usesStack();
	
	public abstract MagicEvent[] getCostEvent(final MagicSource source);
	
	public abstract MagicEvent getEvent(final MagicSource source);
	
	public abstract MagicTargetChoice getTargetChoice();	
}
