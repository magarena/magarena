[
    new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent,final MagicCard card) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(),
                this,
                "PN may\$ gain 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),1));
            }
        }
    }
]
