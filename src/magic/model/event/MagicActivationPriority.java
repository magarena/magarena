package magic.model.event;

public class MagicActivationPriority {

	public int priority;
	public int activationId;
	
	public MagicActivationPriority() {
		clear();
	}

	public MagicActivationPriority(final MagicActivationPriority abilityPriority) {
		priority=abilityPriority.priority;
		activationId=abilityPriority.activationId;
	}
	
	public void clear() {
		priority=-1;
		activationId=-1;
	}

	@Override
	public String toString() {
		return priority+" "+activationId;
	}
}
