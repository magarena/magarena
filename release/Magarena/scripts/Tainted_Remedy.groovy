[
    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            int amount = 0;
            if (permanent.isOpponent(act.getPlayer()) && act.getLifeChange() > 0) {
                amount = act.getLifeChange();
                act.setLifeChange(0);
                return new MagicEvent(
                    permanent,
                    act.player,
                    amount,
                    this,
                    "PN loses RN Life."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-event.getRefInt()));
        }
    }
]
