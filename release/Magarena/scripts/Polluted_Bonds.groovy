[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other.isLand() && other.isEnemy(permanent);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return new MagicEvent(
                permanent,
                other.getController(),
                this,
                "PN loses 2 life and ${permanent.getController().getName()} gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            game.doAction(new ChangeLifeAction(event.getPermanent().getController(), +2));
        }
    }
]
