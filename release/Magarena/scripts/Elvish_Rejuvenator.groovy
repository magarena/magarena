def action = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        game.doAction(new PlayCardAction(
            it,
            event.getPlayer(),
            MagicPlayMod.TAPPED
        ));
        final MagicCardList rest = new MagicCardList(event.getRefCardList().minus(it));
        rest.shuffle();
        rest.each({
            final MagicCard card ->
            game.doAction(new MoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));
        });
    });
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN looks at the top five cards of their library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topFive = player.getLibrary().getCardsFromTop(5);
            game.doAction(new LookAction(
                topFive,
                player,
                "top five cards of your library"
            ));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(
                    "Put a land card from among them onto the battlefield?",
                    new MagicFromCardListChoice(topFive, topFive.findAll({ it.hasType(MagicType.Land) }), 1, "Choose a land card")
                ),
                topFive,
                action,
                "PN may\$ put a land card from among them\$ onto the battlefield tapped. PN puts the rest on the bottom of their library in a random order."
            ));
        }
    }
]
