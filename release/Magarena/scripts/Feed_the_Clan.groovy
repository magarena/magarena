[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 5 life. " +
                "PN gains 10 life instead if he or she controls a creature with power 4 or greater."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.controlsPermanent(CREATURE_POWER_4_OR_MORE) ? 10 : 5;
            game.doAction(new ChangeLifeAction(player, amount));
        }
    }
]
