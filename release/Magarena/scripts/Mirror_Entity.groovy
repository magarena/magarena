[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true,1),
        "X/X"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}"),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                source,
                x,
                this,
                "Creatures PN controls become RN/RN and gain all creature types until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            final MagicPermanent permanent = event.getPermanent();
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(
                        final MagicPermanent S,
                        final MagicPermanent P,
                        final MagicPowerToughness pt) {
                    pt.set(X,X);
                }
            };
            final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
                @Override
                public void modSubTypeFlags(
                        final MagicPermanent P,
                        final Set<MagicSubType> flags) {
                    flags.addAll(MagicSubType.ALL_CREATURES);
                }
            };
            final Collection<MagicPermanent> creatures=game.filterPermanents(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
            }
        }
    }
]
