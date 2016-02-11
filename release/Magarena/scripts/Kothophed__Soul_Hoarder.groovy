[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (!died.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card and loses 1 life."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           final MagicPlayer player = event.getPlayer()
           game.doAction(new DrawAction(player));
           game.doAction(new ChangeLifeAction(player, -1));
        }
    }
]
