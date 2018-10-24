def action = {
    final MagicGame game, final MagicEvent event ->
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int BC = BLACK_CREATURE_YOU_CONTROL.filter(player).size();
                game.logAppendValue(player,BC)
                for (int i = 0; i < BC; i++) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), it, SACRIFICE_CREATURE));
                }
                final int RC = RED_CREATURE_YOU_CONTROL.filter(player).size();
                game.logAppendValue(player,RC)
                for (int i = 0; i < RC; i++) {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), it, SACRIFICE_LAND));
        }
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN create a 5/5 black and red Elemental creature token."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("5/5 black and red Elemental creature token"), 1));
            game.doAction(new PutItemOnStackAction(new MagicTriggerOnStack(new MagicEvent(
                event.getSource(),
                TARGET_OPPONENT,
                action,
                "Target opponent\$ sacrifices a creature for each black creature PN control, "+
                "then sacrifices a land for each red creature PN control."
            ))));
        }
    }
]
