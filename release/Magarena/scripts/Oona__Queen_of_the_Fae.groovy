def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicColor color = event.getChosenColor();
    final MagicTuple tup = event.getRefTuple();
    final MagicPlayer player = tup.getPlayer(1);
    final int X = tup.getInt(0);
    for (final MagicCard card : player.getLibrary().getCardsFromTop(X)) {
        game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
        if (card.hasColor(color)) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 black Faerie Rogue creature token with flying")
            ));
        }
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{X}{U/B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                payedCost.getX(),
                this,
                "Choose a color. Target opponent\$ exiles the top RN cards of his or her library. " +
                "For each card of the chosen color exiled this way, create a 1/1 blue and black Faerie Rogue creature token with flying."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    MagicColorChoice.ALL_INSTANCE,
                    new MagicTuple(event.getRefInt(), it),
                    action,
                    "Chosen color\$."
                ));
            });
        }
    }
]
