package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicCopyMap;

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

    @Override
    public MagicPlayerPayManaCostResult map(final MagicGame game) {
        return this;
    }

    @Override
    public long getId() {
        return hashCode();
    }
}
