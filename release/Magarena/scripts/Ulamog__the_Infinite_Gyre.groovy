[
    new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                MagicTargetChoice.NEG_TARGET_PERMANENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target permanent\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicDestroyAction(creature));
            });
        }
    }
]
