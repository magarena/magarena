[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return game.getNrOfPermanents(MagicType.Land) >= 7 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If there are 7 or more lands in play, sacrifice SN and destroy all lands."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (game.getNrOfPermanents(MagicType.Land) >= 7) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.doAction(new DestroyAction(LAND.filter(event)));
            }
        }
    }
]
