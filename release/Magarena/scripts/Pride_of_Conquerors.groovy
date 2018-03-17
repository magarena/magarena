def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final int amount = player.hasState(MagicPlayerState.CitysBlessing) ? 2 : 1;
    CREATURE_YOU_CONTROL.filter(player).each {
        game.doAction(new ChangeTurnPTAction(it, amount, amount));
    }
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
            game.addEvent(
                event.getSource(),
                action,
                "Creatures PN controls get +1/+1 until end of turn. " +
                "If PN has the city's blessing, those creatures get +2/+2 until end of turn instead."
            );
        }
    }
]

