[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other.isCreature() && other.isEnemy(permanent);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(),
                other.getController(),
                this,
                "PN may\$ have RN lose 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getRefPlayer(),-1));
            }
        }
    }
]
