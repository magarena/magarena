def choice = new MagicTargetChoice("an Island to sacrifice");

def ST = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.CannotAttack);
    }
};

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Islands?"),
                this,
                "PN may\$ sacrifice two Island. If PN does, untap SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Island) >= 2) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
                game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        }
    },
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.isController(attackingPlayer) ? 
                new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Islands?"),
                this,
                "SN can't attack unless PN sacrifices two Islands."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Island) >= 2) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
            } else {
                game.doAction(new MagicAddStaticAction(event.getPermanent(), ST));
            }
        }
    }
]
