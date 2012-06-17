package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;

public class MagicPlayChoiceResult implements MagicMappable {

    public static final MagicPlayChoiceResult PASS=new MagicPlayChoiceResult();
    public static final MagicPlayChoiceResult SKIP=new MagicPlayChoiceResult();

    public MagicSource source;
    public MagicActivation activation;
    
    MagicPlayChoiceResult(final MagicSource source,final MagicActivation activation) {
        this.source=source;
        this.activation=activation;
    }

    private MagicPlayChoiceResult() {}
    
    @Override
    public MagicPlayChoiceResult map(final MagicGame game) {
        if (this==PASS) {
            return PASS;
        } else if (this==SKIP) {
            return SKIP;
        } else {
            return new MagicPlayChoiceResult((MagicSource)source.map(game),activation);
        }
    }
    
    @Override
    public String toString() {
        if (this==PASS) {
            return "pass";
        } else if (this==SKIP) {
            return "skip";
        } else {
            return source.getName();
        }
    }

    public String getText() {
        return activation.getText();
    }

    @Override
    public long getId() {
        return source.getId() * 31 + activation.hashCode();
    }
}
