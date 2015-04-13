[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            new MagicEvent(
                permanent,
                this,
                "SN gets +3/+0 until end of turn for each other attacking Beast."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int power = event.getPlayer().getNrOfPermanents(ATTACKING_BEAST.except(permanent));
            game.doAction(new ChangeTurnPTAction(permanent,power*3,0));
        }
    }
]
