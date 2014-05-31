[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. Its controller discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                game.addEvent(new MagicDiscardEvent(event.getSource(),permanent.getController(),1));
            });
        }
    }
]
