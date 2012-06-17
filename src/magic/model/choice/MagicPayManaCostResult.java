package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicPayManaCostResult {
        
    int getX();
    
    int getConverted();
    
    void doAction(final MagicGame game,final MagicPlayer player);
}