package magic.model.event;

import java.util.Set;
import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.AddStaticAction;
import magic.model.action.ChangeCountersAction;
import magic.model.action.GainAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class MagicAwakenEvent extends MagicEvent {
    final private static MagicTargetChoice tchoice = new MagicTargetChoice("target land you control");
    final private static MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness bPt) {
            bPt.set(0, 0);
        }
    };
    final private static MagicStatic Type = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Elemental);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags | MagicType.Creature.getMask();
        }
    };

    public MagicAwakenEvent(final MagicSource source, final MagicPlayer player, final int n) {
        super(
            source,
            player,
            tchoice,
            n,
            EVENT_ACTION,
            "PN puts RN +1/+1 counters on target land$ you control and " +
            "it becomes a 0/0 Elemental creature with haste. It's still a land."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent it) -> {
            game.doAction(new ChangeCountersAction(
                it,
                MagicCounterType.PlusOne,
                event.getRefInt()
            ));
            game.doAction(new AddStaticAction(it, PT));
            game.doAction(new AddStaticAction(it, Type));
            game.doAction(new GainAbilityAction(it, MagicAbility.Haste, MagicStatic.Forever));
        });
    };
}
