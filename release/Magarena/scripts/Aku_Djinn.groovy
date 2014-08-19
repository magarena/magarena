[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each creature each opponent controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilterFactory.CREATURE_YOUR_OPPONENT_CONTROLS
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeCountersAction(target,MagicCounterType.PlusOne,1));
            }
        }
    }
]
