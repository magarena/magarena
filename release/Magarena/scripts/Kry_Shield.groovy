def ATTACKING_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
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
        "Pump"
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
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Prevent all damage that would be dealt this turn by target creature PN controls.\$ "+
                "That creature gets +0/+X until end of turn, where X is its converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                final int amount = permanent.getConvertedCost();
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new AddTurnTriggerAction(
                    permanent,
                    MagicPreventDamageTrigger.PreventDamageDealtBy
                ));
                game.doAction(new ChangeTurnPTAction(permanent, 0, amount));
            });
        }
    }
]
