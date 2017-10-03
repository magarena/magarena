package magic.model.phase;

import magic.model.MagicGame;

public class MagicDefaultGameplay implements MagicGameplay {

    private static final MagicGameplay INSTANCE=new MagicDefaultGameplay();

    private MagicDefaultGameplay() {}

    @Override
    public MagicPhase getStartPhase(final MagicGame game) {
        return MagicMulliganPhase.getInstance();
    }

    @Override
    public MagicPhase getNextPhase(final MagicGame game) {
        switch (game.getPhase().getType()) {
            case Mulligan:
                return MagicUntapPhase.getInstance();
            case Untap:
                return MagicUpkeepPhase.getInstance();
            case Upkeep:
                return MagicDrawPhase.getInstance();
            case Draw:
                return MagicMainPhase.getFirstInstance();
            case FirstMain:
                return game.canSkip() ?
                    MagicDeclareAttackersPhase.getInstance() :
                    MagicBeginOfCombatPhase.getInstance();
            case BeginOfCombat:
                return MagicDeclareAttackersPhase.getInstance();
            case DeclareAttackers:
                return game.getTurnPlayer().getNrOfAttackers() > 0 ?
                    MagicDeclareBlockersPhase.getInstance() :
                    MagicEndOfCombatPhase.getInstance();
            case DeclareBlockers:
                return MagicCombatDamagePhase.First;
            case CombatDamage:
                return game.getPhase() == MagicCombatDamagePhase.First ?
                    MagicCombatDamagePhase.Second :
                    MagicEndOfCombatPhase.getInstance();
            case EndOfCombat:
                return MagicMainPhase.getSecondInstance();
            case SecondMain:
                return MagicEndOfTurnPhase.getInstance();
            case EndOfTurn:
                return MagicCleanupPhase.getInstance();
            case Cleanup:
                return MagicUntapPhase.getInstance();
            default:
                throw new IllegalStateException("Illegal gameflow.");
        }
    }

    public static MagicGameplay getInstance() {
        return INSTANCE;
    }
}
