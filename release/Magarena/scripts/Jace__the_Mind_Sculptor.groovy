[
    new MagicPlaneswalkerActivation(2, "Look") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Look at the top card of target player\$'s library. You may put that card on the bottom of that player's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
            for (final MagicCard card : it.getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicLookAction(card, "top card of target player's library"));
                game.addEvent(MagicRuleEventAction.create("Scry 1.").getEvent(event.getSource()));
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
                "Draw three cards, then put two cards from your hand on top of your library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),3));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
        }
    },
    new MagicPlaneswalkerActivation(-1, "Bounce") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                this,
                "Return target creature\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            });
        }
    },
    new MagicPlaneswalkerActivation(-12, "Exile") {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Exile all cards from target player\$'s library, " +
                "then that player shuffles his or her hand into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
            for (final MagicCard card : new MagicCardList(it.getLibrary())) {
                game.doAction(new MagicRemoveCardAction(card, MagicLocationType.OwnersLibrary));
                game.doAction(new MagicMoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                }
            for (final MagicCard hand : new MagicCardList(it.getHand())) {
                game.doAction(new MagicRemoveCardAction(hand, MagicLocationType.OwnersHand));
                game.doAction(new MagicMoveCardAction(hand, MagicLocationType.OwnersHand, MagicLocationType.OwnersLibrary));
                }
            });
        }
    }
]
