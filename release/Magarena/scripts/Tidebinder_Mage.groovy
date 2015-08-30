def choice = new MagicTargetChoice("target red or green creature an opponent controls");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "Tap target red or green creature an opponent controls\$. " +
                "It doesn't untap during its controller's untap step as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                game.doAction(new TapAction(it));
                game.doAction(new AddStaticAction(
                    source, 
                    MagicStatic.AsLongAsCond(
                        it, 
                        MagicAbility.DoesNotUntap, 
                        MagicConditionFactory.PlayerControlsSource(event.getPlayer()
                    ))
                ));
            });
        }
    }
]
