[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            final int n = 2 * source.getController().getDomain();
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getGameCost().reduce(n)
                )
            ];
        }
    },
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int n = 2 * permanent.getController().getDomain();
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay SN's upkeep cost?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{10}").reduce(n))
                ),
                this,
                "PN may\$ pay SN's upkeep cost. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
