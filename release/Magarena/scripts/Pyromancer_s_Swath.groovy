[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isFriend(permanent) &&
                damage.getSource().isInstantOrSorcerySpell()) {
            damage.setAmount(damage.getAmount() + 2);
            return MagicEvent.NONE;
            }
        }
    },
   new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            new MagicEvent(
                permanent,
                player,
                this,
                "PN discards his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
                game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),amount));
        }
    }
]
