[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) && permanent.isUntapped() ?
                new MagicEvent(
                    permanent,
                    this,
                    "If SN is untapped, destroy SN and it deals damage to PN equal to the number of pin counters on it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = permanent.getController();
            final int amount = permanent.getCounters(MagicCounterType.Pin);
            game.logAppendValue(player, amount);
            if (permanent.isUntapped()) {
                game.doAction(new DestroyAction(permanent));
                if (amount > 0) {
                    game.doAction(new DealDamageAction(permanent, player, amount));
                }
            }
        }
    },
    
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final int amount = source.getCounters(MagicCounterType.Pin);
            return [
                    new MagicTapEvent(source),
                    new MagicPayManaCostEvent(source,"{"+amount+"}{"+amount+"}")
                ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Pin);
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage equal to the number of pin counters on it to target creature or player.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent permanent = event.getPermanent();
                final int amount = permanent.getCounters(MagicCounterType.Pin);
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new DealDamageAction(permanent, it, amount));
            });
        }
    }
]
