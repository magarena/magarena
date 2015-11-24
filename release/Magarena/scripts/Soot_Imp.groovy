[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasColor(MagicColor.Black)) ? 
                MagicEvent.NONE:
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "PN loses 1 life."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-1));
        }
    }
]
