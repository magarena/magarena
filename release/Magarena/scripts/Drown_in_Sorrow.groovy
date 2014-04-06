[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -2/-2 until end of turn. Scry 1"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,-2,-2));
            };
            game.addEvent(new MagicScryEvent(event));
        }
    }
]
