def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),5));
    } else {
        game.doAction(new DrawAction(event.getRefPlayer(),3));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "PN's opponent may have SN deal 5 damage to them. " +
                "If they don't, target player\$ draws three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer().getOpponent(),
                    new MagicMayChoice("Have SN deal 5 damage to you?"),
                    it,
                    action,
                    "PN may\$ have SN deal 5 damage to them. If they don't, RN draws 3 cards."
                ));
            });
        }
    }
]
