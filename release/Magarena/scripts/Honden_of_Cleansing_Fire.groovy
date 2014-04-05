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
                "PN gains 2 life for each Shrine he or she controls."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int size = 2*(game.filterPermanents(event.getPlayer(),SHRINES_YOU_CONTROL).size());
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),size));
        }
    }
]
