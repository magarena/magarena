[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().controlsPermanent(MagicColor.Black)) {
                permanent.addAbility(MagicAbility.ProtectionFromBlack, flags);
            }
            if (permanent.getController().controlsPermanent(MagicColor.Blue)) {
                permanent.addAbility(MagicAbility.ProtectionFromBlue, flags);
            }
            if (permanent.getController().controlsPermanent(MagicColor.Green)) {
                permanent.addAbility(MagicAbility.ProtectionFromGreen, flags);
            }
            if (permanent.getController().controlsPermanent(MagicColor.Red)) {
                permanent.addAbility(MagicAbility.ProtectionFromRed, flags);
            }
            if (permanent.getController().controlsPermanent(MagicColor.White)) {
                permanent.addAbility(MagicAbility.ProtectionFromWhite, flags);
            }
        }
    }
]
