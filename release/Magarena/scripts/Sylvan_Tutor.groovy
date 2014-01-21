def act = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo() == false) {
        event.processTargetCard(game, {
            final MagicCard card ->
            game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
            game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
        });
    }
}

def evt = {
    final MagicEvent event, final MagicChoice choice ->
    return new MagicEvent(
        event.getSource(),
        event.getPlayer(), 
        choice,
        act,
        "PN may search his or her library for a creature card, reveals it, shuffle his or her library, and put that card on top of it."
    );
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may search his or her library for a creature card, reveals it, shuffle his or her library, and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                MagicTargetChoice.CREATURE_CARD_FROM_LIBRARY
            ));
        }
    }
]
