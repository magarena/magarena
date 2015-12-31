[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                this,
                "Prevent the next 1 damage that would be dealt to target creature\$ and each other creature that shares a color with it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->

                final Set<MagicColor> targetColors = new HashSet<MagicColor>();
                for (final MagicColor color : MagicColor.values()) {
                    if(target.hasColor(color)) {
                        targetColors.add(color);
                    }
                }

                final Set<MagicPermanent> targetList = new HashSet<MagicPermanent>();
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    for (final MagicColor color : targetColors) {
                        if (creature.hasColor(color)) {
                            targetList.add(creature);
                            break;
                        }
                    }
                }

                for (final MagicPermanent creature : targetList) {
                    game.doAction(new PreventDamageAction(creature, 1));
                }
            });
        }
    }
]
