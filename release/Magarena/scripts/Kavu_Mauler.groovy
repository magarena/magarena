[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +1/+1 until end of turn for each other attacking Kavu."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent()
            final Collection<MagicPermanent> creatures = game.filterPermanents(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.ATTACKING_KAVU,
                    permanent
                )
            );
            final int amount = creatures.size();
            game.doAction(new MagicChangeTurnPTAction(permanent,amount,amount));
        }
    }
]
