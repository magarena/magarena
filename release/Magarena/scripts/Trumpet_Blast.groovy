[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Attacking creatures get +2/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilter.TARGET_ATTACKING_CREATURE
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,2,0));
            }
        }
    }
]
