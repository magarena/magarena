package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBecomeTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Aven_Mimeomancer {

    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            player + " may put a feather counter on target creature.",
                            MagicTargetChoice.TARGET_CREATURE),
                        new MagicBecomeTargetPicker(3,1,true),
                        MagicEvent.NO_DATA,
                        this,
                        player + " may$ put a feather counter on target creature$."):
                MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Feather,1,true));
                        game.doAction(new MagicAddStaticAction(creature, new MagicStatic(
                            MagicLayer.SetPT,
                            MagicTargetFilter.TARGET_CREATURE) {
                            @Override
                            public void modPowerToughness(
                                final MagicGame game,
                                final MagicPermanent permanent,
                                final MagicPowerToughness pt) {
                                pt.set(3,1);
                            }
                            @Override
                            public boolean accept(
                                final MagicGame game,
                                final MagicPermanent source,
                                final MagicPermanent target) {
                                return target.getId() == creature.getId() && target.getCounters(MagicCounterType.Feather) > 0;
                            }
                        }));
                        game.doAction(new MagicAddStaticAction(creature, new MagicStatic(
                            MagicLayer.Ability,
                            MagicTargetFilter.TARGET_CREATURE) {
                            @Override
                            public long getAbilityFlags(
                                final MagicGame game,
                                final MagicPermanent permanent,
                                final long flags) {
                                return flags | MagicAbility.Flying.getMask();
                            }
                            @Override
                            public boolean accept(
                                final MagicGame game,
                                final MagicPermanent source,
                                final MagicPermanent target) {
                                return target.getId() == creature.getId() && target.getCounters(MagicCounterType.Feather) > 0;
                            }
                        }));
                    }
                });
            }
		}
    };
}
