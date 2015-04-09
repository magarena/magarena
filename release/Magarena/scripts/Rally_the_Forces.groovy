[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Attacking creatures get +1/+0 and gain first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),ATTACKING_CREATURE);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.FirstStrike));
            }
        }
    }
]
