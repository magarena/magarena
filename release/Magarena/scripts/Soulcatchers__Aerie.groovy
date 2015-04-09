[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.hasSubType(MagicSubType.Bird) && 
                    otherPermanent.getCard().isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a feather counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Feather,
                1
            ));
        }
    },   
    new MagicStatic(
        MagicLayer.ModPT,
        multiple("Bird creatures")
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = source.getCounters(MagicCounterType.Feather);
            pt.add(amount,amount);
        }
    }
]
