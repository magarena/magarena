def action = {
    final MagicGame game, final MagicPlayer player ->
    if (event.isYes()) {
        game.addEvent(new MagicPayLifeEvent(event.getSource(), event.getPlayer(), 3));
        game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
    } else {
        game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Reveal the top three cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList revealedCards = event.getPlayer().getLibrary().getCardsFromTop(3);
            game.doAction(new RevealAction(revealedCards));
            revealedCards.each {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer().getOpponent(),
                    new MagicMayChoice("Pay 3 life?"),
                    it,
                    action,
                    "Put RN into ${event.getPlayer()}'s hand unless PN pays 3 life\$."
                ));
            }
        }
    }
]

