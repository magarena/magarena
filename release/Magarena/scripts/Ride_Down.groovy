def NEG_TARGET_BLOCKING_CREATURE = MagicTargetChoice.Negative("target blocking creature");
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_BLOCKING_CREATURE,
                this,
                "Destroy target blocking creature.\$ Creatures that were blocked by RN this combat gain trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new GainAbilityAction(it.getBlockedCreature(), MagicAbility.Trample));
            });
        }
    }
]
