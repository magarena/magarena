def castAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList revealed = new MagicCardList(event.getRefCardList());
    if (event.isYes()) {
        final MagicCard toCast = revealed.last();
        game.doAction(CastCardAction.WithoutManaCost(
            event.getPlayer(),
            toCast,
            MagicLocationType.OwnersLibrary,
            MagicLocationType.Graveyard
        ));
        revealed.remove(toCast);
    }
    Collections.shuffle(revealed);
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
            def targetPredicate = { !it.hasType(MagicType.Land) && it.getConvertedCost() <= 3 }
            def nonTarget = library.takeWhile(targetPredicate.negate());
            if (library.any(targetPredicate)) {
                final MagicCard target = library.find(targetPredicate);
                game.doAction(new RevealAction(nonTarget.plus(target)));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Cast the card?"),
                    nonTarget.plus(target),
                    castAction,
                    "PN may\$ cast that card without paying its mana cost. " +
                    "Put all revealed cards not cast this way on the bottom of PN's library in any order."
                ));
            } else {
                game.doAction(new RevealAction(nonTarget));
                Collections.shuffle(nonTarget);
                nonTarget.each {
                    game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
                }
            }
        }
    }
]

