package magic.model;

public enum MagicCounterType implements MagicCopyable {
// scores: (All assume owner and controller of permanent with counters on it)
// 0=Ignore (for counters that are used positively and negatively, or counters that mark a rule change for that card while present)
// 1=Count (counters that are only used positively (eg. Counting a spendable resource, negative impact if decreased to certain #, positive impact if increased to certain #))
// -1=Count (counters that are only used negatively (eg. positive impact if decreased to certain #, negative impact if increased to certain #))
// Stat adjusting counters use a value equal to the sum of the pt change

    PlusZeroPlusOne("+0/+1","{0+}",1),
    PlusZeroPlusTwo("+0/+2","{0+2}",2),
    PlusOnePlusZero("+1/+0","{+0}",1),
    PlusOne("+1/+1","{++}",2),
    PlusOnePlusTwo("+1/+2","{++2}",3),
    PlusTwoPlusZero("+2/+0","{+2+0}",2),
    PlusTwo("+2/+2","{+2+2}",4),
    MinusZeroMinusOne("-0/-1","{0-}",-1),
    MinusZeroMinusTwo("-0/-2","{0-2}",-2),
    MinusOneMinusZero("-1/-0","{-0}",-1),
    MinusOne("-1/-1","{--}",-2),
    MinusTwoMinusOne("-2/-1","{-2-}",-3),
    MinusTwo("-2/-2","{-2-2}",-4),

    Age("age","{age}",0),
    Aim("aim","{aim}",1),
    Arrow("arrow","{arw}",1),
    Arrowhead("arrowhead","{arh}",1),
    Awakening("awakening","{awa}",1),
    Blaze("blaze","{blz}",0),
    Blood("blood","{bld}",0),
    Bounty("bounty","{bty}",-1),
    Bribery("bribery","{brb}",-1),
    Brick("brick","{brk}", 1),
    Carrion("carrion","{crn}",1),
    Charge("charge","{chg}",1),
    Corpse("corpse","{cor}",1),
    Credit("credit","{crd}",1),
    Crystal("crystal","{cry}",1),
    Cube("cube","{cbe}",1),
    Currency("currency","{cur}",1),
    Death("death","{dth}",-1),
    Delay("delay","{del}",-1),
    Depletion("depletion","{dep}",0),
    Despair("despair","{des}",-1), //Possibly 0
    Devotion("devotion","{dev}",1),
    Divinity("divinity","{div}",1),
    Doom("doom","{dom}",-1),
    Dream("dream","{drm}",1),
    Echo("echo","{ech}",1),
    Elixir("elixir","{elx}",1),
    Energy("energy","{egy}",1),
    Eon("eon","{eon}",1),
    Experience("experience","{exp}",1),
    Eyeball("eyeball","{eye}",1),
    Fade("fade","{fde}",1),
    Fate("fate","{fte}",-1),
    Feather("feather","{fea}",1),
    Filibuster("filibuster","{fil}",1), //Possibly more
    Flood("flood","{fld}",0), //Possibly -1
    Fungus("fungus","{fun}",-1),
    Fuse("fuse","{fse}",0), //Possibly -1 or 1
    Fury("fury","{fry}",1),
    Gem("gem","{gem}",0),
    Glyph("glyph","{gly}",-1),
    Gold("gold","{gld}",0),
    Growth("growth","{grw}",0),
    Hatchling("hatchling","{hch}",1),
    Healing("healing","{hea}",1),
    Hoofprint("hoofprint","{hpr}",1),
    Hour("hour", "{hou}", 1),
    Hourglass("hourglass","{hgl}",0),
    Hunger("hunger","{hgr}",-1),
    Ice("ice","{ice}",0),
    Infection("infection","{inf}",1),
    Intervention("intervention","{int}",0), //Possibly -1
    Isolation("isolation","{iso}",1),
    Javelin("javelin","{jav}",1),
    Ki("ki","{ki}",1),
    Level("level","{lvl}",0),
    Lore("lore","{lre}",1),
    Loyalty("loyalty","{lyl}",1),
    Luck("luck","{luc}",1),
    Magnet("magnet","{mag}",0),
    Manifestation("manifestation","{mft}",0),
    Mannequin("mannequin","{mqn}",0),
    Matrix("matrix","{mtx}",1),
    Mine("mine","{mne}",0),
    Mining("mining","{mng}",1),
    Mire("mire","{mre}",0),
    Music("music","{mus}",-1),
    Muster("muster","{mtr}",1),
    Net("net","{net}",0),
    Omen("omen","{omn}",0), //Possibly -1
    Ore("ore","{ore}",-1),
    Page("page","{pge}",1),
    Pain("pain","{pan}",-1), //Possibly 1
    Paralyzation("paralyzation","{pzn}",-1), //Possibly 0
    Petal("petal","{ptl}",1),
    Petrification("petrification","{pet}",0),
    Phylactery("phylactery","{phy}",0),
    Pin("pin","{pin}",-1), //Possibly 0
    Plague("plague","{plg}",0),
    Poison("poison","{psn}",0),
    Polyp("polyp","{pyp}",1),
    Pressure("pressure","{pss}",1),
    Pupa("pupa","{pup}",0),
    Quest("quest","{que}",1),
    Rust("rust","{rst}",-1),
    Scream("scream","{srm}",-1),
    Shell("shell","{shl}",-1),
    Shield("shield","{shd}",1),
    Shred("shred","{shr}",-1), //Possibly 0
    Sleep("sleep","{slp}",-1), //Possibly 0
    Sleight("sleight","{slt}",-1), //Possibly 0
    Slime("slime","{slm}",1),
    Soot("soot","{sot}",-1), //Possibly 0
    Spore("spore","{spr}",1),
    Spite("spite","{spt}",1),
    Storage("storage","{stg}",1),
    Strife("strife","{stf}",1),
    Study("study","{stu}",1),
    Theft("theft","{thf}",1),
    Tide("tide","{tde}",0),
    Time("time","{tme}",1), //Possibly 0
    Tower("tower","{twr}",1),
    Training("training","{tng}",0),
    Trap("trap","{trp}",0), //Possibly 1
    Treasure("treasure","{tre}",1),
    Unity("unity", "{uty}",1),
    Velocity("velocity","{vly}",0), //Possibly -1
    Verse("verse","{vrs}",1),
    Vitality("vitality","{vit}",1),
    Vortex("vortex","{vtx}",1),
    Wage("wage","{wge}",0), //Possibly -1
    Winch("winch","{wch}",1),
    Wind("wind","{wnd}",0),
    Wish("wish","{wsh}",1),
    None("","",0);

    public static final int NR_COUNTERS=MagicCounterType.values().length;

    private final String name;
    private final String text;
    private final int score;

    private MagicCounterType(final String name,final String text,final int score) {
        this.name=name;
        this.text=text;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public int getScore() {
        return score;
    }

    public static MagicCounterType getCounterRaw(final String name) {
        MagicCounterType match = None;
        for (final MagicCounterType counter : values()) {
            if (name.startsWith(counter.getName()) && counter.getName().length() > match.getName().length()) {
                match = counter;
            }
        }
        if (match == None) {
            throw new RuntimeException("unknown counter type \"" + name + "\"");
        } else {
            return match;
        }
    }
}
