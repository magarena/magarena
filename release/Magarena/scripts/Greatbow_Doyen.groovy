[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (source.isPermanent() &&
                    source.hasSubType(MagicSubType.Archer) &&
                    source.isFriend(permanent) &&
                    damage.isTargetCreature()) ?
                new MagicEvent(
                    source,
                    damage.getTarget().getController(),
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),event.getRefInt()));
        }
    }
]
