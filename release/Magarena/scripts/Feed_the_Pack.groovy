def choice = new MagicTargetChoice("a nontoken creature to sacrifice");
[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                permanent,
                new MagicMayChoice(
                choice
                 ),
                MagicSacrificeTargetPicker.create(),
                this,
                "PN may\$ sacrifice a nontoken creature\$. If PN does, creates X 2/2 green Wolf creature tokens, where X is the sacrificed creature's toughness."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new SacrificeAction(it));
                    final int toughness = it.getToughness();
                    game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("2/2 green Wolf creature token"), toughness));
               });
            }
        }
    }
]
