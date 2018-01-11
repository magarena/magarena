[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures with no counters on them get -2/-2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getPlayers().each({
                final MagicPlayer player ->
                player.getPermanents().each({
                    final MagicPermanent permanent ->
                    if (permanent.isCreature() && !permanent.hasCounters()) {
                        game.doAction(new ChangeTurnPTAction(permanent, -2, -2));
                    }
                });
            });
        }
    }
]

