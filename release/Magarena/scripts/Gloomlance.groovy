[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$. If that creature was green or white, its controller discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (it.hasColor(MagicColor.Green) || it.hasColor(MagicColor.White)) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),it.getController(),1));
                }
            });
        }
    }
]
