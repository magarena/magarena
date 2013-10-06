[
    new MagicStatic(
        MagicLayer.SetPT,
        MagicTargetFilter.TARGET_FOREST_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    },
    new MagicStatic(
        MagicLayer.Color,
        MagicTargetFilter.TARGET_FOREST_YOU_CONTROL
    ) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return flags|MagicColor.Green.getMask();
        }
    },
    new MagicStatic(
        MagicLayer.Type,
        MagicTargetFilter.TARGET_FOREST_YOU_CONTROL
    ) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Elf);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{G}"),
                new MagicSacrificePermanentEvent(
                    source, 
                    new MagicTargetChoice("an Elf you control")
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +3/+3 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,3));
                }
            });
        }
    }
]
