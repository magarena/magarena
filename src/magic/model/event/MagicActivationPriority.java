package magic.model.event;

public class MagicActivationPriority {

	private int priority;
	private int activationId;
	
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

    public int getPriority() {
        return priority;
    }

    public int getActivationId() {
        return activationId;
    }

    public void incActivationId() {
        activationId++;
    }

    public void setPriority(final int pri) {
        priority = pri;
    }

    public void setActivationId(final int id) {
        activationId = id;
    }

	@Override
	public String toString() {
		return priority+" "+activationId;
	}
}
