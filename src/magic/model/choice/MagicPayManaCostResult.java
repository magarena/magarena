package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicPayManaCostResult {
		
	public int getX();
	
	public int getConverted();
	
	public void doAction(final MagicGame game,final MagicPlayer player);
}