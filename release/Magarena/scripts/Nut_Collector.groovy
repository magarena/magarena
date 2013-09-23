[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PLAY_TOKEN,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a 1/1 Squirrel " +
                    "creature token onto the battlefield." 
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Squirrel1")));
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            if (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) {
				final Collection<MagicPermanent> targets =
					game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_SQUIRREL_CREATURE);
				for (final MagicPermanent target : targets) {
					game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
				}
//				pt.add(2,2);
            }
        }
    }
]
