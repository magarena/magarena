[
    new AtBeginOfCombatTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return permanent.isController(turnPlayer);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "If PN controls three or more creatures, SN gets +1/+1 until end of turn and PN gains 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            if (player.getNrOfPermanents(MagicType.Creature) >= 3) {
                game.doAction(new ChangeTurnPTAction(permanent, 1, 1);
                game.doAction(new ChangeLifeAction(player, 1));
            }
        }
    }
]
