def EFFECT = MagicRuleParser.create("SN gets +1/+2 until end of turn.");

[
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return blocked.isCreature() && blocked.hasColor(MagicColor.Black) ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
