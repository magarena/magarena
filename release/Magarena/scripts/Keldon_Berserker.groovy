[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.getController().controlsPermanent(MagicTargetFilterFactory.UNTAPPED_LAND) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gains +3/+0 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicTargetFilterFactory.UNTAPPED_LAND) == false) {
                game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,0));
            }
        }
    }
]
