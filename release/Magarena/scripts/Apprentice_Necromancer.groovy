[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Reanimate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{B}"),
                new MagicSacrificeEvent(source)
            ];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card from your graveyard\$ to the battlefield. "+
                "That creature gains haste. At the beginning of the next end step, sacrifice it."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicReanimateAction(
                    it,
                    event.getPlayer(),
                    [MagicPlayMod.HASTE, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                ));
            });
        }
    }
]
