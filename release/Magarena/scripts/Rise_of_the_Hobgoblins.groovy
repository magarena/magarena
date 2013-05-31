[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{R/W}")],
        new MagicActivationHints(MagicTiming.Block,true,1),
        "First strike"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R/W}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Red creatures and white creatures you control gain first strike until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicPermanent> targets=game.filterPermanents(
                    player,
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                if (creature.hasColor(MagicColor.Red) ||
                    creature.hasColor(MagicColor.White)) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
                }
            }
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}"))
                ),
                this,
                "You may pay\$ {X}\$. If you do, put X 1/1 red and white Goblin Soldier creature tokens onto the battlefield."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player=event.getPlayer();
                final MagicPayManaCostResult payedManaCost = event.getPaidMana();
                game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("Goblin Soldier"),
                    payManaCost.getX()
                ));
            }
        }
    }
]
