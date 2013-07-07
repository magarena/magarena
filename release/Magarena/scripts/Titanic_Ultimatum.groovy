[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, creatures you control get +5/+5 and gain first strike, lifelink and trample."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,5,5));
                game.doAction(new MagicGainAbilityAction(
                    creature,
                    [
                        MagicAbility.FirstStrike,
                        MagicAbility.Lifelink,
                        MagicAbility.Trample
                    ]
                ));
            }
        }
    }
]
