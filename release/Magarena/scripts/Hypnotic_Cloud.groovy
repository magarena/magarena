[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards a card. " +
                "If SN was kicked, that player discards 3 cards instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                int amount = event.isKicked() ? 3 : 1;
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, amount));
            });
        }
    }
]
