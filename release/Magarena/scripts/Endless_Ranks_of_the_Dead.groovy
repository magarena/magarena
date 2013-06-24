[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts X 2/2 black Zombie creature tokens onto the " +
                    "battlefield, where X is half the number of Zombies you control, rounded down"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(player,MagicTargetFilter.TARGET_ZOMBIE_YOU_CONTROL);
            def amount = targets.size().intdiv(2);
            game.doAction(new MagicPlayTokensAction(player,TokenCardDefinitions.get("Zombie"),amount));;
        }
    }
]
