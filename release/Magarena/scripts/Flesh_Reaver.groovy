[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent == damage.getSource() && damage.isTargetCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
             game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),event.getRefInt()));
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent == damage.getSource() && permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
             game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),event.getRefInt()));
        }
    }
]
