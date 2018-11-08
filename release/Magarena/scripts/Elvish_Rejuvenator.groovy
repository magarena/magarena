def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList topCards = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        game.doAction(new ReturnCardAction(
            MagicLocationType.OwnersLibrary,
            it,
            event.getPlayer(),
            MagicPlayMod.TAPPED
        ));
        topCards.remove(it);
        topCards.shuffle();
        topCards.each({
            final MagicCard card ->
            game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));
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
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(5);
            game.doAction(new LookAction(
                topCards,
                player,
                "top five cards of your library"
            ));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(
                    topCards.findAll({ it.hasType(MagicType.Land) }),
                    topCards,
                    1,
                    true
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                topCards,
                action,
                "PN may put a land card from among them\$ onto the battlefield tapped. PN puts the rest on the bottom of their library in a random order."
            ));
        }
    }
]
