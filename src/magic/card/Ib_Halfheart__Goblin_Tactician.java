package magic.card;


import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Ib_Halfheart__Goblin_Tactician {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Goblin)) ?
                new MagicEvent(
                    permanent,
                    new Object[]{otherPermanent},
                    this,
                    "PN sacrifices " + otherPermanent + ". If PN does, " + otherPermanent + " deals 4 damage to each creature blocking it."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
            final MagicPermanent goblin = (MagicPermanent)data[0];
            final MagicSacrificeAction sac = new MagicSacrificeAction(goblin);
            final MagicPermanentList blockingCreatures = goblin.getBlockingCreatures();
            game.doAction(sac);
            if (sac.isValid()) {
                for (final MagicPermanent blocker : blockingCreatures) {
                    final MagicDamage damage = new MagicDamage(goblin, blocker, 4, false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    };
    
    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
        new MagicCondition[] {MagicCondition.TWO_MOUNTAINS_CONDITION},
        new MagicActivationHints(MagicTiming.Token, false), "Token") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[] {
                new MagicSacrificePermanentEvent(source, source.getController(), MagicTargetChoice.SACRIFICE_MOUNTAIN),
                new MagicSacrificePermanentEvent(source, source.getController(), MagicTargetChoice.SACRIFICE_MOUNTAIN)
            };
        }
    
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts two 1/1 red Goblin creature tokens into play."
            );
        }
    
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player, TokenCardDefinitions.get("Goblin1")));
            game.doAction(new MagicPlayTokenAction(player, TokenCardDefinitions.get("Goblin1")));
        }
    };
}
