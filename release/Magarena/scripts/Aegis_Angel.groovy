[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                PosOther("target permanent",permanent),
                MagicIndestructibleTargetPicker.create(),
                this,
                "Another target permanent\$ gains indestructible for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.AsLongAsCond(
                        it, 
                        MagicAbility.Indestructible, 
                        MagicConditionFactory.PlayerControlsSource(event.getPlayer())
                    )
                ));
            });
        }
    }
]
