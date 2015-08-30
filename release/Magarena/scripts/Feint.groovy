def ATTACKING_CREATURE = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.isAttacking() && target.hasType(MagicType.Creature);
    }
};

def TARGET_ATTACKING_CREATURE = new MagicTargetChoice(
    ATTACKING_CREATURE,
    MagicTargetHint.None,
    "an attacking creature"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_ATTACKING_CREATURE,
                this,
                "Tap all creatures blocking target attacking creature.\$ Prevent all combat damage that would be dealt this turn by that creature and each creature blocking it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                permanent.getBlockingCreatures() each {
                    game.doAction(new TapAction(it));
                    game.doAction(ChangeStateAction.Set(it, MagicPermanentState.NoCombatDamage));
                }
                game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.NoCombatDamage));
            })
        }
    }
]
