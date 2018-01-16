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
            for (final MagicPlayer player : game.getPlayers()) {
                for (final MagicPermanent perm : player.getPermanents()) {
                    if (perm.isCreature() && !perm.hasCounters()) {
                        game.doAction(new ChangeTurnPTAction(perm, -2, -2));
                    }
                }
            }
        }
    }
]

