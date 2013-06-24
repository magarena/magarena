[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    otherPermanent.getController(),
                    this,
                    "PN may\$ have RN lose 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getRefPlayer(),-1));
            }
        }
    }
]
