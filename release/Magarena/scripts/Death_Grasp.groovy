[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals " + amount + " damage to target creature or player\$. " +
                "PN gains " + amount + " life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
