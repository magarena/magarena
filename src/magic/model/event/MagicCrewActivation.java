package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;
import magic.model.action.AddStaticAction;

import java.util.Collections;

public class MagicCrewActivation extends MagicPermanentActivation {

    private static final MagicActivationHints ACTIVATION_HINTS =
        new MagicActivationHints(MagicTiming.Animate);

    private static MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
        }
    };

    private final int crew;

    public MagicCrewActivation(final int n) {
        super(
            ACTIVATION_HINTS,
            "Crew"
        );
        crew = n;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Collections.singletonList(new MagicCrewCostEvent(source, crew));
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "SN becomes an artifact creature until end of turn."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new AddStaticAction(event.getPermanent(), ST));
    }
}
