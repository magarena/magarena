package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicPlayerPayManaCostResult implements MagicPayManaCostResult {

    private final int x;
    private final int converted;

    MagicPlayerPayManaCostResult(final int x,final int converted) {
        this.x=x;
        this.converted=converted;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getConverted() {
        return converted;
    }

    @Override
    public void doAction(final MagicGame game,final MagicPlayer player) {
        //do nothing
    }

    @Override
    public String toString() {
        return x>0?"X="+x:"";
    }
}
