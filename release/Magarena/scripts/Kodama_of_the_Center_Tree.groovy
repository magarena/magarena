[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            final int amount = game.filterPermanents(player,MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL).size();
            pt.set(amount, amount);
        }
    },
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final int cmc = permanent.getGame().filterPermanents(
                permanent.getController(),
                MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL
            ).size()+1;
            return new MagicSoulshiftTrigger(cmc).getEvent(permanent);
        }
    }
]
