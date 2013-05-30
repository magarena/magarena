[
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL
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
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_BLUE_CREATURE_YOU_CONTROL
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
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    if (creature.isTapped() && 
                        (creature.hasColor(MagicColor.Blue) || 
                         creature.hasColor(MagicColor.Green))) {
                        game.doAction(new MagicUntapAction(creature));
                    }
                }
            }
            return MagicEvent.NONE;
        }
    }
]
