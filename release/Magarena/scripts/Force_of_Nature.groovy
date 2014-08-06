[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        "Pay {G}{G}{G}{G}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{G}{G}{G}{G}"))
                    ),
                    this,
                    "PN may\$ pay {G}{G}{G}{G}\$. If PN doesn't, SN deals 8 damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),8)
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
