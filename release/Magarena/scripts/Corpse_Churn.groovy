def choice = new MagicTargetChoice("a creature card from your graveyard");

def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.logAppendMessage(event.getPlayer(),"Return a creature card? (Yes)");
        event.processTargetCard(game, {
            game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            game.logAppendMessage(event.getPlayer(), "${it.getName()} is returned to it's owners hand.");
        });
    } else {
        game.logAppendMessage(event.getPlayer(), "Return a creature card? (No)");
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts the top three cards of his or her library into his or her graveyard, "+
                "then PN may return a creature card from his or her graveyard to his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MillLibraryAction(player, 3));
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicMayChoice(choice),
                action,
                ""
            ));
        }
    }
]

