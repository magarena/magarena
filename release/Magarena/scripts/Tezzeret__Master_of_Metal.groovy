[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals cards from the top of PN's library until PN reveals an artifact card. " +
                "Put that card into PN's hand and the rest on the bottom of PN's library in a random order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            def predicate = { final MagicCard card -> card.hasType(MagicType.Artifact) };
            final MagicCardList nonTarget = (MagicCardList)library.takeWhile({ !predicate(it) });
            if (library.any(predicate)) {
                final MagicCard target = library.find(predicate);
                game.doAction(new RevealAction(nonTarget.plus(target)));
                game.doAction(new ShiftCardAction(target, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
            } else {
                game.doAction(new RevealAction(nonTarget));
            }
            nonTarget.shuffle();
            nonTarget.each({ game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });
        }
    }
]

