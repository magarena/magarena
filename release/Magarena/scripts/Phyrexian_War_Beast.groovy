[
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction data) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices a land and SN deals 1 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.addEvent(new MagicSacrificePermanentEvent(permanent,A_LAND_YOU_CONTROL));
            game.doAction(new MagicDealDamageAction(permanent,permanent.getController(),1));
        }
    }
]
