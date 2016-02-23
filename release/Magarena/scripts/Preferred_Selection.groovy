def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new ShiftCardAction(
            card,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.OwnersHand
        ));
    });
}

def PutCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new ShiftCardAction(
            card,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.BottomOfOwnersLibrary
        ));
    });
}

def ACTION = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final MagicCardList topCards = player.getLibrary().getCardsFromTop(2);
    final MagicEvent sacEvent = new MagicSacrificeEvent(event.getPermanent());
    final MagicEvent payMana = new MagicPayManaCostEvent(event.getSource(), "{2}{G}{G}");
    if (event.isYes() && sacEvent.isSatisfied() && payMana.isSatisfied()) {
        game.logAppendMessage(player,"(Yes)");
        game.addEvent(sacEvent);
        game.addEvent(payMana);
        game.addEvent(new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(topCards, 1, "Choose a card to put into your hand"),
            MagicGraveyardTargetPicker.ReturnToHand,
            TakeCard,
            "PN puts a card in his or her hand."
        ));
    } else {
        game.logAppendMessage(player,"(No)");
        game.addEvent(new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(topCards, 1, "Choose a card to put on the bottom of your library"),
            MagicGraveyardTargetPicker.ExileOwn,
            PutCard,
            "PN puts a card on the bottom of his or her library."
        ));
    }
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN looks at the top two cards of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> topCards = player.getLibrary().getCardsFromTop(2);
            game.doAction(new LookAction(topCards, player, "top two cards of your library"));
            game.addEvent(new MagicEvent(
                event.getPermanent(),
                new MagicMayChoice("Sacrifice "+event.getPermanent().getName()+" and Pay {2}{G}{G}?"),
                ACTION,
                "PN may sacrifice SN and pay {2}{G}{G}. "+
                "If PN does, PN puts one of those cards into his or her hand. If PN doesn't, PN puts one of "+
                "those cards on the bottom of his or her library."
            ));
        }
    }
]
