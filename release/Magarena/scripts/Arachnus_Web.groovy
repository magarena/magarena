[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return permanent.getEnchantedPermanent().getPower() >= 4 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getPermanent()));
        }
    }
]
