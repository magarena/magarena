[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "SN deals 2 damage to RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getRefPermanent(),
                2
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
