[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "Token") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Charge,
                    1
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 1/1 white Soldier creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("Soldier")
            ));
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "Boost") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Charge,
                    1
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +3/+3 and gains flying until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,3));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
                }
            });
        
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicCondition.EIGHT_CHARGE_COUNTERS_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "Ultimate") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    8
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Artifacts, creatures, enchantments, and lands you control are indestructible.\""
            );
        }
        @Override
        public void executeEvent(
                final MagicGame outerGame,
                final MagicEvent event,
                final Object[] choiceResults) {
            outerGame.doAction(new MagicAddStaticAction(
                new MagicStatic(
                    MagicLayer.Ability,
                    MagicTargetFilter.ANY) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        flags.add(MagicAbility.Indestructible);
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return target.getController().getId() == event.getPlayer().getId() && (
                            target.isArtifact() ||
                            target.isCreature() ||
                            target.isEnchantment() ||
                            target.isLand()
                        );
                    }
                }
            ));
        }
    }
]
