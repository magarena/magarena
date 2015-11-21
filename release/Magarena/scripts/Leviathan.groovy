def choice = new MagicTargetChoice("an Island to sacrifice");

[
    new AtYourUpkeepTrigger() {
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
                game.doAction(new UntapAction(event.getPermanent()));
            }
        }
    },
    new AtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.isController(attackingPlayer) ? 
                new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Islands?"),
                this,
                "PN may\$ sacrifice two Islands. If PN doesn't, SN can't attack."
            ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Island) >= 2) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice));
            } else {
                game.doAction(new GainAbilityAction(event.getPermanent(), MagicAbility.CannotAttack));
            }
        }
    }
]
