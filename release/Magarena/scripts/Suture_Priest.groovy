[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other != permanent && other.isCreature() && permanent.isEnemy(other);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                    1,
                    MagicSimpleMayChoice.DEFAULT_YES
                ),
                other.getController(),
                this,
                "PN may\$ have RN lose 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(), -1));
            }
        }
    }
]
