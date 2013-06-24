[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_MYR_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Untap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{1}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Untap each other Myr you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_MYR_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                if (target != permanent) {
                    game.doAction(new MagicUntapAction(target));
                }
            }
        }
    }
]
