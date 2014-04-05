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
                permanent.getController(),
                this,
                "PN puts a 1/1 colorless Spirit creature token onto the battlefield for each Shrine he or she control."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int size = (game.filterPermanents(event.getPlayer(),SHRINES_YOU_CONTROL).size());
            game.doAction(new MagicPlayTokensAction(event.getPlayer(),TokenCardDefinitions.get("1/1 colorless Spirit creature token"),size));
        }
    }
]
