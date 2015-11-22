[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(
                    "Pay {3}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{3}"))
                ),
                this,
                "PN may\$ pay {3}\$. If PN doesn't, PN sacrifices a permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(), 
                    event.getPlayer(),
                    SACRIFICE_PERMANENT
                ));
            }
        }
    }
]
