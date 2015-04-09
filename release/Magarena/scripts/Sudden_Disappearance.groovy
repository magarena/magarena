[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Exile all nonland permanents target player\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                NONLAND_PERMANENT_YOU_CONTROL.filter(it) each {
                    final MagicPermanent nonland ->
                    game.doAction(new MagicExileUntilEndOfTurnAction(nonland));
                }
            });
        }
    }
]
