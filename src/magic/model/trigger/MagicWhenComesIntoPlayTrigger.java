package magic.model.trigger;

import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetPicker;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicRuleEventAction;
import magic.model.action.MagicPlayTokenAction;
import magic.data.TokenCardDefinitions;

public abstract class MagicWhenComesIntoPlayTrigger extends MagicTrigger<MagicPayedCost> {
    public MagicWhenComesIntoPlayTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenComesIntoPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenComesIntoPlay;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }

    public static MagicWhenComesIntoPlayTrigger create(final String rule) {
        final String effect = rule.toLowerCase();
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.action;
        final MagicTargetPicker picker = ruleAction.picker;
        final MagicTargetChoice choice = ruleAction.getChoice(effect);

        return new MagicWhenComesIntoPlayTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
                return new MagicEvent(
                    permanent,
                    choice,
                    picker,
                    this,
                    rule + "$"
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                action.executeEvent(game, event);
            }
        };
    }

    public static final MagicWhenComesIntoPlayTrigger PutGolemOntoTheBattlefield = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 3/3 colorless Golem artifact creature token onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Golem3")));
        }
    };

    public static final MagicWhenComesIntoPlayTrigger ChooseOpponent = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            permanent.setChosenTarget(permanent.getOpponent());
            return MagicEvent.NONE;
        }
    };
}
