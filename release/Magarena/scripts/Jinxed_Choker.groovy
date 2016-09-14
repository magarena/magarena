[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ gains control of SN and puts a charge counter on it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it,event.getPermanent()));
                game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1));
            });
        }
    },

    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals damage to PN equal to the number of charge counters on it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            final MagicPlayer player = event.getPlayer();
            game.logAppendValue(player,amount);
            game.doAction(new DealDamageAction(permanent, player, amount));
        }
    },

    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Counters"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{3}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ — (1) Put a charge counter on SN; or (2) Remove a charge counter from SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.Charge, event.isMode(1) ? 1 : -1));
        }
    }
]
