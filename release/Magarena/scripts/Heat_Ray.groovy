[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals "+amount+" damage to target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,event.getCardOnStack().getX());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
