def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),6));
    } else {
        game.doAction(new MagicMillLibraryAction(event.getRefPlayer(),6));
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
                "Unless opponent has SN deal 6 damage to him or her, " +
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
                    "PN may\$ have SN deal 6 damage to you. If you don't, " +
                    "RN puts the top six cards of his or her library into his or her graveyard."
                ));
            });
        }
    }
]
