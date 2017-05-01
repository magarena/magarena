[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "PN gains control of RN. Untap that permanent. It gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            game.doAction(new GainControlAction(event.getPlayer(),permanent,MagicStatic.UntilEOT));
            game.doAction(new UntapAction(permanent));
            game.doAction(new GainAbilityAction(permanent, MagicAbility.Haste));
        }
    },
    
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "PN gains control of RN. Untap that permanent. It gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            game.doAction(new GainControlAction(event.getPlayer(),permanent,MagicStatic.UntilEOT));
            game.doAction(new UntapAction(permanent));
            game.doAction(new GainAbilityAction(permanent, MagicAbility.Haste));
        }
    }
]
