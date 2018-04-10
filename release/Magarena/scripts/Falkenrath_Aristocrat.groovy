[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    SACRIFICE_CREATURE
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final boolean isHuman = payedCost.getTarget().hasSubType(MagicSubType.Human);
            final String message =
                "SN gains indestructible until end of turn." +
                (isHuman ? " Put a +1/+1 counter on SN." : "");
            return new MagicEvent(
                source,
                isHuman ? 1 : 0,
                this,
                message
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(
                event.getPermanent(),
                MagicAbility.Indestructible
            ));
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,event.getRefInt()));
        }
    }
]
