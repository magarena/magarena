[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    NEG_TARGET_CREATURE_OR_PLAYER
                ),
                amount,
                this,
                "Choose one\$ - (1) draw X cards; " +
                "or (2) SN deals X damage to target creature or player.\$ (X=${amount})" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            if (event.isMode(1)) {
                game.doAction(new DrawAction(event.getPlayer(),amount));
            } else if (event.isMode(2)) {
                event.processTarget(game, {
                    game.doAction(new DealDamageAction(event.getSource(),it,amount));
                });
            }
        }
    }
]
