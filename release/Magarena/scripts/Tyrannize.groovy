def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicPayLifeEvent(event.getSource(), event.getPlayer(), 7));
    } else {
        game.addEvent(new MagicDiscardHandEvent(event.getSource(), event.getPlayer()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Pay 7 life?"),
                    action,
                    "may\$ pay 7 life. if PN doesn't, PN discards his or her hand"
                ));
            })
        }
    }
]

