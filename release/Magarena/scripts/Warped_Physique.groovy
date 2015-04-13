[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Target creature\$ gets +X/-X until end of turn, where X is the number of cards in PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = player.getHandSize();
                game.doAction(new ChangeTurnPTAction(it,+amount,-amount));
            });
        }
    }
]
