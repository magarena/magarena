def COLORLESS_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return  target.isController(player) && target.isCreature() && MagicColor.isColorless(target);
    }
}
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Life"
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
                this,
                "PN gains 1 life for each colorless creature he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = COLORLESS_CREATURE_YOU_CONTROL.filter(player).size();
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player,amount));
        }
    }
]
