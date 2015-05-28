def TAPPED_ARTIFACT_CREATURE_AND_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return  target.isController(player) &&
                    target.isTapped() &&
                   (target.hasType(MagicType.Artifact) || target.hasType(MagicType.Creature) || target.hasType(MagicType.Land));
        }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 1 life for each tapped artifact, creature, and land he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = TAPPED_ARTIFACT_CREATURE_AND_LAND_YOU_CONTROL.filter(player).size();
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player,amount));
        }
    }
]
