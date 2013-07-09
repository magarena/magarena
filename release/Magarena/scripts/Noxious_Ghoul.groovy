[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent source, final MagicPermanent other) {
            return other.isCreature() && other.hasSubType(MagicSubType.Zombie);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent source, final MagicPermanent other) {
            return new MagicEvent(
                source,
                this,
                "All non-Zombie creatures get -1/-1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilter.TARGET_NONZOMBIE_CREATURE
            );
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
            }
        }
    }
]
