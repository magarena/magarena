def choice = new MagicTargetChoice("an artifact, creature, or land to sacrifice");

[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                this,
                "PN sacrifices an artifact, creature, or land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                player,
                choice
            ));
        }
    }
]
