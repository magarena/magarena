package magic.model;

import java.util.EnumSet;
import java.util.Set;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicCounterType {

	MinusZeroMinusOne("-0/-1","{0-}"),
	PlusZeroPlusOne("+0/+1","{0+}"),
	MinusZeroMinusTwo("-0/-2","{0-2}"),
	PlusZeroPlusTwo("+0/+2","{0+2}"),
	MinusOneMinusZero("-1/-0","{-0}"),
	PlusOnePlusZero("+1/+0","{+0}"),
	MinusOne("-1/-1","{--}"),
	PlusOne("+1/+1","{++}"),
	PlusOnePlusTwo("+1/+2","{++2}"),
	MinusTwoMinusTwo("-2/-2","{-2-2}"),
	PlusTwoPlusTwo("+2/+2","{+2+2}"),
	Age("age","{age}"),
	Aim("aim","{aim}"),
	Arrow("arrow","{arw}"),
	Arrowhead("arrowhead","{arh}"),
	Awakening("awakening","{awa}"),
	Blaze("blaze","{blz}"),
	Blood("blood","{bld}"),
	Bounty("bounty","{bty}"),
	Bribery("bribery","{brb}"),
	Carrion("carrion","{crn}"),
	Charge("charge","{chg}"),
	Corpse("corpse","{cor}"),
	Credit("credit","{crd}"),
	Cube("cube","{cbe}"),
	Currency("currency","{cur}"),
	Death("death","{dth}"),
	Delay("delay","{del}"),
	Depletion("depletion","{dep}"),
	Despair("despair","{des}"),
	Devotion("devotion","{dev}"),
	Divinity("divinity","{div}"),
	Doom("doom","{dom}"),
	Dream("dream","{drm}"),
	Echo("echo","{ech}"),
	Elixir("elixir","{elx}"),
	Energy("energy","{egy}"),
	Eon("eon","{eon}"),
	Fade("fade","{fde}"),
	Fate("fate","{fte}"),
	Feather("feather","{fea}"),
	Filibuster("filibuster","{fil}"),
	Flame("flame","{fme}"),
	Flood("flood","{fld}"),
	Fungus("fungus","{fun}"),
	Fuse("fuse","{fse}"),
	Glyph("glyph","{gly}"),
	Gold("gold","{gld}"),
	Growth("growth","{grw}"),
	Hatchling("hatchling","{hch}"),
	Healing("healing","{hea}"),
	Hoofprint("hoofprint","{hpr}"),
	Hourglass("hourglass","{hgl}"),
	Hunger("hunger","{hgr}"),
	Husk("husk","{hsk}"),
	Ice("ice","{ice}"),
	Infection("infection","{inf}"),
	Intervention("intervention","{int}"),
	Javelin("javelin","{jav}"),
	Ki("ki","{ki}"),
	Level("level","{lvl}"),
	Lore("lore","{lre}"),
	Luck("luck","{luc}"),
	Magnet("magnet","{mag}"),
	Mannequin("mannequin","{mqn}"),
	Matrix("matrix","{mtx}"),
	Mine("mine","{min}"),
	Mining("mining","{mng}"),
	Mire("mire","{mre}"),
	Music("music","{mus}"),
	Net("net","{net}"),
	Omen("omen","{omn}"),
	Ore("ore","{ore}"),
	Page("page","{pge}"),
	Pain("pain","{pan}"),
	Paralyzation("paralyzation","{pzn}"),
	Petal("petal","{ptl}"),
	Petrification("petrification","{pet}"),
	Phylactery("phylactery","{phy}"),
	Pin("pin","{pin}"),
	Plague("plague","{plg}"),
	Poison("poison","{psn}"),
	Polyp("polyp","{pyp}"),
	Pressure("pressure","{pss}"),
	Pupa("pupa","{pup}"),
	Quest("quest","{que}"),
	Rust("rust","{rst}"),
	Scream("scream","{srm}"),
	Scroll("scroll","{scr}"),
	Shell("shell","{shl}"),
	Shield("shield","{shd}"),
	Shred("shred","{shr}"),
	Sleep("sleep","{slp}"),
	Sleight("sleight","{sgt}"),
	Slime("slime","{slm}"),
	Soot("soot","{sot}"),
	Spore("spore","{spr}"),
	Storage("storage","{stg}"),
	Strife("strife","{stf}"),
	Study("study","{stu}"),
	Tide("tide","{tde}"),
	Time("time","{tme}"),
	Training("training","{tng}"),
	Trap("trap","{trp}"),
	Treasure("treasure","{tre}"),
	Velocity("velocity","{vel}"),
	Verse("verse","{ver}"),
	Vitality("vitality","{vit}"),
	Wage("wage","{wge}"),
	Winch("winch","{win}"),
	Wind("wind","{wnd}"),
	Wish("wish","{wsh}"),
	None("","");

    public static final int NR_COUNTERS=MagicCounterType.values().length;

    private final String name;
    private final String text;

    private MagicCounterType(final String name,final String text) {
        this.name=name;
        this.text=text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
    
    public static MagicCounterType getCounterRaw(final String name) {
        MagicCounterType match = None;
        for (final MagicCounterType counter : values()) {
            if (name.startsWith(counter.getName()) && counter.getName().length() > match.getName().length()) {
                match = counter;
            }
        }
        if (match == None) {
            throw new RuntimeException("Unable to convert " + name + " to a type of counter");
        } else {
            return match;
        }
    }
}
