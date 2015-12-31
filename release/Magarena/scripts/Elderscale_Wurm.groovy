[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (permanent.getController().getLife() < 7) ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN's life total is less than 7, PN's life total becomes 7."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int life = player.getLife();
            if (life < 7) {
                game.doAction(new ChangeLifeAction(player, 7 - life))
            }
        }
    },
    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            if (permanent.isController(act.getPlayer()) &&
                act.isDamage() &&
                act.getOldLife() >= 7 &&
                act.getNewLife() < 7) {
                act.setLifeChange(7 - act.getOldLife());
            }
            return MagicEvent.NONE;
        }
    }
]
