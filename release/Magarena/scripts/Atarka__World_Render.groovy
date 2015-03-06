def EFFECT = MagicRuleEventAction.create("SN gains double strike until end of turn.");

[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature.isFriend(permanent) &&
                    creature.hasSubType(MagicSubType.Dragon)) ?
                EFFECT.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
