[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died.isNonToken() &&
                    died.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 1 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getPermanent(), event.getPlayer(), 1));
        }
    }
]
