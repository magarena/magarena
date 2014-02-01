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
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final int cmc = permanent.getGame().filterPermanents(
                permanent.getController(),
                MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL
            ).size()+1;
            return new MagicSoulshiftTrigger(cmc).executeTrigger(game, permanent, died);
        }
    }
]
