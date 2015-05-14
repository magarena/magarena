[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    POS_TARGET_PLAYER,
                    POS_TARGET_CREATURE_OR_PLAYER
                ),
                amount,
                this,
                "Choose one\$ — (1) target player gains X life; " +
                "or (2) prevent the next X damage that would be dealt to target creature or player this turn.\$ (X=${amount})" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            if (event.isMode(1)) {
                event.processTargetPlayer(game, {
                    game.doAction(new ChangeLifeAction(it,X));
                });
            } else if (event.isMode(2)) {
                event.processTarget(game, {
                game.doAction(new PreventDamageAction(it,X));
                });
            }
        }
    }
]
