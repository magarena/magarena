package magic.model;

import magic.model.target.MagicTarget;


public class MagicDamage {
	
	private final MagicSource source;
	private MagicTarget target;
	private int amount;
	private int dealtAmount=0;
	private final boolean combat;
	private boolean unpreventable=false;
	private boolean noRegeneration=false;

	public MagicDamage(final MagicSource source,final MagicTarget target,final int amount,final boolean combat) {
		this.source=source;
		this.target=target;
		this.amount=amount;
		this.combat=combat;
	}
	
	public MagicSource getSource() {
		return source;
	}
	
	public void setTarget(final MagicTarget target) {
		this.target=target;
	}
	
	public MagicTarget getTarget() {
		return target;
	}
	
	public void setAmount(final int amount) {
		this.amount=amount;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setDealtAmount(final int dealtAmount) {
		this.dealtAmount=dealtAmount;
	}
	
	public int getDealtAmount() {
		return dealtAmount;
	}
		
	public boolean isCombat() {
		return combat;
	}
		
	public void setUnpreventable() {
		unpreventable=true;
	}
	
	public boolean isUnpreventable() {
		return unpreventable;
	}
	
	public void setNoRegeneration() {
		noRegeneration=true;
	}
	
	public boolean hasNoRegeneration() {
		return noRegeneration;
	}
}
