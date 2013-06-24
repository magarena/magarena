def MAY_GAIN_ONE_LIFE = new MagicEventAction() {
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(), 1));
        }
    }
};

def MAY_LOSE_ONE_LIFE = new MagicEventAction() {
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(), -1));
        }
    }
};

[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other != permanent && other.isCreature();
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return permanent.isFriend(other) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    MAY_GAIN_ONE_LIFE,
                    "PN may\$ gain 1 life."
                ):
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    MAY_LOSE_ONE_LIFE,
                    "PN may\$ have " + other.getController() + " lose 1 life."
                );
        }
    }
]
