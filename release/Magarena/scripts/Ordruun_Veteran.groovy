[
    new MagicBattalionTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN gains double strike until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSetAbilityAction(event.getPermanent(),MagicAbility.DoubleStrike));
        }
    }
]
