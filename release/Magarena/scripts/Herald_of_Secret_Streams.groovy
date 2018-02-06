[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            creature(
                MagicCounterType.PlusOne,
                Control.You
            ).filter(source).each {
                it.addAbility(MagicAbility.Unblockable);
            }
        }
    }
]

