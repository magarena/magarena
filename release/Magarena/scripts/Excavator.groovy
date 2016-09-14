def choice = new MagicTargetChoice("a basic land to sacrifice");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "Landwalk"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificePermanentEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicUnblockableTargetPicker.create(),
                payedCost.getTarget(),
                this,
                "Target creature\$ gains landwalk of each of the land types of the sacrificed land until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent sacLand = event.getRefPermanent();
                for (final MagicSubType subType : MagicSubType.ALL_BASIC_LANDS) {
                    if (sacLand.hasSubType(subType)) {
                        game.doAction(new GainAbilityAction(it,subType.getLandwalkAbility()));
                    }
                }
            });
        }
    }
]
