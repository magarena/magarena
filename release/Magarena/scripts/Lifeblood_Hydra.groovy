[
    new WhenSelfDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains life and draw cards equal to SN's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getPower();
            game.logAppendValue(event.getPlayer(),amount);
            game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
            game.doAction(new DrawAction(event.getPlayer(), amount));
        }
    }
]
