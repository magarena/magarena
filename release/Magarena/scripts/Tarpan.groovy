[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
        }
    }
]
