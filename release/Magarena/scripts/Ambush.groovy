[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Blocking creatures gain first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_BLOCKING_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target, MagicAbility.FirstStrike));
            }
        }
    }
]
