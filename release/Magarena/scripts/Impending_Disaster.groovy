[
    new MagicAtYourUpkeepTrigger() {
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
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                final Collection<MagicPermanent> lands = game.filterPermanents(LAND);
                for (final MagicPermanent land : lands) {
                    game.doAction(new MagicDestroyAction(land));
                }
            }
        }
    }
]
