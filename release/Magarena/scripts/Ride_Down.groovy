def NEG_TARGET_BLOCKING_CREATURE = MagicTargetChoice.Negative("target blocking creature");
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_BLOCKING_CREATURE,
                this,
                "Destroy target blocking creature\$. " +
                "Creatures that were blocked by that creature this combat gain trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent blocked = it.getBlockedCreature();
                game.doAction(new DestroyAction(it));
                game.doAction(new GainAbilityAction(blocked, MagicAbility.Trample));
            });
        }
    }
]
