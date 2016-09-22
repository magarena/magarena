[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            final int cost = enchanted.getConvertedCost();
            return enchanted.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(
                        "Pay {"+cost+"}?",
                        new MagicPayManaCostChoice(MagicManaCost.create(cost))
                    ),
                    enchanted,
                    this,
                    "PN may\$ pay {"+cost+"}\$. If PN doesn't, sacrifice RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new SacrificeAction(event.getRefPermanent()));
            }
        }
    }
]
