def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTarget(game, {
        final int manaCost = event.getRefInt();
        game.logAppendMessage(event.getPlayer()," ("+manaCost+")");
        game.doAction(new DealDamageAction(event.getSource(),it,manaCost));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact or enchantment\$." +
                "If SN was kicked, it deals damage equal to that permanent's converted mana cost to target creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (!event.isKicked()) {
                    return;
                }
                final int amount = it.getConvertedCost();
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(amount),
                    amount,
                    action,
                    "SN deals RN damage to target creature\$."
                ));
            });
        }
    }
]
