def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final int amount = player.hasState(MagicPlayerState.CitysBlessing) ? 3 : 2;
    game.doAction(new DrawAction(player, amount));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Ascend."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicRuleEventAction.create("ascend").getEvent(event));
            game.addEvent(new MagicEvent(
                event.getSource(),
                action,
                "PN draws two cards." +
                "If PN has the city's blessing, PN draws three cards instead."
            ));
        }
    }
]


