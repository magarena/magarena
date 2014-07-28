def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTarget(game, {
        final MagicDamage damage = new MagicDamage(event.getSource(),it,event.getRefInt());
        game.doAction(new MagicDealDamageAction(damage));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact or enchantment\$." +
                "If SN was kicked, it deals damage equal to that permanent's converted mana cost to target creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (!event.isKicked()) {
                    return;
                }
                final int amount = it.getConvertedCost();
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(amount),
                    amount,
                    action,
                    "SN deals RN damage to target creature\$."
                ));
            });
        }
    }
]
