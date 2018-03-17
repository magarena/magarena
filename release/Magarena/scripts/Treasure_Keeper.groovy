def castAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList revealed = new MagicCardList(event.getRefCardList());
    if (event.isYes()) {
        final MagicCard toCast = revealed.last();
        revealed.remove(toCast);
        game.doAction(CastCardAction.WithoutManaCost(
            event.getPlayer(),
            toCast,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.Graveyard
        ));
    }
    revealed.each {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
    }
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
            final MagicCardList nonTarget = (MagicCardList)library.takeWhile({ !predicate(it) });
            if (library.any(predicate)) {
                final MagicCard target = library.find(predicate);
                game.doAction(new RevealAction(nonTarget.plus(target)));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Cast the card?"),
                    new MagicCardList(nonTarget.plus(target)),
                    castAction,
                    "PN may\$ cast that card without paying its mana cost. " +
                    "Put all revealed cards not cast this way on the bottom of PN's library in any order."
                ));
            } else {
                game.doAction(new RevealAction(nonTarget));
                nonTarget.each {
                    game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
                }
            }
        }
    }
]

