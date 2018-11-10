[
    new AtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "If PN controls four or more Demons with different names, PN wins the game."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> demons = event.getPlayer().getPermanents().findAll({ it.hasSubType(MagicSubType.Demon) });
            if (demons.unique(false, { it.getName() }).size() >= 4) {
                game.doAction(new LoseGameAction(event.getPlayer().getOpponent()));
            }
        }
    }
]
