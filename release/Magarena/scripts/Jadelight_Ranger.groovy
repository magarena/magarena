[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN explores, then SN explores again."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            2.times {
                game.addEvent(new MagicExploreEvent(event.getPermanent()));
            }
        }
    }
]

