def ACTION = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final MagicCard card = event.getRefCard();
        game.doAction(new RevealAction(card));
        if (card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)) {
            game.doAction(new MagicTransformAction(event.getPermanent()));
        }
    }
}

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN looks at the top card of his or her library. PN may reveal that card. If an instant or sorcery card is revealed this way, transform SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new LookAction(card, event.getPlayer(), "top card of your library"));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Reveal the top card of your library?"),
                    card,
                    ACTION,
                    ""
                ));
            }
        }
    }
]
