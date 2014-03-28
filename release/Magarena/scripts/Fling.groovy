[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to RN's power to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                // temp fix for case where additional cost is not payed when spell is not cast form hand
                event.processRefPermanent(game, {
                    final MagicPermanent sacrificed ->
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,sacrificed.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                });
            });
        }
    }
]
