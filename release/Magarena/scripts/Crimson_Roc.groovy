def EFFECT = MagicRuleEventAction.create("SN gets +1/+0 and gains first strike until end of turn.");

[
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return blocked.isCreature() && blocked.hasAbility(MagicAbility.Flying) == false ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
