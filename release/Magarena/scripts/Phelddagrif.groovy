[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Flying"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{W}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "SN gains flying until end of turn. Target opponent\$ gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Flying));
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it,2));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Bounce"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getOpponent(),
                new MagicMayChoice("Draw a card?"),
                this,
                "Return SN to its owner's hand. Opponent may draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer(),1));
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Trample"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "SN gains trample until end of turn. " +
                "Target opponent\$ creates a 1/1 green Hippo creature token."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Trample));
            event.processTargetPlayer(game, {
                game.doAction(new PlayTokensAction(
                    it,
                    CardDefinitions.getToken("1/1 green Hippo creature token"),
                    1
                ));
            });
        }
    }
]
