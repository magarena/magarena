def restoreCardAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList revealed = event.getRefCardList();
    cards.shuffle();
    revealed.each {
        game.doAction(new MoveCardAction(
            it,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.BottomOfOwnersLibrary
        ));
    }
}

def castAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList revealed = new MagicCardList(event.getRefCardList());
    if (event.isYes()) {
        final MagicCard card = revealed.removeCardAtTop();
        game.doAction(CastCardAction.WithoutManaCost(
            event.getPlayer(),
            card,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.Graveyard
        ));
    } else {
        game.doAction(new RemoveCardAction(revealed.getCardAtTop(), MagicLocationType.OwnersLibrary));
    }
    game.addEvent(new MagicEvent(
        event.getSource(),
        event.getPlayer(),
        revealed,
        restoreCardAction,
        "PN puts all revealed cards not cast this way on the bottom of PN's library in a random order."
    ));
}

[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
            return new MagicEvent(
                source,
                this,
                "PN reveals cards from the top of PN's library until PN reveals a nonland card with converted mana cost 3 or less."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList library = player.getLibrary();
            def predicate = { final MagicCard card -> !card.hasType(MagicType.Land) && card.getConvertedCost() <= 3 }
            final MagicCardList revealed = new MagicCardList();
            MagicCard target = MagicCard.NONE;
            while (target == MagicCard.NONE && library.size() > 0) {
                final MagicCard topCard = library.getCardAtTop()
                game.doAction(new RevealAction(topCard));
                revealed.add(topCard);
                if (predicate(topCard)) {
                    target = topCard;
                } else {
                    game.doAction(new RemoveCardAction(topCard, MagicLocationType.OwnersLibrary));
                }
            }

            if (target != MagicCard.NONE) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Cast the card?"),
                    revealed,
                    castAction,
                    "PN may\$ cast that card without paying its mana cost."
                ));
            }
            else {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    revealed,
                    restoreCardAction,
                    "PN puts all revealed cards not cast this way on the bottom of PN's library in a random order."
                ));
            }
        }
    }
]

