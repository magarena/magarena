[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {B}{B}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{B}{B}"))
                ),
                this,
                "PN may\$ pay {B}{B}\$. If PN doesn't, SN deals 2 damage to PN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
            }
        }
    }
]
