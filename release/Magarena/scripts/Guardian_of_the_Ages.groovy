[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent creature) {
            return permanent.isEnemy(creature) && permanent.hasAbility(MagicAbility.Defender) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN loses defender and gains trample."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            game.doAction(new LoseAbilityAction(perm, MagicAbility.Defender, MagicStatic.Forever));
            game.doAction(new GainAbilityAction(perm, MagicAbility.Trample, MagicStatic.Forever));
        }
    }
]
