[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals cards from the top of PN's library until PN reveals an artifact card. " +
                "PN puts that card into PN's hand and the rest on the bottom of PN's library in a random order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            def predicate = { final MagicCard card -> card.hasType(MagicType.Artifact) };
            final MagicCardList revealed = new MagicCardList();
            while (library.size() > 0) {
                final MagicCard topCard = library.getCardAtTop();
                game.doAction(new RevealAction(topCard));
                if (predicate(topCard)) {
                    game.doAction(new ShiftCardAction(topCard, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                    break;
                } else {
                    revealed.add(topCard);
                    game.doAction(new RemoveCardAction(topCard, MagicLocationType.OwnersLibrary));
                }
            }

            revealed.shuffle();
            revealed.each({ game.doAction(new MoveCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });
        }
    }
]

