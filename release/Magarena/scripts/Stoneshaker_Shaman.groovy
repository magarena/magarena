def choice = new MagicTargetChoice("an untapped land to sacrifice");

[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer turnPlayer) {
            return new MagicEvent(
                permanent,
                turnPlayer,
                this,
                "PN sacrifices an untapped land."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                choice
            ));
        }
    }
]
