[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN creates a token that's a copy of target creature card in his or her graveyard\$, "+
                "except it's an artifact in addition to its other types. It gains haste. Sacrifice it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    it,
                    [MagicPlayMod.ARTIFACT, MagicPlayMod.HASTE_UEOT, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                ));
            });
        }
    }
]
