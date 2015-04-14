[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "SN deals 3 damage to target player\$. That player discards two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
                game.addEvent(new MagicDiscardEvent(event.getSource(),it,2));
            });
        }
    }
]
