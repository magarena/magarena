[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return eotPlayer.getLife()<=5 ? new MagicEvent(
                permanent,
                eotPlayer,
                this,
                "SN deals 2 damage to PN."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), 2));
        }
    }
]
