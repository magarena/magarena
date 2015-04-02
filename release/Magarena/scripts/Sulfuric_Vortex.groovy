[
    new MagicIfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeLifeAction act) {
            if (act.getLifeChange() > 0) {
                act.setLifeChange(0);
            }
            return MagicEvent.NONE;
        }
    }
]
