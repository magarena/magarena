def creaturePowerGEQLife = new MagicPermanentFilterImpl() {
    @Override
    public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
        return target.hasType(MagicType.Creature) &&
            target.getPower() >= player.getLife();
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice(
                    creaturePowerGEQLife,
                    MagicTargetHint.Negative,
                    "target creature with power greater than or equal to your life total"
                ),
                this,
                "Exile target creature with power greater than or equal to PN's life total\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
            });
        }
    }
]

