def FATESEAL = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer PN = event.getPlayer();
    if (event.isYes()) {
        game.logAppendMessage(PN, "${PN} moved the card to the bottom.");
        game.doAction(new ScryAction(event.getRefPlayer()));
    } else {
        game.logAppendMessage(PN, "${PN} put the card back on top.");
    }
}

[
    new MagicPlaneswalkerActivation(2, "Look") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Look at the top card of target player\$'s library. PN may put that card on the bottom of that player's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : it.getLibrary().getCardsFromTop(1)) {
                    game.doAction(new LookAction(card, event.getPlayer(), "top card of ${it}'s library"));
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        event.getPlayer(),
                        new MagicMayChoice("Put this card on the bottom of ${it}'s library?"),
                        it,
                        FATESEAL,
                        ""
                    ));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(0, "Draw") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws three cards, then puts two cards from his or her hand on top of his or her library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),3));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
        }
    },
    new MagicPlaneswalkerActivation(-12, "Exile") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Exile all cards from target player\$'s library, " +
                "then that player shuffles his or her hand into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : new MagicCardList(it.getLibrary())) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Exile
                    ));
                }
                game.doAction(new ShuffleCardsIntoLibraryAction(
                    new MagicCardList(it.getHand()),
                    MagicLocationType.OwnersHand,
                ));
            });
        }
    }
]
