def ARTIFACT_CARD_FROM_HAND = new MagicTargetChoice("an artifact card from your hand");

def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent discardEvent = new MagicDiscardChosenEvent(event.getSource(), event.getPlayer(), ARTIFACT_CARD_FROM_HAND)
    if (event.isYes() && discardEvent.isSatisfied()) {
        game.addEvent(discardEvent);
    } else {
        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 2));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN\$ draws three cards. PN then discards two cards unless he or she discards an artifact card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player, 3));
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicMayChoice("Discard an artifact card?"),
                action,
                ""
            ));
        }
    }
]

