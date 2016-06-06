[
    new MagicPlaneswalkerActivation(-9) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(7),
                this,
                "SN deals 7 damage to target player\$. That player discards 7 cards, then sacrifices 7 permanents."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 7));
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, 7));
                for (int i=7;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        it,
                        SACRIFICE_PERMANENT
                    ));
                }
            });
        }
    }
]
