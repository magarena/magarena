[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final int cmc = permanent.getGame().filterPermanents(
                permanent.getController(),
                SPIRIT_YOU_CONTROL
            ).size()+1;
            return new MagicSoulshiftTrigger(cmc).executeTrigger(game, permanent, died);
        }
    }
]
