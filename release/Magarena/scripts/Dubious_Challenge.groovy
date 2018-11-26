def putOntoBattlefieldAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer controller = event.getSource().getController();
    final MagicPlayer opponent = event.getPlayer();
    final MagicCardList rest = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        rest.remove(it);
        game.doAction(new PutOntoBattlefieldAction(MagicLocationType.Exile, it, opponent));
    });
    rest.each {
        game.doAction(new PutOntoBattlefieldAction(MagicLocationType.Exile, it, controller));
    }
}

def exileAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer controller = event.getPlayer();
    final MagicCardList exiled = new MagicCardList();
    event.processChosenCards(game, {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
        if (it.isInExile()) {
            exiled.add(it);
        }
    });
    game.doAction(new ShuffleLibraryAction(controller));
    game.addEvent(new MagicEvent(
        event.getSource(),
        event.getRefPlayer(),
        new MagicFromCardListChoice(exiled, 1, true),
        exiled,
        putOntoBattlefieldAction,
        "Target player (PN) may choose one of the exiled cards\$ and put it onto the battlefield under his or her control. " +
        "${controller} puts the rest onto the battlefield under ${controller}'s control."
    ));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "PN looks at the top ten cards of PN's library,"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(10);
            game.doAction(new LookAction(topCards, player, "top ten cards of your library"));
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(topCards.findAll({ it.hasType(MagicType.Creature) }), 2, true),
                    it,
                    exileAction,
                    "exile up to two creature cards from among them\$, then shuffle PN's library."
                ));
            });
        }
    }
]

