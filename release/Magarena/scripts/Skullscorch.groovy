def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),4));
    } else {
        game.addEvent(MagicDiscardEvent.Random(event.getSource(), event.getPlayer(), 2));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards two cards at random unless that player has SN deal 4 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("have SN deal 4 damage to you?"),
                    action,
                    "PN may\$ have SN deal 4 damage to you. If you don't, discard two cards at random."
                ));
            });
        }
    }
]
