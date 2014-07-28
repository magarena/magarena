[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{W}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target attacking or blocking creature\$. " +
                "SN doesn't untap during your next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage=new MagicDamage(event.getPermanent(),it,3);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(MagicChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
            });
        }
    }
]
