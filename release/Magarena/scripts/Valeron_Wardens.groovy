def EFFECT = MagicRuleEventAction.create("Draw a card.");

[
    new BecomesStateTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
            return data.permanent.isCreature() && permanent.isFriend(data.permanent) && data.state == MagicPermanentState.Renowned;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeStateAction data) {
            return EFFECT.getEvent(permanent);
        }
    }
]
