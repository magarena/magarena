def Action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicDealDamageAction(event.getSource(),event.getRefPermanent(),2));
}

def Event = {
    final MagicPermanent permanent, final MagicPermanent creature ->
    return new MagicEvent(
        permanent,
        creature,
        Action,
        "SN deals 2 damage to RN."
    );
}

[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent, creature);
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent, creature);
        }
    }
]
