[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasColor(MagicColor.White) && cardOnStack.isEnemy(permanent)) ? 
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "PN loses 1 life and "+permanent.getController().toString()+" gains 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
            game.doAction(new MagicChangeLifeAction(event.getPermanent().getController(),1));
        }
    }
]
