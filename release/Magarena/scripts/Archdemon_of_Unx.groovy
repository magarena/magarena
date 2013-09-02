[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a non-Zombie creature, then put a 2/2 black Zombie creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                player,
                MagicTargetChoice.SACRIFICE_NON_ZOMBIE
            ));
            game.doAction(new MagicPlayTokenAction(
                player,
                TokenCardDefinitions.get("Zombie")
            ));
        }
    }
]
