[
    new ThisBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{R}"))
                ),
                this,
                "PN may\$ pay {R}\$. If PN does, SN gets +2/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeTurnPTAction(event.getPermanent(),2,0));
            }
        }
    }
]
