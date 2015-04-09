[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ARTIFACT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact\$ and all other artifacts " +
                "with the same name as that artifact."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                    ARTIFACT,
                    it.getName()
                );
                targetFilter.filter(game) each {
                    final MagicPermanent permanent ->
                    game.doAction(new MagicDestroyAction(permanent));
                }
            });
        }
    }
]
