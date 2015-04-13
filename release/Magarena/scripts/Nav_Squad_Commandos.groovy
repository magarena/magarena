[
    new MagicBattalionTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +1/+1 until end of turn. Untap it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),1,1));
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }
    }
]
