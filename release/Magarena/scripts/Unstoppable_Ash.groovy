[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPlayer player = permanent.getController();
            return (player == attacker.getController()) ?
                new MagicEvent(
                    permanent,
                    player,
                    attacker,
                    this,
                    "RN gets +0/+5 until end of turn."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(
                event.getRefPermanent(),
                0,
                5
            ));
        }
    }
]
