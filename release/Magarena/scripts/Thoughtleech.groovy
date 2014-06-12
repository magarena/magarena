[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (permanent.isEnemy(tapped) && tapped.hasSubType(MagicSubType.Forest)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ gain 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
            }
        }
    }
]
