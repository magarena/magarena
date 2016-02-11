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
                "RN loses 2 life and PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getRefPlayer(),-2));
            game.doAction(new ChangeLifeAction(event.getPlayer(), +2));
        }
    }
]
