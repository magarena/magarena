[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact or enchantment.\$ If that permanent was blue or black, PN draws a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new DestroyAction(permanent));
                if (permanent.hasColor(MagicColor.Blue) || permanent.hasColor(MagicColor.Black)) {
                    game.doAction(new DrawAction(event.getPlayer()));
                }
            });
        }
    }
]
