[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                payedCost.getKicker(),
                this,
                payedCost.isKicked() ?
                    "Destroy all lands." :
                    "Destroy all lands you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 1) {
                game.addEvent(MagicRuleEventAction.create("Destroy all lands.").getEvent(event.getSource()));
            } else {
                game.addEvent(MagicRuleEventAction.create("Destroy all lands you control.").getEvent(event.getSource()));
            }
        }
    }
]
