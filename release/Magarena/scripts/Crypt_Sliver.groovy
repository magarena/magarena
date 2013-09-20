
def CryptRegen = new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Crypt"
    ) {

         @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_SLIVER, source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter, true, MagicTargetHint.Positive,
                "target Sliver");
            return new MagicEvent(
                source,
                targetChoice,
                MagicRegenerateTargetPicker.getInstance(),
                this,
                "Regenerate target Sliver\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,
                new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
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
            permanent.addAbility(CryptRegen);
        }
    }
]
