[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +X/+X until end of turn, where X is the number of Clerics in play."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount =game.getNrOfPermanents(MagicSubType.Cleric);
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,amount));
        }
    }
]
