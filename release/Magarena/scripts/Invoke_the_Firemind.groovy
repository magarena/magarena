[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    NEG_TARGET_CREATURE_OR_PLAYER
                ),
                payedCost.getX(),
                this,
                "Choose one\$ - (1) draw X cards; " +
                "or (2) SN deals X damage to target creature or player.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            game.logAppendMessage(event.getPlayer(),"(X="+amount+")");
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
