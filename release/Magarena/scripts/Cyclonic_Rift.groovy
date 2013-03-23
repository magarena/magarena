[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_NONLAND_PERMANENT_YOU_DONT_CONTROL,
                MagicBounceTargetPicker.getInstance(),
                this,
                "Return target nonland permanent\$ you don't control to its owner's hand."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                }
            });
        }
    },
    new MagicCardActivation(
        [
            MagicConditionFactory.ManaCost("{6}{U}")
        ],
        new MagicActivationHints(MagicTiming.Tapping,true),
        "Overload"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, source.getController(), MagicManaCost.create("{6}{U}"))
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return each nonland permanent you don't control to its owner's hand."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.OwnersHand));
            }
        }
    }
]
