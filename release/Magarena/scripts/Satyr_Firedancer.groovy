[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent.isOpponent(damage.getTarget()) &&
                    permanent.isFriend(damage.getSource()) &&
                    damage.getSource().hasType(MagicType.Sorcery) || damage.getSource().hasType(MagicType.Instant)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to target creature\$ your opponent controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(
                    event.getSource(),
                    it,
                    event.getRefInt()
                ));
            });
        }
    }
]
