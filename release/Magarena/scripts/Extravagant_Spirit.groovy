[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final String costStr = "{${upkeepPlayer.getHandSize()}}";
            final MagicManaCost cost = MagicManaCost.create(costStr);
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {${costStr}}?",
                    new MagicPayManaCostChoice(cost)
                ),
                this,
                "PN may\$ pay {1} for each card in his or her hand. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
