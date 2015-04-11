[
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source,"{2}{B}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the battlefield. "+
                "It gains haste. Exile it at the beginning of the next end step. "+
                "If it would leave the battlefield, exile it instead of putting it anywhere else."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MagicPlayCardAction(
                    it,
                    event.getPlayer(),
                    [MagicPlayMod.HASTE, MagicPlayMod.EXILE_AT_END_OF_TURN, MagicPlayMod.EXILE_WHEN_LEAVES]
                ));
            });
        }
    }
]
