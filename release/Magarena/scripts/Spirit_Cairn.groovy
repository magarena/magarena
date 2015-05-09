[
    new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent,final MagicCard card) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {W}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{W}"))
                ),
                this,
                "PN may\$ pay {W}. If PN does, put a 1/1 white Spirit creature token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 white Spirit creature token with flying")
                ));
            }
        }
    }
]
