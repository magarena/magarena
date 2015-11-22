[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent.getEnchantedPermanent() &&
                    damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "RN discards two cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getRefPlayer(),2));
        }
    }
]
