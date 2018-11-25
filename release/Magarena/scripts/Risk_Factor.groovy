def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), 4));
    } else {
        game.doAction(new DrawAction(event.getRefPlayer(), 3));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                ""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicEvent(
                source,
                player.getOpponent(),
                new MagicMayChoice("Have ${source} deal 4 damage to you?"),
                player,
                action,
                "PN may\$ have SN deal 4 damage to them. If PN doesn't, RN draws 3 cards."
            ));
        }
    }
]
