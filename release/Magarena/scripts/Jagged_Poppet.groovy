[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return damage.getTarget() == permanent ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN discards ${amount} cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer(), event.getRefInt()));
        }
    },
    
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return MagicCondition.HELLBENT.accept(permanent) ?
                new MagicEvent (
                permanent,
                damage.getTargetPlayer(),
                amount,
                this,
                "PN discards ${amount} cards."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer(), event.getRefInt()));
        }
    }
]
