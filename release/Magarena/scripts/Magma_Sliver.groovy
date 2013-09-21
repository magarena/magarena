
def MagmaPump = new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Magma"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_SLIVER, source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter, true, MagicTargetHint.Positive,
                "target Sliver");
			return new MagicEvent(
                source,
                targetChoice,
                MagicPumpTargetPicker.create(),
                this,
                "Target Sliver creature gets +X/+0 until end of turn, " +
				"where X is the number of Slivers on the battlefield\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
					final int amount =
						creature.getController().getNrOfPermanents(MagicSubType.Sliver) +
						creature.getController().getOpponent().getNrOfPermanents(MagicSubType.Sliver);
                    game.doAction(new MagicChangeTurnPTAction(creature,amount,0));
                }
            });
        }
    };

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagmaPump);
        }
    }
]
