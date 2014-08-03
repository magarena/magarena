[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on each creature each opponent controls."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(creature.getController(),MagicTargetFilterFactory.CREATURE_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeCountersAction(target,MagicCounterType.PlusOne,1));
            }
        }
    }
]
