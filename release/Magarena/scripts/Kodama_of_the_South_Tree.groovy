[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                this,
                "Each other creature PN controls gets +1/+1 and gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                event.getPermanent()
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target, 1, 1));
                game.doAction(new MagicGainAbilityAction(target, MagicAbility.Trample));
            }
        }
    }
]
