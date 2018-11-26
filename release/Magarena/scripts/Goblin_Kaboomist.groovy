def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new DealDamageAction(event.getPermanent(), event.getPermanent(), 2));
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a colorless artifact token named Land Mine with \"{R}, Sacrifice this artifact: This artifact deals 2 damage to target attacking creature without flying.\""+
                "Then flip a coin. If PN loses the flip, SN deals 2 damage to itself."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("colorless artifact token named Land Mine"),1));
            game.addEvent(new MagicCoinFlipEvent(
                event,
                MagicEventAction.NONE,
                loseAct
            ));
        }
    }
]
