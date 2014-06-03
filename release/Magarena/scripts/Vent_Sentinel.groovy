[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{1}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage to target player\$ equal to the number of creatures with defender PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer target ->
                final int amount = event.getPlayer().getNrOfPermanents(MagicTargetFilterFactory.CREATURE_WITH_DEFENDER_YOU_CONTROL);
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    target,
                    amount
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
            });
        }
    }
]
