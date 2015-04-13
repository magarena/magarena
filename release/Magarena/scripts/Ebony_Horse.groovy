def ATTACKING_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && target.isCreature() && target.isAttacking();
    }
};

def TARGET_ATTACKING_CREATURE_YOU_CONTROL = new MagicTargetChoice(
    ATTACKING_CREATURE_YOU_CONTROL,
    MagicTargetHint.Positive,
    "target attacking creature you control"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Untap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_ATTACKING_CREATURE_YOU_CONTROL,
                MagicTapTargetPicker.Untap,
                this,
                "Untap target attacking creature you control\$. " +
                "Prevent all combat damage that would be dealt to and dealt by it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicUntapAction(it));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    MagicIfDamageWouldBeDealtTrigger.PreventCombatDamageDealtToDealtBy
                ));
            });
        }
    }
]
