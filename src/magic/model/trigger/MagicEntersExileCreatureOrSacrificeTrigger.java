package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.MagicSubType;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicPermanentFilterImpl;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetType;

public class MagicEntersExileCreatureOrSacrificeTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicSubType[] subtypes;
    private final String targets;

    public MagicEntersExileCreatureOrSacrificeTrigger(final String targets) {
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
        final MagicTargetFilter<MagicPermanent> targetFilter = new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent creature) {
                boolean hasSubType = subtypes.length == 0;
                for (final MagicSubType subtype : subtypes) {
                    hasSubType |= creature.hasSubType(subtype);
                }
                return hasSubType &&
                       creature.isCreature() &&
                       creature.getController() == player &&
                       creature.getId() != permanent.getId();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == MagicTargetType.Permanent;
            }
        };

        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            targetFilter,
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
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,creature));
                }
            });
        } else {
            game.doAction(new MagicSacrificeAction(permanent));
        }
    }
}
