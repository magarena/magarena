[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                CREATURE_YOU_CONTROL.except(source),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source, "{R}"),
                new MagicSacrificePermanentEvent(source,targetChoice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to the power of RN to target player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent sacrificed=event.getRefPermanent();
                game.doAction(new DealDamageAction(event.getSource(),it,sacrificed.getPower()));
            });
        }
    }
]
