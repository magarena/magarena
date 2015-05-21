[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses 1 life for each attacking creature PN controls. PN gains that much life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = ATTACKING_CREATURE_YOU_CONTROL.filter(player).size();
                game.logAppendMessage(player, "("+amount+")");
                game.doAction(new ChangeLifeAction(it, -amount));
                game.doAction(new ChangeLifeAction(player, amount));
            });
        }
    }
]
