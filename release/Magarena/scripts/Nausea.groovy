[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -1/-1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            Collection<MagicPermanent> creatures = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilter.TARGET_CREATURE
            )
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
            }
        }
    }
]
