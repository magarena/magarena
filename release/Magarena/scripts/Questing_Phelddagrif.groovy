[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
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
                "SN gets +1/+1 until end of turn. " +
                "Target opponent\$ puts a 1/1 green Hippo creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), 1, 1));
            event.processTargetPlayer(game, {
                game.doAction(new PlayTokensAction(
                    it,
                    CardDefinitions.getToken("1/1 green Hippo creature token"),
                    1
                ));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Protection"
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
                "SN gains protection from black and from red until end of turn. Target opponent\$ gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.ProtectionFromBlack));
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.ProtectionFromRed));
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it,2));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Flying"
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
                "SN gains flying until end of turn. Opponent may draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Flying));
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer(),1));
            }
        }
    }
]
