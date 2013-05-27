[
    new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_SOLDIER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Vigilance);
        }
    },
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{2}{W}")],
        new MagicActivationHints(MagicTiming.Token,true),
        "Token"
    ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{W}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 1/1 white Soldier creature token onto the battlefield."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Soldier")));
        }
    }
]
