[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player draws X cards. (X="+amount+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player,amount));
            }
        }
    }
]
