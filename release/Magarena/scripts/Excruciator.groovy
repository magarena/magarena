[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (permanent == damage.getSource()) {
                damage.setUnpreventable();
            }
            return MagicEvent.NONE;
        }
    }
]
