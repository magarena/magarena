[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            return (played.isEnemy(permanent) && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    this,
                    "PN may\$ gain 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),3));
            }
        }
    }
]
