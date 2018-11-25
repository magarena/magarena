def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    for (final MagicCard card : player.getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary,card,event.getPlayer(),MagicPlayMod.TAPPED));
                } else {
                    game.doAction(new DrawAction(event.getPlayer()));
        }
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Scry"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{4}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Scry 1, then reveal the top card of PN's library. If it's a land card, PN puts it onto the battlefield tapped. " +
                "Otherwise, PN draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryEvent(event));
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                action,
                ""
            ));
        }
    }
]
