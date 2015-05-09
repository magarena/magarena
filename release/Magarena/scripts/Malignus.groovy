[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            def amount = (player.getOpponent().getLife() + 1).intdiv(2);
            pt.set(amount,amount);
        }
    },
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (permanent == damage.getSource()) {
                damage.setUnpreventable();
            }
            return MagicEvent.NONE;
        }
    }
]
