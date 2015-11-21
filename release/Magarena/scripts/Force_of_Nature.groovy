[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {G}{G}{G}{G}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{G}{G}{G}{G}"))
                ),
                this,
                "PN may\$ pay {G}{G}{G}{G}\$. If PN doesn't, SN deals 8 damage to PN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),8));
            }
        }
    }
]
