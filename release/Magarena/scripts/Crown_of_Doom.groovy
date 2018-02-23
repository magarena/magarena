def filter = new MagicPlayerFilterImpl() {
    @Override
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
        return target != ((MagicPermanent)source).getOwner();
    }
};

[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_TURN_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Control"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice(filter, "target player other than " + source + "'s owner"),
                this,
                "Target player other than SN's owner\$ gains control of SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it, event.getPermanent()));
            })
        }
    }
]
