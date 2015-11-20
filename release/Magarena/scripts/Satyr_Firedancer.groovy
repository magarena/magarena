[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().isInstantOrSorcerySpell() &&
                    permanent.isFriend(damage.getSource()) &&
                    permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to target creature an opponent controls.\$"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(
                    event.getSource(),
                    it,
                    event.getRefInt()
                ));
            });
        }
    }
]
