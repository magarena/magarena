[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "PN gains 3 life for each black creature target opponent\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = BLACK_CREATURE_YOU_CONTROL.filter(it).size();
                game.logAppendValue(player,amount)
                game.doAction(new ChangeLifeAction(player, amount*3));
            });
        }
    }
]
