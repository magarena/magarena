[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                game.getDefendingPlayer(),
                this,
                "PN exiles the top 20 cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(20)) {
                game.doAction(new ShiftCardAction(card,
                MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
            }
        }
    }
]