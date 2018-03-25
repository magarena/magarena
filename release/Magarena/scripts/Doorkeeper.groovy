def CREATURE_WITH_DEFENDER_YOU_CONTROL = creature(MagicAbility.Defender, Control.You);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
               new MagicTapEvent(source),
               new MagicPayManaCostEvent(source, "{2}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top X cards from his or her library into his or her graveyard, "+
                "where X is the number of creatures with defender PN control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer()
                final int amount = event.getPlayer().getNrOfPermanents(CREATURE_WITH_DEFENDER_YOU_CONTROL);
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new MillLibraryAction(it, amount));
            });
        }
    }
]
