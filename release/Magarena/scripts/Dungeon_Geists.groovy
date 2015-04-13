[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                this,
                "Tap target creature opponent controls\$. " +
                "It doesn't untap during its controller's untap step as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                game.doAction(new MagicTapAction(it));
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.AsLongAsCond(
                        it, 
                        MagicAbility.DoesNotUntap, 
                        MagicConditionFactory.PlayerControlsSource(event.getPlayer())
                    )
                ));
            });
        }
    }
]
