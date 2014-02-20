[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicBounceTargetPicker.create(),
                this,
                "Put target artifact, or enchantment\$ on top of its owner's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicRemoveFromPlayAction(
                    permanent,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            });
        }
    }
]
