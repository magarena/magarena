[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.isEnemy(permanent) && 
                    spell.hasColor(MagicColor.White)) ?
                new MagicEvent(
                    permanent,
                    spell.getController(),
                    this,
                    "PN loses 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
        }
    }
]
