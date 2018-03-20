def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer opponent = event.getPlayer();
    final int amount = event.getSource().getController().hasState(MagicPlayerState.CitysBlessing) ?
        (opponent.getNrOfPermanents(MagicType.Creature) + 1).intdiv(2).toInteger() :
        1;
    amount.times {
        game.addEvent(new MagicSacrificePermanentEvent(
            event.getSource(),
            opponent,
            SACRIFICE_CREATURE
        ));
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

            final MagicPlayer opponent = event.getSource().getOpponent();
            game.addEvent(new MagicEvent(
                event.getSource(),
                opponent,
                event.getPlayer(),
                action,
                "PN sacrifices a creature. " +
                "If RN has the city's blessing, instead PN sacrifices half the creatures PN controls, rounded up."
            ));
        }
    }
]


