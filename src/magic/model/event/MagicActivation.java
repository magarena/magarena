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
	
    private final MagicCondition conditions[];
    private final String text;
	private final MagicActivationHints hints;
	private final int priority;
    private final int index;
	
    private int cardIndex;
	private long id;
	private MagicTargetChoice targetChoice;

	/** Conditions can be null. */
	public MagicActivation(
            final int index,
            final MagicCondition conditions[],
            final MagicActivationHints hints,
            final String txt
            ) {
        
        this.cardIndex = -1;
        this.id = -1;             //depends on card
		this.targetChoice = null; //depends on card

        //check arguments
        if (conditions == null) {
            throw new RuntimeException("conditions is null");
        }
        if (hints == null) {
            throw new RuntimeException("hints is null");
        }
        if (txt == null) {
            throw new RuntimeException("hints is null");
        }

        this.text = txt;
        this.index = index;
		this.conditions=conditions;
		this.hints=hints;
		this.priority=hints.getTiming().getPriority();
	}
    
    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
        this.id = (cardIndex << 16) + index;
        this.targetChoice = getTargetChoice();
		
        // set the activation for the single activation condition, depends on id
        for (final MagicCondition condition : conditions) {
            if (condition instanceof MagicSingleActivationCondition) {
                final MagicSingleActivationCondition singleCondition = (MagicSingleActivationCondition)condition;
                singleCondition.setActivation(id);
            }
        }
    }
	
    public final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
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
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(game,source)) {
                return false;
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
