def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return target.containsInChoiceResults(permanent.getEnchantedPermanent()) ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
