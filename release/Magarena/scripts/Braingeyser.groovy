[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ draws "+amount+" cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getCardOnStack().getX();
                game.doAction(new MagicDrawAction(it,amount));
            });
        }
    }
]
