[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent) &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "Target creature's\$ controller sacrifices it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new SacrificeAction(it));
            });
        }
    }
]
