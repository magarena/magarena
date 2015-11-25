[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +X/+0 until end of turn, where X is the number of lands defending player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = game.getDefendingPlayer().getNrOfPermanents(MagicType.Land);
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), +X, 0));
        }
    }
]
