package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class MagicEntersDamageTargetTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int n;
    private final MagicTargetChoice targetChoice;

    public MagicEntersDamageTargetTrigger(final MagicTargetChoice targetChoice, final int n) {
        this.n = n;
        this.targetChoice = targetChoice;
    }

    private final String genDescription(final MagicTargetChoice targetChoice) {
        String description = "creature or player$.";
        if (targetChoice == MagicTargetChoice.TARGET_CREATURE) {
            description = "creature$.";
        }
        else if (targetChoice == MagicTargetChoice.NEG_TARGET_PLAYER) {
            description = "player$.";
        }
        return description;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            targetChoice,
            new MagicDamageTargetPicker(n),
            this,
            "SN deals " + n + " damage to target " + genDescription(targetChoice)
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTarget(game,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                game.doAction(new MagicDealDamageAction(event.getSource(), target, n));
            }
        });
    }
}
