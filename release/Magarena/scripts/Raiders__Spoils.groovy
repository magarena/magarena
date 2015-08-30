[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (damage.getTarget().isPlayer() &&
                    damage.isCombat() &&
                    permanent.isFriend(source) &&
                    source.hasSubType(MagicSubType.Warrior)) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice("Pay 1 life?"),
                    this,
                    "PN may\$ pay 1 life. If PN does, PN draws a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player = event.getPlayer();
                game.addEvent(new MagicPayLifeEvent(event.getSource(), player, 1));
                game.doAction(new DrawAction(player));
            }
        }
    }
]
