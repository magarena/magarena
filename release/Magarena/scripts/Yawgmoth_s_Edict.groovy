[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasColor(MagicColor.White) && cardOnStack.isEnemy(permanent)) ? 
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    cardOnStack.getController(),
                    this,
                    "RN loses 1 life and PN gains 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getRefPlayer(),-1));
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
        }
    }
]
