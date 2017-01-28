package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicSource;
import magic.model.MurmurHash3;
import magic.model.event.MagicSourceActivation;

public class MagicPlayChoiceResult implements MagicMappable<MagicPlayChoiceResult> {

    public static final MagicPlayChoiceResult PASS=new MagicPlayChoiceResult(null);
    public static final MagicPlayChoiceResult SKIP=new MagicPlayChoiceResult(null);

    public final MagicSourceActivation<? extends MagicSource> sourceActivation;

    MagicPlayChoiceResult(final MagicSourceActivation<? extends MagicSource> aSourceActivation) {
        sourceActivation = aSourceActivation;
    }

    @Override
    public MagicPlayChoiceResult map(final MagicGame game) {
        if (this==PASS) {
            return PASS;
        } else if (this==SKIP) {
            return SKIP;
        } else {
            return new MagicPlayChoiceResult(
                MagicSourceActivation.create(
                    game,
                    sourceActivation
                )
            );
        }
    }

    @Override
    public String toString() {
        if (this==PASS) {
            return "pass";
        } else if (this==SKIP) {
            return "skip";
        } else {
            return sourceActivation.source.getName();
        }
    }

    public String getText() {
        return sourceActivation.activation.getText();
    }

    @Override
    public long getId() {
        return MurmurHash3.hash(new long[] {
            sourceActivation.source.getStateId(),
            sourceActivation.activation.hashCode(),
        });
    }
}
