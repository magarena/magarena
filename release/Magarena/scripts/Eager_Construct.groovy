def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicScryEvent(event.getSource(), event.getPlayer()));
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                ""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getPlayers().each({
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    (MagicPlayer)it,
                    new MagicMayChoice("Scry 1?"),
                    action,
                    "PN may\$ scry 1."
                ));
            });
        }
    }
]

