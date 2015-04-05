[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Each other creature you control gets +X/+X until end of turn, where X is SN's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int X = permanent.getPower();
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                permanent
            );
            final Collection targets = game.filterPermanents(event.getPlayer(),filter);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,X,X));
            }
        }
    }
]
