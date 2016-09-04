package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.exception.GameException;

/** Lower priority values trigger before higher priority values. */
public abstract class MagicTrigger<T> implements MagicEventAction,MagicChangeCardDefinition {

    public  static final int REPLACEMENT       = 1;
    public  static final int REDIRECT_DAMAGE   = 10;
    public  static final int CANT_BE_PREVENTED = 20;
    public  static final int INCREASE_DAMAGE   = 30;
    public  static final int PREVENT_DAMAGE    = 40;
    public  static final int REPLACE_DAMAGE    = 60;
    public  static final int BEFORE_DEFAULT    = 90;
    private static final int DEFAULT_PRIORITY  = 100;

    private final int priority;

    protected MagicTrigger(final int aPriority) {
        priority = aPriority;
    }

    protected MagicTrigger() {
        this(DEFAULT_PRIORITY);
    }

    public final int getPriority() {
        return priority;
    }

    public boolean usesStack() {
        return getType() != MagicTriggerType.IfDamageWouldBeDealt && priority > REPLACEMENT;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new GameException(getClass() + " did not override executeEvent", game);
    }

    public abstract MagicTriggerType getType();

    public abstract MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final T data);

    public boolean accept(final MagicPermanent permanent, final T data) {
        return true;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
