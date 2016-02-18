[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{U}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card. Each opponent exiles the top card of his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player));
            for (final MagicCard card : player.getOpponent().getLibrary().getCardsFromTop(1)) {
                game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                game.logAppendMessage(player, MagicMessage.format("%s is exiled.", card));
            }
        }
    }
]
