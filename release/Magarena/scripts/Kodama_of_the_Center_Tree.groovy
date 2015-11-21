[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final int cmc = SPIRIT_YOU_CONTROL.filter(permanent).size() + 1;
            return new SoulshiftTrigger(cmc).executeTrigger(game, permanent, died);
        }
    }
]
