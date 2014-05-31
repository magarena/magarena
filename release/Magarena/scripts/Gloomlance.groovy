[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$. If that creature was green or white, its controller discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                if (permanent.hasColor(MagicColor.Green) || permanent.hasColor(MagicColor.White)) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),permanent.getController(),1));
                }
            });
        }
    }
]
