[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isSource(permanent) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "Whenever SN deals damage, you lose that much life."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-event.getRefInt()));
        }
    }
]
