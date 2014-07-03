[
    new MagicChannelActivation("{3}{R}", new MagicActivationHints(MagicTiming.Removal, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final MagicPermanent creature ->
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,4);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
