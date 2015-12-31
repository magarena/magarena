[
    new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. " +
                "All Slivers have protection from chosen color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            final MagicAbility protection = color.getProtectionAbility();
            game.doAction(new AddStaticAction(
                event.getPermanent(),
                new MagicStatic(MagicLayer.Ability, SLIVER) {
                    @Override
                    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                        flags.add(protection);
                    }
                }
            ));
        }
    }
]
