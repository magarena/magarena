def EFFECT = MagicRuleEventAction.create("Tap target creature an opponent controls.");

[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.getEnchantedPermanent() == creature ?
                EFFECT.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
