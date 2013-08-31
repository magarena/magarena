[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main,true),
        "Add counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{1}"),
                new MagicDiscardEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a study counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put all creature cards from all graveyards onto the " +
                "battlefield under your control. They're black Zombies " +
                "in addition to their other colors and types."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicCard> targets = game.filterCards(
                player,
                MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS
            );
            for (final MagicCard card : targets) {
                game.doAction(new MagicReanimateAction(
                    card, 
                    player, 
                    [MagicPlayMod.BLACK, MagicPlayMod.ZOMBIE]
                ));
            }
        }
    }
]
