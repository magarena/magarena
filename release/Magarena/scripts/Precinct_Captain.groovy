[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 white Soldier creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Soldier")
            ));
        }
    }
]
