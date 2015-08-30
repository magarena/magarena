[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    NEG_TARGET_PLAYER,
                    POS_TARGET_PLAYER
                ),
                amount,
                this,
                "Choose one\$ — (1) SN deals X damage to target player; " +
                "or (2) target player gains X life.\$ (X=${amount})" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            if (event.isMode(1)) {
                event.processTargetPlayer(game, {
                    game.doAction(new DealDamageAction(event.getSource(),it,X));
                });
            } else if (event.isMode(2)) {
                event.processTargetPlayer(game, {
                    game.doAction(new ChangeLifeAction(it,X));
                });
            }
        }
    }
]
