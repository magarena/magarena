[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature\$ for as long as SN remains on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsSourceIsOnBattlefield(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
