[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile target creature an opponent controls\$. Its controller gains life equal to its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                    game.doAction(new ChangeLifeAction(
                        it.getController(),
                        it.getPower()
                    ));
                });
            }
        }
    }
]

