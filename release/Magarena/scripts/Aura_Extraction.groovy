[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_ENCHANTMENT,
                MagicBounceTargetPicker.create(),
                this,
                "Put target enchantment\$ on top of its owner's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent enchantment ->
                game.doAction(new MagicRemoveFromPlayAction(
                    enchantment,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            });
        }
    }
]
