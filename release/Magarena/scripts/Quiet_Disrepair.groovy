def EFFECT2 = MagicRuleEventAction.create("You gain 2 life.");

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - destroy enchanted permanent; " +
                "or you gain 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.doAction(new DestroyAction(event.getPermanent().getEnchantedPermanent()));
            } else {
                event.executeModalEvent(game, EFFECT2, EFFECT2);
            }
        }
    }
]
