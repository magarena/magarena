[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "PN gain 2 life for each creature target player\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer it ->
                final int amount = it.getNrOfPermanents(MagicType.Creature);
                final MagicPlayer player = event.getPlayer();
                game.logAppendValue(player, amount);
                game.doAction(new ChangeLifeAction(player, amount*2));
            });
        }
    }
]

