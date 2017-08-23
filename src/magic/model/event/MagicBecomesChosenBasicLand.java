package magic.model.event;

import java.util.Set;
import java.util.Collections;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicColor;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicAbility;
import magic.model.choice.MagicColorChoice;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.action.AddStaticAction;

public class MagicBecomesChosenBasicLand extends MagicEvent {

    private static MagicStatic SUBTYPE(final MagicColor color) {
        return new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
            @Override
            public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
                flags.clear();
                flags.add(color.getLandSubType());
            }
        };
    }

    private static MagicStatic MANA(final MagicColor color) {
        return new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                permanent.loseAllAbilities();
                permanent.addAbility(new MagicTapManaActivation(Collections.singletonList(color.getManaType())));
            }
        };
    }

    private static final MagicEventAction action = (final MagicGame game, final MagicEvent event) -> {
        for (final MagicPermanent it : event.getRefPermanentList()) {
            game.doAction(new AddStaticAction(it, SUBTYPE(event.getChosenColor())));
            game.doAction(new AddStaticAction(it, MANA(event.getChosenColor())));
        }
    };

    public MagicBecomesChosenBasicLand(final MagicSource source, final MagicPlayer player, final MagicPermanentList permanentList) {
        super(
            source,
            player,
            MagicColorChoice.ALL_INSTANCE,
            permanentList,
            action,
            "Chosen type$."
        );
    }
}
