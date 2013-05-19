[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target artifact or enchantment\$. " +
                "PN gains 3 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
                }
            });
        }
    }
]
