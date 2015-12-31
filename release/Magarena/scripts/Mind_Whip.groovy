[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return enchantedPermanent.getController() == upkeepPlayer ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(
                        "Pay {3}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{3}"))
                    ),
                    enchantedPermanent,
                    this,
                    "PN may\$ pay {3}. If he or she doesn't, SN deals 2 damage to PN and "+
                    "${permanent.getController().getName()} taps ${enchantedPermanent.getName()}."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new DealDamageAction(event.getPermanent(), event.getPlayer(), 2));
                game.doAction(new TapAction(event.getRefPermanent()));
            }
        }
    }
]
