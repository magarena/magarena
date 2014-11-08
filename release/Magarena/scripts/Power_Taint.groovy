[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedEnchantment = permanent.getEnchantedPermanent();
            return (enchantedEnchantment.isEnchantment() && enchantedEnchantment.isController(upkeepPlayer)) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(
                        "Pay {2}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                    ),
                    this,
                    "PN may\$ pay {2}\$. If PN doesn't, PN loses 2 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
