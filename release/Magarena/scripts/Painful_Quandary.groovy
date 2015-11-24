[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.isEnemy(permanent) ? 
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.LOSE_LIFE,
                        5,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may lose 5 life\$ unless PN discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-5));
            } else {
                game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer()));
            }
        }
    }
]
