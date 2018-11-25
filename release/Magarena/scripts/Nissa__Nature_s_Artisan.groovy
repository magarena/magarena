[
    new MagicPlaneswalkerActivation(-4) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top two cards of his or her library. "+
                "PN puts all land cards from among them onto the battlefield and the rest into his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(2);
            game.doAction(new RevealAction(cards));
            for (final MagicCard card : cards) {
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary, card, player));
                } else {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
