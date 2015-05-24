[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "PN gains 2 life for each Mountain target opponent\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer opponent ->
                final int amount = opponent.getNrOfPermanents(MagicSubType.Mountain);
                final MagicPlayer player = event.getPlayer();
                game.logAppendValue(player, amount);
                game.doAction(new ChangeLifeAction(player, amount*2));
            });
        }
    }
]
