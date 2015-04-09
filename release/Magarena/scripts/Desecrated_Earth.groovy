[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. Its controller discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.addEvent(new MagicDiscardEvent(event.getSource(),it.getController(),1));
            });
        }
    }
]
