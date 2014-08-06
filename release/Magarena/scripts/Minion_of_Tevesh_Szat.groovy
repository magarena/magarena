[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        "Pay {B}{B}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{B}{B}"))
                    ),
                    this,
                    "PN may\$ pay {B}{B}\$. If PN doesn't, SN deals 2 damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),2)
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
