[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "Put a +1/+1 counter on RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return creature == permanent.getEnchantedPermanent() ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN.") :
                MagicEvent.NONE;
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return creature == permanent.getEnchantedPermanent() ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN.") :
                MagicEvent.NONE;
        }
    }
]
