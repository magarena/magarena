def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.getCounters(MagicCounterType.Arrowhead) == 0 ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
