package magic.model.target;

import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public class MagicTargetNone implements MagicTarget {

	private static final MagicTarget INSTANCE=new MagicTargetNone();
	
	private MagicTargetNone() {}
	
	@Override
	public MagicTargetNone map(final MagicGame game) {
		return this;
	}
	
	@Override
	public MagicCopyable create() {
		return this;
	}
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {
		
	}
	
	@Override
	public void setPreventDamage(final int amount) {
		
	}
	
	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {
		return false;
	}
	
	@Override
	public boolean isSpell() {
		return false;
	}
	
	@Override
	public boolean isPlayer() {
		return false;
	}
	
	@Override
	public boolean isPermanent() {
		return false;
	}
	
	@Override
	public int getPreventDamage() {
		return 0;
	}
	
	@Override
	public String getName() {
		return "no legal targets";
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public MagicPlayer getController() {
        throw new RuntimeException("getting controller of MagicTargetNone");
	}
	
	public static final MagicTarget getInstance() {
		return INSTANCE;
	}

    public long getId() {
        return hashCode();
    }
}
