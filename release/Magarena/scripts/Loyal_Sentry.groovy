[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent defender) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == defender && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "Destroy both RN and SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDestroyAction(event.getRefPermanent()));
            game.doAction(new MagicDestroyAction(event.getPermanent()));
        }
    }
]
