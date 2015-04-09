[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(
                    "Pay {1}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                ),
                this,
                "PN may\$ pay {1}\$. If PN doesn't, PN sacrifices a permanent."
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
