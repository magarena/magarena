[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN places two level tokens on each creature with level up he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                if (target.hasAbility(MagicAbility.LevelUp)) {
                    game.doAction(new MagicChangeCountersAction(
                        target,
                        MagicCounterType.Level,
                        2,
                        true
                    ));
                }
            }
        }
    }
]
