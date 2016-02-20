[
    new ThisBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                    permanent,
                    permanent.getBlockedCreature(),
                    this,
                    "Destroy RN and SN."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getPermanent()));
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
