def trigger = new ThisDiesTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
        return new MagicEvent(
            permanent,
            permanent.getOpponent(),
            new MagicMayChoice("Draw a card?"),
            this,
            "PN may\$ draw a card."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new DrawAction(event.getPlayer()));
        }
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
                    "If RN die this turn, PN may draw a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
               game.doAction(new AddTurnTriggerAction(event.getRefPermanent(),trigger));
        }
    }
]
