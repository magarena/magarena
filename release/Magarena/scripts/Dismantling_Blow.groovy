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
                "If SN was kicked, draw two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (event.isKicked()) {
                    game.doAction(new MagicDrawAction(event.getPlayer(),2));
                }
            });
        }
    }
]
