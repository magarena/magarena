[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicSource source = damage.getSource();
            return (!source.isCreaturePermanent() && permanent.isFriend(source)) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN gains RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),event.getRefInt()));
        }
    }
]
