def sourceEvent = MagicRuleEventAction.create("Tap target creature an opponent controls.");

[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.getEnchantedPermanent() == creature ?
                sourceEvent.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
