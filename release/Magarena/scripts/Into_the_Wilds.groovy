def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final MagicCard card = event.getRefCard();
        final MagicPlayer player = event.getPlayer();
        game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary, card, event.getPlayer()));
        game.logAppendMessage(player, "${player} puts (${card}) it onto the battlefield.")
    }
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN looks at the top card of his or her library. If it's a land card, "+
                "PN may put it onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new LookAction(card, player, "top card of your library"));
                if (card.hasType(MagicType.Land)) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicSimpleMayChoice(
                            MagicSimpleMayChoice.DRAW_CARDS,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES
                        ),
                        card,
                        action,
                        "\$"
                    ));
                }
            }
        }
    }
]
