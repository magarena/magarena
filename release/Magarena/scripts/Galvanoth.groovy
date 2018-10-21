def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
    }
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("look at the top card of your library?"),
                this,
                "PN may\$ looks at the top card of PN's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player = event.getPlayer();
                final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
                for (final MagicCard card : cards) {
                    game.doAction(new LookAction(card, player, "top card of your library"));
                    if (card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)) {
                        game.addEvent(new MagicEvent(
                            event.getSource(),
                            new MagicMayChoice("Cast the card without paying its mana cost?"),
                            card,
                            action,
                            "If it's an instant or sorcery card, PN may\$ cast it without paying its mana cost."
                        ));
                    }
                }
            }
        }
    }
]
