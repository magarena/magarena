package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.ExileLinkAction;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicPermanentFilterImpl;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;

public class ChampionTrigger extends EntersBattlefieldTrigger {

    private final MagicSubType[] subtypes;
    private final String targets;

    public ChampionTrigger(final String targets) {
        this.targets = targets;
        if ("creature".equalsIgnoreCase(targets)) {
            subtypes = new MagicSubType[0];
        } else {
            final String[] tokens = targets.split(" or ");
            subtypes = new MagicSubType[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                subtypes[i] = MagicSubType.getSubType(tokens[i]);
            }
        }
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        final MagicTargetFilter<MagicPermanent> targetFilter = subtypes.length == 0 ?
            MagicTargetFilterFactory.CREATURE_YOU_CONTROL :
            new MagicPermanentFilterImpl() {
                @Override
                public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent tribal) {
                    boolean hasSubType = false;
                    for (final MagicSubType subtype : subtypes) {
                        hasSubType |= tribal.hasSubType(subtype);
                    }
                    return hasSubType && tribal.isController(player);
                }
            };

        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            new MagicOtherPermanentTargetFilter(targetFilter, permanent),
            MagicTargetHint.None,
            "another " + targets + " to exile"
        );

        return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    targetChoice
                ),
                MagicExileTargetPicker.create(),
                this,
                "You may$ exile another " + targets + " you control$. " +
                "If you don't, sacrifice SN.");
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent permanent = event.getPermanent();
        if (event.isYes()) {
            event.processTargetPermanent(game, (final MagicPermanent creature) -> {
                final ExileLinkAction act = new ExileLinkAction(permanent,creature);
                game.doAction(act);
                game.executeTrigger(MagicTriggerType.WhenChampioned, act);
            });
        } else {
            game.doAction(new SacrificeAction(permanent));
        }
    }
}
