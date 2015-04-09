[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    NEG_TARGET_PLAYER,
                    POS_TARGET_PLAYER
                ),
                payedCost.getX(),
                this,
                "Choose one\$ - SN deals X damage to target player; " +
                "or target player gains X life.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            if (event.isMode(1)) {
                event.processTargetPlayer(game, {
                    game.doAction(new MagicDealDamageAction(event.getSource(),it,X));
                });
            } else if (event.isMode(2)) {
                event.processTargetPlayer(game, {
                    game.doAction(new MagicChangeLifeAction(it,X));
                });
            }
        }
    }
]
