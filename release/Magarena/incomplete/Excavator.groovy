[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificePermanentEvent(source, new MagicTargetChoice("a basic land to sacrifice"))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicUnblockableTargetPicker.create(),
                payedCost.getTarget(),
                this,
                "Target creature\$ gains landwalk of each of the land types of the sacrificed land until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final MagicPermanent creature ->
                final MagicPermanent sacrificed = event.getRefPermanent();
                final Collection<MagicSubType> allBasicLands = MagicSubType.ALL_BASIC_LANDS;
                for (final MagicSubType basicLand : allBasicLands) {
                    if (sacrificed.hasSubType(basicLand)) {
                        game.doAction(new MagicGainAbilityAction(creature,basicLand.getLandwalkAbility()));
                    }
                }
            });
        }
    }
]
