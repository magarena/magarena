def SHRINES_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.hasSubType(MagicSubType.Shrine);
        }
    };

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (permanent.getController() == upkeepPlayer) ? new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card for each Shrine PN controls."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int size = (game.filterPermanents(event.getPlayer(),SHRINES_YOU_CONTROL).size());
            event.processTarget(game, {
                final MagicPlayer player ->
                game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, size));
            });
        }
    }
]
