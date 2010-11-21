package magic.model.target;

import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public interface MagicTarget extends MagicCopyable,MagicMappable {
	
	public String getName();
	
	public boolean isPlayer();
	
	public boolean isSpell();
	
	public boolean isPermanent();
		
	public MagicPlayer getController();
			
	public int getPreventDamage();
	
	public void setPreventDamage(int amount);
	
	public boolean isValidTarget(final MagicGame game,final MagicSource source);
}