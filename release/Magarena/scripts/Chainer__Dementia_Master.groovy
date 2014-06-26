[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayLifeEvent(source,3), new MagicPayManaCostEvent(source, "{B}{B}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN puts target creature card from a graveyard\$ onto the battlefield under PN's control. "+
                "That creature is black and is a Nightmare in addition to its other creature types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard target ->
                game.doAction(new MagicReanimateAction(
                    target,
                    event.getPlayer(),
                    [MagicPlayMod.BLACK, MagicPlayMod.NIGHTMARE]
                ));
            });
        }
    },
    
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all Nightmares."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final Collection<MagicPermanent> nightmares = game.filterPermanents(MagicTargetFilterFactory.NIGHTMARE_PERMANENT);
            for (final MagicPermanent permanent : nightmares) {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
            };
        }
    }
]
