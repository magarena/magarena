def UNTAPPED_ISLAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Island) && target.isUntapped() && target.isController(player);
    }
};

[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                eotPlayer,
                this,
                "Tap all untapped Islands PN controls and Monsoon deals X damage to him or her, "+
                "where X is the number of Islands tapped this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = UNTAPPED_ISLAND_YOU_CONTROL.filter(player).size();
            game.logAppendX(player, amount);
            if (amount > 0) {
                UNTAPPED_ISLAND_YOU_CONTROL.filter(player) each {
                    game.doAction(new TapAction(it));
                }
                game.doAction(new DealDamageAction(event.getSource(), player, amount));
            }
        }
    }
]
