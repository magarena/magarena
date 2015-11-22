[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN gains life equal to RN's toughness."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            // get toughness here so counters on the creature are considered
            final int toughness = (event.getRefPermanent()).getToughness();
            game.doAction(new ChangeLifeAction(event.getPlayer(),toughness));
        }
    }
]
