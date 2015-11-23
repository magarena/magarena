[
    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            if (permanent.isController(act.getPlayer()) && 
                act.isDamage() &&
                act.getOldLife() >= 1 && 
                act.getNewLife() < 1) {
                act.setLifeChange(1 - act.getOldLife());
            }
            return MagicEvent.NONE;
        }
    }
]
