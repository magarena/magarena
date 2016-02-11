[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died.isNonToken()) ?
                new MagicEvent(
                    permanent,
                    died.getOwner(),
                    this,
                    "PN loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    }
]
