def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),6));
    } else {
        game.doAction(new MillLibraryAction(event.getRefPlayer(),6));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Any player may have SN deal 6 damage to him or her. If no one does, " +
                "target player\$ puts the top six cards of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer().getOpponent(),
                    new MagicMayChoice("have SN deal 6 damage to you?"),
                    it,
                    action,
                    "PN may\$ have SN deal 6 damage to him or her. If PN doesn't, " +
                    "RN puts the top six cards of his or her library into his or her graveyard."
                ));
            });
        }
    }
]
