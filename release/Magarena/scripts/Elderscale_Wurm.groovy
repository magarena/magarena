[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "When Elderscale Wurm enters the battlefield, if your life total is less than 7, your life total becomes 7."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getplayer.getLife() < 7) {
                event.getplayer.setLife(7);
            }
            return MagicEvent.NONE;
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = damage.getTarget();
            if (player == target && player.getLife() < 7) {
                player.setLife(7);
            }
            return MagicEvent.NONE;
        }
    }
]
