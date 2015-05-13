[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target enchantment\$ and all other enchantments " +
                "with the same name as that enchantment."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                    ENCHANTMENT,
                    it.getName()
                );
                targetFilter.filter(event) each {
                    final MagicPermanent permanent ->
                    game.doAction(new DestroyAction(permanent));
                }
            });
        }
    }
]
