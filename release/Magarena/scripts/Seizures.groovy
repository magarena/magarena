[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted==tapped ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    new MagicMayChoice(
                        "Pay {3}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{3}"))
                    ),
                    this,
                    "PN may\$ pay {3}\$. If PN doesn't, SN deals 3 damage to him or her."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),3));
            }
        }
    }
]
