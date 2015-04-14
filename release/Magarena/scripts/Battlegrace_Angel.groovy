[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.isFriend(creature) &&
                    permanent.getController().getNrOfAttackers() == 1) ?
                new MagicEvent(
                    permanent,
                    creature,
                    this,
                    "RN gains lifelink until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new GainAbilityAction(
                    it,
                    MagicAbility.Lifelink
                ));
            });
        }
    }
]
