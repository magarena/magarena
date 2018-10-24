def trigger = new ThisDiesTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
        return new MagicEvent(
            permanent,
            permanent.getOpponent(),
            permanent.getCard(),
            this,
            "Return SN to the battlefield under PN's control."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ReanimateAction(
            event.getRefCard(),
            event.getPlayer(),
            MagicPlayMod.BLACK_ZOMBIE
        ));
    }
}

[
   new DamageIsDealtTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isSource(permanent) && (damage.isTargetCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPermanent(),
                    this,
                    "If RN die this turn, return it to the battlefield under PN's control. "+
                    "That creature is a black Zombie in addition to its other colors and types."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
               game.doAction(new AddTurnTriggerAction(event.getRefPermanent(),trigger));
        }
    }
]
