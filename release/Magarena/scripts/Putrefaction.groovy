[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasColor(MagicColor.Green) || cardOnStack.hasColor(MagicColor.White)) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "PN discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer()));
        }
    }
]
