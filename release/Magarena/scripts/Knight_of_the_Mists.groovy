def EFFECT = MagicRuleEventAction.create("Destroy target Knight. It can't be regenerated.");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {U}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{U}"))
                ),
                this,
                "PN may\$ pay {U}\$. If PN doesn't, destroy target Knight and it can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.addEvent(EFFECT.getEvent(event.getSource()));
            }
        }
    }
]
