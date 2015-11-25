[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +X/+X until end of turn, where X is the number of Clerics on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = game.getNrOfPermanents(MagicSubType.Cleric);
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), +X, +X));
        }
    }
]
