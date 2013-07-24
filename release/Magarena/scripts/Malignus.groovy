[
    new MagicCDA() {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPlayer player,
                final MagicPowerToughness pt) {
            // floor((x + 1) / 2) == ceil(x / 2)
            def amount = (player.getOpponent().getLife() + 1).intdiv(2);
            pt.set(amount,amount);
        }
    },
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            if (permanent == damage.getSource()) {
                // Generates no event or action.
                damage.setUnpreventable();
            }
            return MagicEvent.NONE;
        }
    }
]
