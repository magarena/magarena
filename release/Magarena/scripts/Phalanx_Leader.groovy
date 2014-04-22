[
    new MagicHeroicTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeCountersAction(
                    target,
                    MagicCounterType.PlusOne,
                    1,
                    true
                ));
            }
        }
    }
]