[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.isKicked() ? 4 : 2;
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals 2 damage to target creature or player\$. "+
                "If SN was kicked, it deals 4 damage to that creature or player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.isKicked() ? 4 : 2;
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
