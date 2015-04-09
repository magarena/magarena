[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN controls get +1/+0 and gain fear until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Fear));
            }
        }
    }
]
