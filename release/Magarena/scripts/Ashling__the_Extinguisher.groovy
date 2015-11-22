def choice = new MagicTargetChoice("target creature defending player controls");

[
    new SelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                choice,
                MagicSacrificeTargetPicker.create(),
                this,
                "Target creature's\$ controller sacrifices it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new SacrificeAction(it));
            });
        }
    }
]
