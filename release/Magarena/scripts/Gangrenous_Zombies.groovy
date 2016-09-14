def SNOW_SWAMP = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasType(MagicType.Snow) &&
                   target.hasSubType(MagicSubType.Swamp);
        }
}
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 1 damage to each creature and each player. If PN controls a snow Swamp, "+
                "SN deals 2 damage to each creature and each player instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = SNOW_SWAMP.filter(player).size() > 0 ? 2 : 1;
            game.logAppendValue(player, amount);
            CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
            PLAYER.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            }
        }
    }
]
