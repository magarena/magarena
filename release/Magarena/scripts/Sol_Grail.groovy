[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            final MagicAbilityList ab = new MagicAbilityList();
            ab.add(new MagicTapManaActivation(Collections.singletonList(color.getManaType())));
            game.doAction(new GainAbilityAction(event.getPermanent(), ab, MagicStatic.Forever));
        }
    }
]
