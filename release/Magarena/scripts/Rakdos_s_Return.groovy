[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "SN deals " + amount + " damage to target opponent\$. That player discards " + amount + " cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getCardOnStack().getX();
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                game.addEvent(new MagicDiscardEvent(event.getSource(),it, amount));
            });
        }
    }
]
