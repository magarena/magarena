def action = {
    final MagicGame game, final MagicEvent event ->
    final Control control = player.hasState(MagicPlayerState.CitysBlessing) ? Control.Opp : Control.Any;
    permanent(MagicType.Creature, control).filter(player).each {
        game.doAction(new ChangeTurnPTAction(it, -2, -2));
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
            game.addEvent(new MagicEvent(
                event.getSource(),
                action,
                "All creatures get -2/-2 until end of turn. " +
                "If PN has the city's blessing, instead only creatures PN's opponent controls get -2/-2 until end of turn."
            ));
        }
    }
]


