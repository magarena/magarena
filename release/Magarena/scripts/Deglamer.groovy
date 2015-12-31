[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicExileTargetPicker.create(),
                this,
                "PN chooses target artifact or enchantment\$. " +
                "Its owner shuffles it into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersLibrary));
            });
        }
    }
]
