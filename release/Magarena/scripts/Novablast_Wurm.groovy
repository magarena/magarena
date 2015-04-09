[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Destroy all creatures other than SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final Collection<MagicPermanent> targets= game.filterPermanents(
                permanent.getController(),
                CREATURE.except(permanent)
            );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
