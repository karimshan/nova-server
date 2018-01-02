package org.nova.game.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.LocalNPCUpdate;
import org.nova.game.masks.LocalPlayerUpdate;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.npc.godwars.zaros.Nex;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.content.Cannon;
import org.nova.game.player.content.CrystalChest;
import org.nova.game.player.content.Ectophial;
import org.nova.game.player.content.FriendChatsManager;
import org.nova.game.player.content.GiftBox;
import org.nova.game.player.content.GodPath;
import org.nova.game.player.content.ItemEffects;
import org.nova.game.player.content.Pots;
import org.nova.game.player.content.ShatteredHeart;
import org.nova.game.player.content.ShootingStar;
import org.nova.game.player.content.SkillCapeCustomizer;
import org.nova.game.player.content.TitleManager;
import org.nova.game.player.content.Trade;
import org.nova.game.player.content.WildernessHelmets;
import org.nova.game.player.content.cities.content.Crates;
import org.nova.game.player.content.cities.content.DonatorHouse;
import org.nova.game.player.content.cities.content.Fillables;
import org.nova.game.player.content.cities.content.ShipCharter;
import org.nova.game.player.content.cities.content.ZanarisHouse;
import org.nova.game.player.content.dungeoneering.DungeonPartyManager;
import org.nova.game.player.content.exchange.Offer;
import org.nova.game.player.content.handlers.AgilityRewardsHandler;
import org.nova.game.player.content.itemactions.Artifacts;
import org.nova.game.player.content.itemactions.Decant;
import org.nova.game.player.content.itemactions.DwarvenRockCake;
import org.nova.game.player.content.itemactions.ItemBonusActions;
import org.nova.game.player.content.itemactions.MarkerSeeds;
import org.nova.game.player.content.itemactions.RockPlinth;
import org.nova.game.player.content.loyaltyprogramme.LoyaltyProgramme;
import org.nova.game.player.content.minigames.ClanWars;
import org.nova.game.player.content.minigames.clanwars.WarControler;
import org.nova.game.player.content.minigames.duel.DuelArena;
import org.nova.game.player.content.minigames.pestcontrol.PestControlGame;
import org.nova.game.player.content.newuser.CharacterCreation;
import org.nova.game.player.content.playertask.PlayerTaskHandler;
import org.nova.game.player.content.questbind.QuestBinder;
import org.nova.game.player.content.quests.UzerRuins;
import org.nova.game.player.content.quests.Wanted;
import org.nova.game.player.controlers.CastleWarsPlaying;
import org.nova.game.player.controlers.CastleWarsWaiting;
import org.nova.game.player.controlers.CorpBeastControler;
import org.nova.game.player.controlers.DTControler;
import org.nova.game.player.controlers.GodWars;
import org.nova.game.player.controlers.JailControler;
import org.nova.game.player.controlers.PestControlLobby;
import org.nova.game.player.controlers.Wilderness;
import org.nova.game.player.controlers.ZGDControler;
import org.nova.kshan.bot.Bot;
import org.nova.kshan.clans.Clan;
import org.nova.kshan.content.LocationCrystal;
import org.nova.kshan.content.Membership;
import org.nova.kshan.content.PlayTime;
import org.nova.kshan.content.TemporaryData;
import org.nova.kshan.content.areas.Area;
import org.nova.kshan.content.areas.c_o_r.C_O_R;
import org.nova.kshan.content.cutscenes.Cutscenes;
import org.nova.kshan.content.interfaces.BookTab;
import org.nova.kshan.content.interfaces.ScreenFade;
import org.nova.kshan.content.interfaces.Tasks;
import org.nova.kshan.content.interfaces.buttonstroke.ButtonStrokeManager;
import org.nova.kshan.content.keystroke.KeyStrokeManager;
import org.nova.kshan.content.minigames.duelarena.Duel;
import org.nova.kshan.content.pets.PetManager;
import org.nova.kshan.content.skills.construction.PlayerHouse;
import org.nova.kshan.content.skills.slayer.SlayerTask;
import org.nova.kshan.content.slaves.Slave;
import org.nova.kshan.dialogues.DialogueManager;
import org.nova.kshan.dialogues.impl.ChooseGameMode;
import org.nova.kshan.dialogues.testscript.DialogueScriptManager;
import org.nova.kshan.input.InputEvents;
import org.nova.kshan.randoms.RandomEventHandler;
import org.nova.kshan.utilities.Logs;
import org.nova.network.Session;
import org.nova.network.decoders.packets.PacketDecoder;
import org.nova.network.decoders.packets.handlers.ButtonPackets;
import org.nova.network.encoders.PacketSender;
import org.nova.utility.loading.Logger;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public class Player extends Entity {
	
	private static final long serialVersionUID = 2011932556974180375L;

	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;

	private boolean acceptAid;
	private transient ActionManager actionManager;
	private boolean activeDfsSession;
	private boolean activeResource;
	public int agilityPoints;
	private transient AgilityRewardsHandler agilityrewardsHandler;
	public boolean Ahrim;
	public boolean Akrisea;
	public boolean alch;
	private boolean alchSession;
	private boolean allowChatEffects;
	public boolean ancients;
	private Appearance appearence;
	private int ardougneFlowerPatch;
	private boolean ardougneFlowerPatchRaked;
	private int ardougneHerbPatch;
	private boolean ardougneHerbPatchRaked;
	private int ardougneNorthAllotmentPatch;
	private boolean ardougneNorthAllotmentPatchRaked;
	private int ardougneSouthAllotmentPatch;
	private boolean ardougneSouthAllotmentPatchRaked;
	public int ArmadylKills = 0;
	private transient long ashDelay;
	private int assaultStage;
	private int assistStatus;
	private Auras auras;
	public int BandosKills = 0;
	private Bank bank;
	public long bankDelay;
	private Slave slave;
	private long banned;
	public int barbLap;
	private int barrowsKillCount;
	private transient long boneDelay;
	private int bookPages;
	private boolean boostedtasks;
	public int bossid;
	private int[] boundChuncks;
	public int box = 0;
	private boolean buildMode;
	public int burned;
	public long buttonDelay;
	public boolean buying;
	private boolean CamelotKnight;
	private int camelotknightStage;
	private boolean cantChangeLocation;
	private int canifisFlowerPatch;
	private boolean canifisFlowerPatchRaked;
	private int canifisHerbPatch;
	private boolean canifisHerbPatchRaked;
	private int canifisNorthAllotmentPatch;
	private boolean canifisNorthAllotmentPatchRaked;
	private int canifisSouthAllotmentPatch;
	private boolean canifisSouthAllotmentPatchRaked;
	private boolean canLivid;
	private boolean canlootResources;
	private boolean cannotDfs;
	private transient boolean canPvp;
	public boolean canTalkToSprite;
	private transient boolean cantMove;
	private boolean cantReceiveDamage;
	private boolean castedRecite;
	private boolean castedVeng;

	private int catherbyFlowerPatch;
	private boolean catherbyFlowerPatchRaked;
	private int catherbyHerbPatch;
	private boolean catherbyHerbPatchRaked;
	private int catherbyNorthAllotmentPatch;
	private boolean catherbyNorthAllotmentPatchRaked;
	private int catherbySouthAllotmentPatch;
	private boolean catherbySouthAllotmentPatchRaked;
	private transient CharacterCreation characterCreation;
	private transient GodPath characterpath;
	private ChargesManager charges;
	private boolean chargeSpell;
	public long chargeTill;
	private int clanStatus;
	private transient ClanWars clanWars;
	private transient boolean clientLoadedMapRegion;
	private transient Runnable closeInterfacesEvent;
	public int cluenoreward;
	private CombatDefinitions combatDefinitions;
	public int combatExperience = 50;
	private double combatXPBoost = 1.0;
	private boolean completedExams;
	private boolean completedhoarfrost;
	private boolean completedhoarfrostdepths;
	private boolean completedRecipeforDisaster;
	private boolean completedSaradomin;
	private boolean completedTask;
	private boolean completedThrone;
	private boolean completedwanted;
	private int[] completionistCapeCustomized;
	private ControlerManager controlerManager;
	public int cooked;
	private transient CoordsEvent coordsEvent;
	private int crabPoints;
	private transient Crates crates;
	private int credits;

	private transient CrystalChest crystalChest;
	private int crystalX, crystalY, crystalPlane;
	private transient ClanChat currentClanChat;
	private transient FriendChatsManager currentFriendChat;

	private String currentFriendChatOwner;
	public int currentSlot;
	private int currentX, currentY, currentH;
	private int customHeadIcon = -1;
	private int deathHubType;
	private double deaths;
	private transient Decant decant;
	private boolean DeveloperMode;
	public long dfsDelay;
	public boolean Dharrock;
	private transient DialogueManager dialogue;
	private transient MatrixDialogueManager dialogueManager;
	private transient boolean disableEquip;
	private transient int displayMode;
	private String displayName;
	private long displayTime;
	private DominionTower dominionTower;
	public boolean donator;
	private transient DonatorHouse donatorHouse;
	private long donatorTill;
	public int doublexearned;
	public int dragonfireCharges;
	private boolean DraynorMyths;
	private transient DuelConfigurations duelConfigurations;
	public int duelWins;
	public int dungdeaths = 0;
	public DungeonPartyManager dungeon;
	private int dungeoneeringPoints;
	public int dungtime = 0;
	private transient Cannon dwarfcannon;
	private transient DwarvenRockCake dwarvenrockCake;
	private int easterEventStage;
	private transient Ectophial ectophial;
	private EmotesManager emotesManager;
	private boolean enabledChannels;
	private Equipment equipment;
	private String eventName;
	private int examStage;
	private boolean extremeDonator;
	private int faladorFlowerPatch;
	private boolean faladorFlowerPatchRaked;

	private int faladorHerbPatch;
	private boolean faladorHerbPatchRaked;
	private int faladorNorthAllotmentPatch;
	private boolean faladorNorthAllotmentPatchRaked;
	private int faladorSouthAllotmentPatch;
	private boolean faladorSouthAllotmentPatchRaked;
	private int faladorTreePatch;
	private boolean faladorTreePatchRaked;
	private Familiar familiar;
	public int farmob = -1;
	public int farmtim;
	private int favorPoints;
	public int favourpoints;
	public int fifteenxearned;
	public boolean fifthquest;
	public int fiftyxearned;
	private transient Fillables fillables;
	private boolean filterGame;
	private boolean finishedHalloweenEvent;
	public boolean finishedKolodionTask;
	private transient boolean finishing;
	private long fireImmune;
	public int firstColumn = 1, secondColumn = 1, thirdColumn = 1;
	private boolean firstCompCape;
	private boolean firstMaxCape;
	public boolean firstquest;
	public int fished;
	public int fletched;
	private transient long foodDelay;
	private boolean forceNextMapLoadRefresh;
	private FriendsIgnores friendsIgnores;
	private transient List<GameTick> gameTickCache;
	public int geAmount = 0;
	public int geItem = 0;
	public int getHomeH;
	public int getHomeX;
	public int getHomeY;
	private transient GiftBox giftbox;
	public transient byte godDelay;
	public boolean GotVote;
	public boolean Guthan;
	private boolean hadCannon;
	private boolean hadHati;
	private boolean halloweenCostume;
	private int halloweenStage;
	private boolean hasBeenToHouse = false;
	private boolean hasClan;
	private boolean hasConfirmedRoomDeletion = false;
	public boolean hasGuthix, hasSaradomin, hasZamorak;
	public boolean hasPlantedTree;
	public boolean hasRandomEvent;
	private boolean hasVoted;
	private boolean hatibonus;
	private long hatibonusTill;
	private int hiddenBrother;
	private transient HintIconsManager hintIconsManager;
	private int hoarfrostdepthsStage;
	public boolean hoarfrosthollow;
	private int hoarfrostStage;
	private int houseX;
	private int houseY;
	public boolean inChat;
	public boolean inClanChat = false;
	private boolean inClops;

	private TemporaryData temporaryData;
	
	public int increaseFarmWc;
	private boolean inFunPK;
	private boolean inResource;
	private boolean inteam;
	private transient Runnable interfaceListenerEvent;
	private transient Interfaces interfaceManager;
	private boolean inTutorial;
	private Inventory inventory;
	private transient boolean invulnerable;
	private transient IsaacKeyPair isaacKeyPair;
	private boolean isBot;
	public boolean isOnline;
	private transient ItemBonusActions itembonusactions;
	private ItemEffects itemEffects;
	public int[] items;
	public int[] itemsN;
	private long jailed;
	public boolean Kharil;

	private boolean[] killedBarrowBrothers;
	private double kills;
	private transient boolean largeSceneView;
	private String lastIP;
	private String lastMsg;
	public GlobalObject lastObject;
	private transient long lastPublicMessage;
	private int lastRecite;
	private int lastVeng;
	private long lastVoted;

	private boolean[] learnedPrayer = new boolean[3];
	private boolean learnedRenewal;
	private boolean lending;

	private long lendingTill;
	public int lividPoints;

	private transient LocalNPCUpdate localNPCUpdate;
	private transient LocalPlayerUpdate localPlayerUpdate;

	private transient long lockDelay;

	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;
	public int logscutted;
	private boolean lootshareEnabled;
	private int loyaltyPoints;
	private transient LoyaltyProgramme loyaltyprogramme;
	private int lummyTreePatch;
	private boolean lummyTreePatchRaked;
	public boolean magic;
	private MarkerSeeds markerSeeds;

	private int[] maxedCapeCustomized;
	private Membership membership;
	private boolean metAdvisor;

	private int miscellaniaThrone;
	private boolean mouseButtons;

	private transient boolean musicianResting;
	private MusicsManager musicsManager;
	private long muted;
	private String muteReason;

	private boolean newplayer;
	private long newplayerTill;
	private boolean newsbook;
	public GlobalObject object;
	public Offer[] offers;
	private boolean oldItemsLook;
	public int oresmined;
	public int oresStored;
	private int overloadDelay;
	private boolean overloaded;
	private List<String> ownedObjectsManagerKeys;
	private transient long packetsDecoderPing;
	private transient int panelId;

	private String password;
	private boolean permBanned;
	private int pestPoints;
	private PetManager pet;
	private int petFollow = -1;
	public int petId;
	private boolean pickedGameMode;
	private int pkPoints;
	private int PKPoints;
	private int place;
	public boolean planted;
	private transient PlayerTaskHandler playertaskHandler;
	private PlayTime playTime;
	private int plinthStage;
	private long poisonImmune;
	private transient long polDelay;
	private transient long potDelay;
	private int[] pouches;
	private Prayer prayer;
	private int prayerRenewalDelay;
	public int prestige;
	public int price = 0;
	private transient PriceCheckManager priceCheckManager;
	private int privateChatSetup;
	private boolean profanityFilter;
	private int publicStatus;
	private ArrayList<Integer> purchasedloyaltyauras;
	private transient QuestBinder questbinder;
	public int questpoints;
	private int questStage;
	private boolean quizEvent;
	private RandomEventHandler randomEvent;
	private transient Cutscenes cutscenes;
	public int randomeventPoints;
	private long randomEventTimer;
	private Pets realPet;
	private boolean receivedSantaSuit;
	private boolean receivedwildyhat;
	private long rechargeTill;
	private int recipeStage;
	private boolean recite;
	private boolean removedPrayers;
	private int render = -1;

	private int reports;

	public int resourcepoints;
	private int resourcesTotal;
	private int resourceTime;
	private transient boolean resting;
	private int rights;

	private transient RockPlinth rockPlinth;

	private ArrayList<GlobalObject> rockplinthObject;
	public int rocksAddedToPlinth;
	private boolean ruinsofuzer;
	private int ruinsofuzerStage;
	private byte runEnergy;
	private transient boolean running;
	
	public int SaradominKills = 0;

	private int saradominStage;

	private int savedHomeX, savedHomeY, savedHomeZ;

	private int saveplane;

	private int saveX;

	private int saveY;

	public int scapeKeys;

	private transient int screenHeight;

	private transient int screenWidth;

	private transient InputEvents inputEvents;

	public boolean secondquest;

	public boolean sentInter = false;

	private transient Session session;

	private ShatteredHeart shatteredHeart;

	private transient ShipCharter shipcharter;

	private transient ShootingStar shootingStar;
	private Skills skills;
	private double skillXPBoost = 1.0;
	private int skullDelay;

	private int skullId;

	private int slayerPoints;

	private SlayerTask slayerTask;

	public int smithed;

	public boolean softreset;

	private boolean speakedwithMaverick;

	private transient boolean started;

	public int starter = 0;

	private boolean completedTutorial;

	private int starterStage;

	private int startingStage;

	private transient long stopDelay;

	private boolean storedBones;

	private int streak;

	private int strongholdStage;

	private boolean summer;

	private int summoningLeftClickOption;

	private transient List<Integer> switchItemCache;

	public boolean taggedStar;

	private Entity target;

	public String taskName;

	public int taskpoints;

	public Tasks tasks;

	private int taverlyTreePatch;

	private boolean taverlyTreePatchRaked;

	private boolean teleport;

	public boolean teleporting;

	private transient Map<String, Object> temporaryAttributes;

	private int temporaryMovementType;

	public boolean thirdquest;

	public int thirtyxearned;

	private boolean tier1;

	private boolean tier2;

	private boolean tier3;

	private boolean tier4;

	private boolean tier5;

	private boolean tier6;

	public int timesVoted;

	private long timeUntilWrongLocation;

	private TitleManager titleManager;

	public int toks = 0;

	public boolean Torag;

	private transient Trade trade;

	private int tradeStatus;

	private boolean transformed;

	private transient int trapAmount;

	public int treescutted;

	private int tStage;
	private boolean unselectedXP;

	private boolean updateMovementType;

	private int urnStage;
	protected transient String username;
	private transient boolean usingTicket;
	private boolean vantsPermission;
	private int varrockTreePatch;
	private boolean varrockTreePatchRaked;
	/**
	 * Did the current player vengeance someone?
	 */
	private boolean vengeanceOther;
	/**
	 * The timer for vengeance till.
	 */
	private long vengeanceTill;
	public boolean Verac;
	private boolean veteran;
	private int votePoints;
	public int VotePoints = 0;
	private int wantedstage;
	public int wGuildTokens;
	private transient WildernessHelmets wildernessHats;
	private boolean worldNotifications;

	private boolean xpBoost;

	private long XPBoostTill;

	public boolean xpLock;

	public int xpModifier;

	public boolean yew;

	public int ZamorakKills = 0;

	private transient ZanarisHouse zanarisHouse;
	
	private PlayerHouse house;
	private transient Duel duel;

	public Player(String password) {
		this(password, Constants.START_PLAYER_LOCATION);
	}

	public Player(String password, Location location) {
		super(location);
		setHitpoints(100); // Default hitpoints
		this.password = password;
		appearence = new Appearance();
		membership = new Membership();
		inventory = new Inventory();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		controlerManager = new ControlerManager();
		randomEvent = new RandomEventHandler();
		cutscenes = new Cutscenes(this);
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		friendsIgnores = new FriendsIgnores();
		dominionTower = new DominionTower();
		charges = new ChargesManager();
		auras = new Auras();
		runEnergy = 100;
		allowChatEffects = true;
		slayerTask = new SlayerTask();
		mouseButtons = true;
		pouches = new int[4];
		house = new PlayerHouse(this);
		killedBarrowBrothers = new boolean[6];
		playTime = new PlayTime();
		tasks = new Tasks();
		SkillCapeCustomizer.resetSkillCapes(this);
		ownedObjectsManagerKeys = new LinkedList<String>();
	}

	public void addAshDelay(long time) {
		ashDelay = time + Misc.currentTimeMillis();
	}

	public void addBoneDelay(long time) {
		boneDelay = time + Misc.currentTimeMillis();
	}

	@SuppressWarnings("deprecation")
	public void addChargeSpell(int seconds) {
		if (chargeTill < Misc.currentTimeMillis())
			chargeTill = Misc.currentTimeMillis();
		Date date = new Date(chargeTill);
		date.setSeconds((date.getSeconds() + seconds));
		chargeTill = date.getTime();
	}

	public void addDisplayTime(long i) {
		this.displayTime = i + Misc.currentTimeMillis();
	}

	@SuppressWarnings("deprecation")
	public void addDragonfireTimerTask(int seconds) {
		if (dfsDelay < Misc.currentTimeMillis())
			dfsDelay = Misc.currentTimeMillis();
		Date date = new Date(dfsDelay);
		date.setSeconds((date.getSeconds() + seconds));
		dfsDelay = date.getTime();
	}

	public void addFireImmune(long time) {
		fireImmune = time + Misc.currentTimeMillis();
	}

	public void addFoodDelay(long time) {
		foodDelay = time + Misc.currentTimeMillis();
	}

	public void addLogicPacketToQueue(LogicPacket packet) {
		for (LogicPacket p : logicPackets) {
			if (p.getId() == packet.getId()) {
				logicPackets.remove(p);
				break;
			}
		}
		logicPackets.add(packet);
	}

	public void addPoisonImmune(long time) {
		poisonImmune = time + Misc.currentTimeMillis();
		getPoison().reset();
	}

	public void addPolDelay(long delay) {
		polDelay = delay + Misc.currentTimeMillis();
	}

	public void addPotDelay(long time) {
		potDelay = time + Misc.currentTimeMillis();
	}

	public void addStopDelay(long delay) {
		stopDelay = Misc.currentTimeMillis() + (delay * 600);
	}

	/**
	 * Adds a given delay for vengeance other.
	 *
	 * @param seconds
	 */
	@SuppressWarnings("deprecation")
	public void addVengeanceOther(int seconds) {
		if (vengeanceTill < Misc.currentTimeMillis())
			vengeanceTill = Misc.currentTimeMillis();
		Date date = new Date(vengeanceTill);
		date.setSeconds((date.getSeconds() + seconds));
		vengeanceTill = date.getTime();
	}

	public void camLook(int x, int y, int height, int speed) {
		packets().sendCameraLook(Game.getCutsceneX(this, x), Game.getCutsceneY(this, y), height, speed, speed);
	}

	public void camPos(int x, int y, int height, int speed) {
		packets().sendCameraPos(Game.getCutsceneX(this, x), Game.getCutsceneY(this, y), height, speed, speed);
	}

	public boolean cantChangeLocation() {
		return cantChangeLocation;
	}

	public boolean canSpawn() {
		if (Wilderness.isAtWild(this)

				|| getControllerManager().getControler() instanceof CorpBeastControler
				|| getControllerManager().getControler() instanceof PestControlLobby
				|| getControllerManager().getControler() instanceof PestControlGame
				|| getControllerManager().getControler() instanceof ZGDControler
				|| getControllerManager().getControler() instanceof GodWars
				|| getControllerManager().getControler() instanceof JailControler
				|| getControllerManager().getControler() instanceof DTControler
				|| getControllerManager().getControler() instanceof DuelArena
				|| getControllerManager().getControler() instanceof CastleWarsPlaying
				|| getControllerManager().getControler() instanceof CastleWarsWaiting
				|| getControllerManager().getControler() instanceof WarControler) {
			return false;
		}
		return true;
	}

	public boolean cantMove() {
		return cantMove;
	}

	public String checkdonation(String username) {
		try {
			URL url = new URL("http://Nova-rs.com/check.php?username="
					+ username + "");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String result = reader.readLine();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "MYSQL";
	}

	@Override
	public void checkMultiArea() {
		if (!started)
			return;
		boolean isAtMultiArea = isForceMultiArea() ? true : Game
				.isMultiArea(this);
		if (isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			packets().sendGlobalConfig(616, 1);
		} else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			packets().sendGlobalConfig(616, 0);
		}
	}

	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}

	public void closeInterfaces() {
		if (getTrade() != null) {
			return;
		}
		if (duel != null)
			return;
		if (interfaceManager.containsScreenInter())
			interfaceManager.closeScreenInterface();
		if (interfaceManager.containsInventoryInter())
			interfaceManager.closeInventoryInterface();
		if(!interfaces().chatboxKeptOpen()) {
			if(interfaces().containsChatBoxInter()) {
				dialogueManager.finishDialogue();
				dialogue.finishDialogue();
				dialogueScript.terminate();
			}
		}
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}

	public double combatXPBoost() {
		return combatXPBoost;
	}

	public void combatXPBoost(double b) {
		this.combatXPBoost = b;
	}

	public void decreaseLoyaltyPoints(int price) {
		this.price = price;
	}

	public DialogueManager getDialogue() {
		return dialogue;
	}

	public void drainRunEnergy() {
		setRunEnergy(runEnergy - 1);
	}

	public void extraAutoSave() {
		// SQL.createConnection();
	}

	@Override
	public void finish() {
		if (finishing || hasFinished())
			return;
		finishing = true;
		long currentTime = Misc.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime
				|| getEmotesManager().getNextEmoteEnd() >= currentTime
				|| stopDelay >= currentTime) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Misc.currentTimeMillis();
						finishing = false;
						finish();
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	public String gameMode() {
		return combatXPBoost() == 195 ? "Easy"
				: combatXPBoost() == 75 ? "Normal"
						: combatXPBoost() == 20 ? "Advanced"
								: combatXPBoost() == 3.8 ? "EXTREME" : "Unknown";
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public AgilityRewardsHandler getAgilityrewardsHandler() {
		return agilityrewardsHandler;
	}

	public Appearance getAppearance() {
		return appearence;
	}

	public int getArdougneFlowerPatch() {
		return ardougneFlowerPatch;
	}

	public boolean getArdougneFlowerPatchRaked() {
		return ardougneFlowerPatchRaked;
	}

	public int getArdougneHerbPatch() {
		return ardougneHerbPatch;
	}

	public boolean getArdougneHerbPatchRaked() {
		return ardougneHerbPatchRaked;
	}

	public int getArdougneNorthAllotmentPatch() {
		return ardougneNorthAllotmentPatch;
	}

	public boolean getArdougneNorthAllotmentPatchRaked() {
		return ardougneNorthAllotmentPatchRaked;
	}

	public int getArdougneSouthAllotmentPatch() {
		return ardougneSouthAllotmentPatch;
	}

	public boolean getArdougneSouthAllotmentPatchRaked() {
		return ardougneSouthAllotmentPatchRaked;
	}

	public long getAshDelay() {
		return ashDelay;
	}

	public int getAssaultStage() {
		return assaultStage;
	}

	public int getAssistStatus() {
		return assistStatus;
	}

	public Auras getAuraManager() {
		return auras;
	}

	public Bank getBank() {
		return bank;
	}

	public long getBannedDelay() {
		return banned;
	}
	
	public boolean isBanned() {
		return banned > Misc.currentTimeMillis() || permBanned;
	}

	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public long getBoneDelay() {
		return boneDelay;
	}

	public int getBookPages() {
		return bookPages;
	}

	public int[] getBoundChuncks() {
		return boundChuncks;
	}

	public int getCamelotknightStage() {
		return camelotknightStage;
	}

	public int getCanifisFlowerPatch() {
		return canifisFlowerPatch;
	}

	public boolean getCanifisFlowerPatchRaked() {
		return canifisFlowerPatchRaked;
	}

	public int getCanifisHerbPatch() {
		return canifisHerbPatch;
	}

	public boolean getCanifisHerbPatchRaked() {
		return canifisHerbPatchRaked;
	}

	public int getCanifisNorthAllotmentPatch() {
		return canifisNorthAllotmentPatch;
	}

	public boolean getCanifisNorthAllotmentPatchRaked() {
		return canifisNorthAllotmentPatchRaked;
	}

	public int getCanifisSouthAllotmentPatch() {
		return canifisSouthAllotmentPatch;
	}

	public boolean getCanifisSouthAllotmentPatchRaked() {
		return canifisSouthAllotmentPatchRaked;
	}

	public int getCatherbyFlowerPatch() {
		return catherbyFlowerPatch;
	}

	public boolean getCatherbyFlowerPatchRaked() {
		return catherbyFlowerPatchRaked;
	}

	public int getCatherbyHerbPatch() {
		return catherbyHerbPatch;
	}

	public boolean getCatherbyHerbPatchRaked() {
		return catherbyHerbPatchRaked;
	}

	public int getCatherbyNorthAllotmentPatch() {
		return catherbyNorthAllotmentPatch;
	}

	public boolean getCatherbyNorthAllotmentPatchRaked() {
		return catherbyNorthAllotmentPatchRaked;
	}

	public int getCatherbySouthAllotmentPatch() {
		return catherbySouthAllotmentPatch;
	}

	public boolean getCatherbySouthAllotmentPatchRaked() {
		return catherbySouthAllotmentPatchRaked;
	}

	public CharacterCreation getCharacterCreation() {
		return characterCreation;
	}

	public GodPath getCharacterpath() {
		return characterpath;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public ClanChat getClanChat() {
		return currentClanChat;
	}

	public int getClanStatus() {
		return clanStatus;
	}

	public ClanWars getClanWars() {
		return clanWars;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}

	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	public ControlerManager getControllerManager() {
		return controlerManager;
	}

	public int getCooked() {
		return cooked;
	}

	public int getCrabPoints() {
		return crabPoints;
	}

	public Crates getCrates() {
		return crates;
	}

	public int getCredits() {
		return credits;
	}

	public CrystalChest getCrystalChest() {
		return crystalChest;
	}

	public int getCrystalPlane() {
		return crystalPlane;
	}

	public int getCrystalX() {
		return crystalX;
	}

	public int getCrystalY() {
		return crystalY;
	}

	public FriendChatsManager getCurrentFriendChat() {
		return currentFriendChat;
	}

	public String getCurrentFriendChatOwner() {
		return currentFriendChatOwner;
	}

	public int getCurrentH() {
		return currentH;
	}

	public String getCurrentLocation() {
		String loc = "Unknown";
//		try {
//			BufferedReader r = new BufferedReader(new FileReader(new File(
//					"data/map/locations.txt")));
//			while (true) {
//				String line = r.readLine();
//				if (line == null)
//					break;
//				if (line.startsWith("//") || line.equals(""))
//					continue;
//				String[] lineSplit = line.split(" - ");
//				int regionId = Integer.parseInt(lineSplit[0]);
//				String location = lineSplit[1];
//				if (getRegionId() == regionId)
//					loc = location;
//			}
//			r.close();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		} // TODO
		return loc;
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public int getCustomHeadIcon() {
		return customHeadIcon;
	}

	public int getDeathHubType() {
		return deathHubType;
	}

	public double getDeaths() {
		return deaths;
	}

	public Decant getDecant() {
		return decant;
	}

	public MatrixDialogueManager getMatrixDialogues() {
		return dialogueManager;
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public String getDisplayName() {
		if (displayName != null)
			return displayName;
		return Misc.formatPlayerNameForDisplay(username);
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public DominionTower getDominionTower() {
		return dominionTower;
	}

	public DonatorHouse getDonatorHouse() {
		return donatorHouse;
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "0" : new Date(donatorTill).toGMTString()) + ".";
	}

	public int getDragonfireCharges() {
		return dragonfireCharges;
	}

	public DuelConfigurations getDuelConfigurations() {
		return duelConfigurations;
	}

	public int getDungeoneeringPoints() {
		return dungeoneeringPoints;
	}

	public Cannon getDwarfcannon() {
		return dwarfcannon;
	}

	public DwarvenRockCake getDwarvenrockCake() {
		return dwarvenrockCake;
	}

	public int getEasterEventStage() {
		return easterEventStage;
	}

	public Ectophial getEctophial() {
		return ectophial;
	}

	public EmotesManager getEmotesManager() {
		return emotesManager;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public String getEventName() {
		return eventName;
	}

	public int getExamStage() {
		return examStage;
	}

	public int getEXPBoostTime() {
		return this.getXPBoostLeft().length();
	}

	public int getFaladorFlowerPatch() {
		return faladorFlowerPatch;
	}

	public boolean getFaladorFlowerPatchRaked() {
		return faladorFlowerPatchRaked;
	}

	public int getFaladorHerbPatch() {
		return faladorHerbPatch;
	}

	public boolean getFaladorHerbPatchRaked() {
		return faladorHerbPatchRaked;
	}

	public int getFaladorNorthAllotmentPatch() {
		return faladorNorthAllotmentPatch;
	}

	public boolean getFaladorNorthAllotmentPatchRaked() {
		return faladorNorthAllotmentPatchRaked;
	}

	public int getFaladorSouthAllotmentPatch() {
		return faladorSouthAllotmentPatch;
	}

	public boolean getFaladorSouthAllotmentPatchRaked() {
		return faladorSouthAllotmentPatchRaked;
	}

	public int getFaladorTreePatch() {
		return faladorTreePatch;
	}

	public boolean getFaladorTreePatchRaked() {
		return faladorTreePatchRaked;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public int getFavorPoints() {
		return favorPoints;
	}

	public Fillables getFillables() {
		return fillables;
	}

	public long getFireImmune() {
		return fireImmune;
	}

	public int getFirstColumn() {
		return this.firstColumn;
	}

	public long getFoodDelay() {
		return foodDelay;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}

	public GameTick getGameTickFromCache(String tick) {
		for (GameTick t : gameTickCache)
			if (t.getName().equalsIgnoreCase(tick))
				return t;
		return null;
	}

	public List<GameTick> getGameTicks() {
		return gameTickCache;
	}

	public GiftBox getGiftbox() {
		return giftbox;
	}
	
	public int getHalloweenStage() {
		return halloweenStage;
	}

	public long getHatibonusTill() {
		return hatibonusTill;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public int getHoarfrostdepthsStage() {
		return hoarfrostdepthsStage;
	}

	public int getHoarfrostStage() {
		return hoarfrostStage;
	}

	public int getHouseX() {
		return houseX;
	}

	public int getHouseY() {
		return houseY;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public IsaacKeyPair getIsaacKeyPair() {
		return isaacKeyPair;
	}

	public ItemBonusActions getItembonusactions() {
		return itembonusactions;
	}

	public ItemEffects getItemEffects() {
		return itemEffects;
	}

	public long getJailed() {
		return jailed;
	}

	public double getKDR() {
		return kills / deaths;
	}

	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	public double getKills() {
		return kills;
	}

	public String getLastIP() {
		return lastIP;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public GlobalObject getLastObject() {
		return lastObject;
	}

	public long getLastPublicMessage() {
		return lastPublicMessage;
	}

	public int getLastRecite() {
		return lastRecite;
	}

	public int getLastVeng() {
		return lastVeng;
	}

	public long getLastVote() {
		return lastVoted;
	}

	public boolean[] getLearnedPrayer() {
		return learnedPrayer;
	}

	public long getLendingTill() {
		return lendingTill;
	}

	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}

	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}

	public long getLockDelay() {
		return lockDelay;
	}

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public LoyaltyProgramme getLoyaltyprogramme() {
		return loyaltyprogramme;
	}

	public int getLummyTreePatch() {
		return lummyTreePatch;
	}

	public boolean getLummyTreePatchRaked() {
		return lummyTreePatchRaked;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	public MarkerSeeds getMarkerSeeds() {
		return markerSeeds;
	}

	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}

	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 10
				+ equipment.getEquipmentHpIncrease();
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	public Membership getMembership() {
		return membership;
	}

	public int getMessageAmount() {
		Integer messageAmount = (Integer) getTemporaryAttributtes().get(
				"Message");
		if (messageAmount == null)
			return 0;
		return messageAmount;
	}

	public int getMiscellaniaThrone() {
		return miscellaniaThrone;
	}

	public int getMovementType() {
		if (getTemporaryMoveType() != -1)
			return getTemporaryMoveType();
		return isRunning() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
	}

	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public long getMutedDelay() {
		return muted;
	}

	public String getMuteReason() {
		return muteReason;
	}
	
	public boolean isMuted() {
		return muted > Misc.currentTimeMillis();
	}

	public int getOverloadDelay() {
		return overloadDelay;
	}

	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null)
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}

	public String getPassword() {
		return password;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public int getPetFollow() {
		return petFollow;
	}

	public int getPetId() {
		return petId;
	}

	public int getPkPoints() {
		return pkPoints;
	}

	public int getPKPoints() {
		return PKPoints;
	}

	public int getPlace() {
		return place;
	}

	public PlayerTaskHandler getPlayerTaskHandler() {
		return playertaskHandler;
	}

	public int getPlinthStage() {
		return plinthStage;
	}

	public long getPoisonImmune() {
		return poisonImmune;
	}

	public long getPolDelay() {
		return polDelay;
	}

	public long getPotDelay() {
		return potDelay;
	}

	public int[] getPouches() {
		return pouches;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}

	public int getPrivateChatSetup() {
		return privateChatSetup;
	}

	public int getPublicStatus() {
		return publicStatus;
	}

	public ArrayList<Integer> getPurchasedLoyaltyAuras() {
		return purchasedloyaltyauras;
	}

	public boolean[] getPurchasedLoyaltyItems(String category) {
		return null;
	}

	public QuestBinder getQuestbinder() {
		return questbinder;
	}

	public int getQuestStage() {
		return questStage;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	public Pets getRealPet() {
		return realPet;
	}

	@SuppressWarnings("deprecation")
	public String getrechargeTill() {
		return (donator ? "0" : new Date(rechargeTill).toGMTString()) + ".";
	}

	public int getRecipeStage() {
		return recipeStage;
	}

	public int getReports() {
		return reports;
	}

	public int getResourcepoints() {
		return resourcepoints;
	}

	public int getResourcesTotal() {
		return resourcesTotal;
	}

	public int getResourceTime() {
		return resourceTime;
	}

	public int getRights() {
		return rights;
	}

	public RockPlinth getRockPlinth() {
		return rockPlinth;
	}

	public ArrayList<GlobalObject> getRockplinthObject() {
		return rockplinthObject;
	}

	public int getRocksAddedToPlinth() {
		return rocksAddedToPlinth;
	}

	public int getRoomX() {
		return Math.round(getXInRegion() / 8);
	}

	public int getRoomY() {
		return Math.round(getYInRegion() / 8);
	}

	public int getRuinsofuzerStage() {
		return ruinsofuzerStage;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public int getSaradominStage() {
		return saradominStage;
	}

	public int getSavedHomeX() {
		return savedHomeX;
	}

	public int getSavedHomeY() {
		return savedHomeY;
	}

	public int getSavedHomeZ() {
		return savedHomeZ;
	}

	public int getSaveplane() {
		return saveplane;
	}

	public int getSaveX() {
		return saveX;
	}

	public int getSaveY() {
		return saveY;
	}

	public int getScapeKeys() {
		return scapeKeys;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getSecondColumn() {
		return this.secondColumn;
	}

	public Session getSession() {
		if(isBot)
			return new Session(null);
		return session;
	}

	public ShipCharter getShipcharter() {
		return shipcharter;
	}

	public ShootingStar getShootingStar() {
		return shootingStar;
	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public int getSkullId() {
		return skullId;
	}

	public int getStarterStage() {
		return starterStage;
	}

	public int getStartingStage() {
		return startingStage;
	}

	public long getStopDelay() {
		return stopDelay;
	}

	public int getStreak() {
		return streak;
	}

	public int getStrongholdStage() {
		return strongholdStage;
	}

	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public List<Integer> getSwitchItemCache() {
		return switchItemCache;
	}

	public Entity getTarget() {
		return target;
	}

	public int getTaverlyTreePatch() {
		return taverlyTreePatch;
	}

	public boolean getTaverlyTreePatchRaked() {
		return taverlyTreePatchRaked;
	}

	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public Object getTemporaryAttribute(String attribute) {
		return temporaryAttributes.get(attribute);
	}

	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}

	public int getThirdColumn() {
		return this.thirdColumn;
	}

	public TitleManager getTitleManager() {
		return titleManager;
	}

	public Trade getTrade() {
		return trade;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public int getTrapAmount() {
		return trapAmount;
	}

	public int gettStage() {
		return tStage;
	}

	public int getUrnStage() {
		return urnStage;
	}

	public String getUsername() {
		return username;
	}

	public int getVarrockTreePatch() {
		return varrockTreePatch;
	}

	public boolean getVarrockTreePatchRaked() {
		return varrockTreePatchRaked;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public int getWantedStage() {
		return wantedstage;
	}

	public int getWGuildTokens() {
		return wGuildTokens;
	}

	public WildernessHelmets getWildernessHats() {
		return wildernessHats;
	}

	public String getXPBoostLeft() {
		return (xpBoost ? "0" : new Date(XPBoostTill).getTime()) + ".";

	}

	public ZanarisHouse getZanarisHouse() {
		return zanarisHouse;
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (equipment.wearingRingOfStone())
			equipment.deactivateRingOfStone();
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (invulnerable) {
			hit.setDamage(0);
			return;
		}
		if (auras.usingPenance()) {
			int amount = (int) (hit.getDamage() * 0.2);
			if (amount > 0)
				prayer.restorePrayer(amount);
		}
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof NPC) {
			NPC n = (NPC) source;
			if (n.getCombatDefinitions().canAttackThroughPrayer()) {

			}
		}
		if (polDelay > Misc.currentTimeMillis())
			hit.setDamage((int) (hit.getDamage() * 0.5));
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17))
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18))
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2229));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19))
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_MELEE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_RANGE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_MAGE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) {
			if (Misc.getRandom(100) <= 70)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) {
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75),
					HitLook.REGULAR_DAMAGE));
		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.prayer.hasPrayersOn()) {
				if (p2.prayer.usingPrayer(0, 24)) { // smite
					int drain = hit.getDamage() / 4;
					if (drain > 0)
						prayer.drainPrayer(drain);
				} else {
					if (hit.getDamage() == 0)
						return;
					if (!p2.prayer.isBoostedLeech()) {
						if (hit.getLook() == HitLook.MELEE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 19)) {
								if (Misc.getRandom(4) == 0) {
									p2.prayer.increaseTurmoilBonus(this);
									p2.prayer.setBoostedLeech(true);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 1)) { // sap att
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(0)) {
										p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(0);
										p2.packets()
										.sendMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2214));
									p2.prayer.setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2215, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2216));
										}
									}, 1);
									return;
								}
							} else {
								if (p2.prayer.usingPrayer(1, 10)) {
									if (Misc.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(3)) {
											p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(3);
											p2.packets()
											.sendMessage(
													"Your curse drains Attack from the enemy, boosting your Attack.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										Game.sendProjectile(p2, this, 2231,
												35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(
												new WorldTask() {
													@Override
													public void run() {
														setNextGraphics(new Graphics(
																2232));
													}
												}, 1);
										return;
									}
								}
								if (p2.prayer.usingPrayer(1, 14)) {
									if (Misc.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(7)) {
											p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(7);
											p2.packets()
											.sendMessage(
													"Your curse drains Strength from the enemy, boosting your Strength.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										Game.sendProjectile(p2, this, 2248,
												35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(
												new WorldTask() {
													@Override
													public void run() {
														setNextGraphics(new Graphics(
																2250));
													}
												}, 1);
										return;
									}
								}

							}
						}
						if (hit.getLook() == HitLook.RANGE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 2)) { // sap range
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(1)) {
										p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(1);
										p2.packets()
										.sendMessage(
												"Your curse drains Range from the enemy, boosting your Range.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.prayer.setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2218, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2219));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 11)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(4)) {
										p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(4);
										p2.packets()
										.sendMessage(
												"Your curse drains Range from the enemy, boosting your Range.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2236, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2238));
										}
									});
									return;
								}
							}
						}
						if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 3)) { // sap mage
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(2)) {
										p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(2);
										p2.packets()
										.sendMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.prayer.setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2221, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2222));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 12)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(5)) {
										p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(5);
										p2.packets()
										.sendMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2240, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2242));
										}
									}, 1);
									return;
								}
							}
						}

						// overall

						if (p2.prayer.usingPrayer(1, 13)) { // leech defence
							if (Misc.getRandom(10) == 0) {
								if (p2.prayer.reachedMax(6)) {
									p2.packets()
									.sendMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.prayer.increaseLeechBonus(6);
									p2.packets()
									.sendMessage(
											"Your curse drains Defence from the enemy, boosting your Defence.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2244, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2246));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 15)) {
							if (Misc.getRandom(10) == 0) {
								if (getRunEnergy() <= 0) {
									p2.packets()
									.sendMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100
											: p2.getRunEnergy() + 10);
									setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy() - 10
											: 0);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2256, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2258));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 16)) {
							if (Misc.getRandom(10) == 0) {
								if (combatDefinitions
										.getSpecialAttackPercentage() <= 0) {
									p2.packets()
									.sendMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.combatDefinitions.restoreSpecialAttack();
									combatDefinitions
									.desecreaseSpecialAttack(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2252, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2254));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 4)) { // sap spec
							if (Misc.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.prayer.setBoostedLeech(true);
								if (combatDefinitions
										.getSpecialAttackPercentage() <= 0) {
									p2.packets()
									.sendMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									combatDefinitions
									.desecreaseSpecialAttack(10);
								}
								Game.sendProjectile(p2, this, 2224, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2225));
									}
								}, 1);
								return;
							}
						}
					}
				}
			}
		} else {
			NPC n = (NPC) source;
			if (n.getId() == 13448)
				sendSoulSplit(hit, n);
		}
	}

	public boolean hasBeenToHouse() {
		return hasBeenToHouse;
	}

	public boolean hasCustomHeadIcon() {
		return getCustomHeadIcon() > -1;
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}

	public boolean hasFinishedKolodionTask() {
		return finishedKolodionTask;
	}

	public boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 1377:
		case 13472:
		case 35:// Excalibur
		case 8280:
		case 14632:
		case 24455:
		case 24456:
		case 24457:
		case 14679:
			return true;
		default:
			return false;
		}
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean hasVoted() {
		long timeLeft = (lastVoted - Misc.currentTimeMillis());
		long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
		long min = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(timeLeft));
		long sec = TimeUnit.MILLISECONDS.toSeconds(timeLeft)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(timeLeft));
		String time = "" + hours + "h " + min + "m " + sec + "s";
		if (timeLeft > 0) {
			packets().sendMessage(
					"You must wait <col=FF0000>" + time
					+ "</col> before you may claim another reward.");
			return true;
		}
		return false;
	}

	public boolean hasXPBoost() {
		return donator || donatorTill > Misc.currentTimeMillis();
	}

	@Override
	public void heal(int ammount, int extra) {
		super.heal(ammount, extra);
		refreshHitPoints();
	}

	public HintIconsManager hints() {
		return hintIconsManager;
	}

	public boolean inArea(String areaName) {
		for (Area area : Area.getAreas())
			if (area != null)
				if (areaName.equalsIgnoreCase(area.getName()))
					if (Area.inArea(this, area))
						return true;
		return false;
	}

	public boolean inClopsRoom() {
		return inClops;
	}

	public void incrementMessageAmount() {
		getTemporaryAttributtes().put("Message", getMessageAmount() + 1);
	}

	public void init(Session session, String string) {
		username = string;
		this.session = session;

	}

	public void init(Session session, String username, int displayMode,
			int screenWidth, int screenHeight, IsaacKeyPair isaacKeyPair) {
		if (dominionTower == null)
			dominionTower = new DominionTower();
		if (auras == null)
			auras = new Auras();
		if(this.locationCrystal == null)
			locationCrystal = new LocationCrystal();
		if(cavernOfRemembrance == null)
			cavernOfRemembrance = new C_O_R(this);
		this.keyStrokes = new KeyStrokeManager(this);
		this.buttonStrokes = new ButtonStrokeManager(this);
		this.session = session;
		this.setIsaacKeyPair(isaacKeyPair);
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		screenFade = new ScreenFade(this);
		interfaceManager = new Interfaces(this);
		dialogueManager = new MatrixDialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		dialogue = new DialogueManager(this);
		dialogueScript = new DialogueScriptManager(this);
		priceCheckManager = new PriceCheckManager(this);
		playertaskHandler = new PlayerTaskHandler();
		ectophial = new Ectophial(this);
		inputEvents = new InputEvents(this);
		characterCreation = new CharacterCreation(this);
		dwarfcannon = new Cannon(this);
		zanarisHouse = new ZanarisHouse(this);
		crates = new Crates(this);
		shipcharter = new ShipCharter(this);
		agilityrewardsHandler = new AgilityRewardsHandler(this);
		loyaltyprogramme = new LoyaltyProgramme(this);
		dwarvenrockCake = new DwarvenRockCake(this);
		rockPlinth = new RockPlinth(this);
		shatteredHeart = new ShatteredHeart(this);
		shootingStar = new ShootingStar(this);
		donatorHouse = new DonatorHouse(this);
		characterpath = new GodPath(this);
		itembonusactions = new ItemBonusActions(this);
		fillables = new Fillables(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		appearence.setPlayer(this);
		if (membership == null)
			membership = new Membership();
		if(clan == null)
			clan = new Clan(this);
		membership.player(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		if (playTime == null)
			playTime = new PlayTime();
		if (slayerTask == null)
			slayerTask = new SlayerTask();
		if (tasks == null)
			tasks = new Tasks();
		if(house == null)
			house = new PlayerHouse(this);
		if(temporaryData == null)
			temporaryData = new TemporaryData();
		if(slave == null)
			slave = new Slave();
		slave.setPlayer(this);
		temporaryData.setFields(this);
		tasks.setPlayer(this);
		slayerTask.setPlayer(this);
		playTime.setPlayer(this);
		bank.setPlayer(this);
		controlerManager.setPlayer(this);
		if(cutscenes == null)
			cutscenes = new Cutscenes(this);
		randomEvent.set(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		dominionTower.setPlayer(this);
		auras.setPlayer(this);
		charges.setPlayer(this);
		setDirection(Misc.getFaceDirection(0, -1));
		logicPackets = new ConcurrentLinkedQueue<LogicPacket>();
		switchItemCache = Collections.synchronizedList(new ArrayList<Integer>());
		initEntity();
		temporaryAttributes = new HashMap<String, Object>();
		gameTickCache = new ArrayList<GameTick>();
		packetsDecoderPing = Misc.currentTimeMillis();
		Game.addPlayer(this);
		Game.updateEntityRegion(this);
		System.out.println("[Players] " + getUsername()
				+ " logged in. Password: " + getPassword()
				+ " [Players online: " + Game.getPlayers().size() + "]");
	}

	public Interfaces interfaces() {
		return interfaceManager;
	}

	public boolean isAcceptAid() {
		return acceptAid;
	}

	public boolean isActiveDfsSession() {
		return activeDfsSession;
	}

	public boolean isActiveResource() {
		return activeResource;
	}

	public boolean isAlchSession() {
		return alchSession;
	}

	public boolean isBoostedtasks() {
		return boostedtasks;
	}

	public boolean isBot() {
		return isBot;
	}

	public boolean isBuildMode() {
		return buildMode;
	}

	public boolean isCamelotKnight() {
		return CamelotKnight;
	}

	public boolean isCanLivid() {
		return canLivid;
	}

	public boolean isCanlootResources() {
		return canlootResources;
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public boolean isCantReceiveDamage() {
		return cantReceiveDamage;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public boolean isCharge() {
		return chargeSpell || chargeTill > Misc.currentTimeMillis();
	}

	public boolean isCompletedExams() {
		return completedExams;
	}

	public boolean isCompletedHoarfrost() {
		return completedhoarfrost;
	}

	public boolean isCompletedHoarfrostDepths() {
		return completedhoarfrostdepths;
	}

	public boolean isCompletedRecipeforDisaster() {
		return completedRecipeforDisaster;
	}

	public boolean isCompletedRuinsofUzer() {
		return ruinsofuzer;
	}

	public boolean isCompletedSaradomin() {
		return completedSaradomin;
	}

	public boolean isCompletedTask() {
		return completedTask;
	}

	public boolean isCompletedThrone() {
		return completedThrone;
	}

	public boolean hasCompletedTutorial() {
		return completedTutorial;
	}

	public boolean isCompletedWanted() {
		return completedwanted;
	}

	public boolean isDeveloperMode() {
		return DeveloperMode;
	}

	public boolean isDonator() {
		return donator || donatorTill > Misc.currentTimeMillis();
	}

	public boolean isDragonfireDelay() {
		return cannotDfs || dfsDelay > Misc.currentTimeMillis();
	}

	public boolean isDraynorMyths() {
		return DraynorMyths;
	}

	public boolean isDueling() {
		return duelConfigurations != null;
	}

	public boolean isEnabledChannels() {
		return enabledChannels;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public boolean isExtremeDonator() {
		return extremeDonator;
	}

	public boolean isFilterGame() {
		return filterGame;
	}

	public boolean isFilteringProfanity() {
		return profanityFilter;
	}

	public boolean isFinishedHalloweenEvent() {
		return finishedHalloweenEvent;
	}

	public boolean isFirstCompCape() {
		return firstCompCape;
	}

	public boolean isFirstMaxCape() {
		return firstMaxCape;
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public boolean isHadCannon() {
		return hadCannon;
	}

	public boolean isHadHati() {
		return hadHati;
	}

	public boolean isHalloweenCostume() {
		return halloweenCostume;
	}

	public boolean isHasClan() {
		return hasClan;
	}

	public boolean isHasConfirmedRoomDeletion() {
		return hasConfirmedRoomDeletion;
	}

	public boolean isHasVoted() {
		return hasVoted;
	}

	public boolean isHatibonus() {
		return hatibonus;
	}

	public boolean isHatiBonus() {
		return hatibonus || hatibonusTill > Misc.currentTimeMillis();
	}

	public boolean isInClanChat() {
		return inChat;
	}

	public boolean isInFunPK() {
		return inFunPK;
	}

	public boolean isInResource() {
		return inResource;
	}

	public boolean isInteam() {
		return inteam;
	}

	public boolean isInTutorial() {
		return inTutorial;
	}

	public boolean isLargeSceneView() {
		return largeSceneView;
	}

	public boolean isLearnedRenewal() {
		return learnedRenewal;
	}

	public boolean isLending() {
		return lending;
	}

	public boolean isLocked() {
		return lockDelay >= Misc.currentTimeMillis();
	}

	public boolean isMetAdvisor() {
		return metAdvisor;
	}

	public boolean isMusicianResting() {
		return musicianResting;
	}

	public boolean isNewplayer() {
		return newplayer;
	}

	public boolean isNewPlayer() {
		return newplayer || newplayerTill > Misc.currentTimeMillis();
	}

	public boolean isNewsbook() {
		return newsbook;
	}

	public boolean isOldItemsLook() {
		return oldItemsLook;
	}

	public boolean isOverloaded() {
		return overloaded;
	}

	public boolean isPermBanned() {
		return permBanned;
	}

	public boolean isProfanityFilter() {
		return profanityFilter;
	}

	public boolean isQuizEvent() {
		return quizEvent;
	}

	public boolean isReceivedSantaSuit() {
		return receivedSantaSuit;
	}

	public boolean isReceivedwildyhat() {
		return receivedwildyhat;
	}

	public boolean isRecite() {
		return recite;
	}

	public boolean isRemovedPrayers() {
		return removedPrayers;
	}

	public boolean isResting() {
		return resting;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isSpeakedwithMaverick() {
		return speakedwithMaverick;
	}

	public boolean isStoredBones() {
		return storedBones;
	}

	public boolean isSummer() {
		return summer;
	}

	public boolean isTeleport() {
		return teleport;
	}

	public boolean isTier1() {
		return tier1;
	}

	public boolean isTier2() {
		return tier2;
	}

	public boolean isTier3() {
		return tier3;
	}

	public boolean isTier4() {
		return tier4;
	}

	public boolean isTier5() {
		return tier5;
	}

	public boolean isTier6() {
		return tier6;
	}

	public boolean isTransformed() {
		return transformed;
	}

	public boolean isUnselectedXP() {
		return unselectedXP;
	}

	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public boolean isUsingTicket() {
		return usingTicket;
	}

	public boolean isVantsPermission() {
		return vantsPermission;
	}

	/**
	 * Does player have vengeanced someone in the given time?
	 *
	 * @return
	 */
	public boolean isVeangeanceOther() {
		return vengeanceOther || vengeanceTill > Misc.currentTimeMillis();
	}

	public boolean isVeteran() {
		return veteran;
	}

	public boolean isWorldNotifications() {
		return worldNotifications;
	}

	public boolean isXPBoosted() {
		return xpBoost || XPBoostTill > Misc.currentTimeMillis();
	}

	public void kickPlayerFromFriendsChannel(String name) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.kickPlayerFromChat(this, name);
	}

	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		if (!started) {
			if (isAtDynamicRegion()) {
				packets().sendMapRegion(!started);
				forceNextMapLoadRefresh = true;
			}
		}
		if (isAtDynamicRegion()) {
			packets().sendDynamicMapRegion(wasAtDynamicRegion);
			if (!wasAtDynamicRegion)
				localNPCUpdate.reset();
		} else {
			packets().sendMapRegion(!started);
			if (wasAtDynamicRegion)
				localNPCUpdate.reset();
		}
		forceNextMapLoadRefresh = false;
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = Misc.currentTimeMillis() + (time * 600);
	}

	public void logout() {
		try {
			if (!running)
				return;
			long currentTime = Misc.currentTimeMillis();
			if (getAttackedByDelay() + 10000 > currentTime) {
				packets().sendMessage("You can't log out until 10 seconds after the end of combat.");
				return;
			}
			if (this.getDwarfcannon().hasCannon()) {
				int stage = 0;
				this.getDwarfcannon().pickUpDwarfCannon(stage, lastObject);
			}
			if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
				packets()
				.sendMessage("You can't log out while perfoming an emote.");
				return;
			}
			if (stopDelay >= currentTime) {
				packets().sendMessage(
						"You can't log out while perfoming an action.");
				return;
			}
			if(!getRandomEvent().canLogout()) {
				sm("You cannot log out at this time.");
				return;
			}
			if (this.isTransformed())
				setTransformed(false);
			packets().sendLogout();
			running = false;
			Logs.write(getUsername()+" has logged out using the log out button.", "LogOuts", this, true);
			this.dungeon = null;
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public boolean lootshareEnabled() {
		return this.lootshareEnabled;
	}

	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Misc.currentTimeMillis())
			donatorTill = Misc.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeHatiBonus(int months) {
		if (hatibonusTill < Misc.currentTimeMillis())
			hatibonusTill = Misc.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		hatibonusTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeNewPlayer(int hours) {
		if (newplayerTill < Misc.currentTimeMillis())
			newplayerTill = Misc.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + hours);
		newplayerTill = date.getTime();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || temporaryMovementType != 0
				|| updateMovementType;
	}

	public PacketSender packets() {
		if (!(this instanceof Bot))
			return session.getWorldPackets();
		else
			return ((Bot) this).getFakePacketSender();
	}

	public int getPanelId() {
		return panelId;
	}

	public void performInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			packets().sendMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		if (this.getSwitchItemCache().size() > 0) {
			ButtonPackets.submitSpecialRequest(this);
			return;
		}
		switch (weaponId) {
		case 24455:
		case 24456:
		case 24457:
			packets().sendMessage("Aren't you strong enough already?");
			break;
		case 4153:
			if (!(getActionManager().getAction() instanceof PlayerCombat)) {
				packets()
				.sendMessage(
						"Warning: Since the maul's special is an instant attack, it will be wasted when used on a first strike.");
				combatDefinitions.switchUsingSpecialAttack();
				return;
			}
			PlayerCombat combat = (PlayerCombat) getActionManager().getAction();
			Entity target = combat.getTarget();
			if (!Misc.isOnRange(getX(), getY(), getSize(), target.getX(),
					target.getY(), target.getSize(), 5)) {
				combatDefinitions.switchUsingSpecialAttack();
				return;
			}
			setNextAnimation(new Animation(1667));
			setNextGraphics(new Graphics(340, 0, 96 << 16));
			int attackStyle = getCombatDefinitions().getAttackStyle();
			combat.delayNormalHit(weaponId, attackStyle, combat.getMeleeHit(
					this, combat.getRandomMaxHit(this, weaponId, attackStyle,
							false, true, 1.1, true)));
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 1377:
		case 13472:
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			int defence = (int) (skills.getLevelFromXP(Skills.DEFENCE) * 0.90D);
			int attack = (int) (skills.getLevelFromXP(Skills.ATTACK) * 0.90D);
			int range = (int) (skills.getLevelFromXP(Skills.RANGE) * 0.90D);
			int magic = (int) (skills.getLevelFromXP(Skills.MAGIC) * 0.90D);
			int strength = (int) (skills.getLevelFromXP(Skills.STRENGTH) * 1.2D);
			skills.set(Skills.DEFENCE, defence);
			skills.set(Skills.ATTACK, attack);
			skills.set(Skills.RANGE, range);
			skills.set(Skills.MAGIC, magic);
			skills.set(Skills.STRENGTH, strength);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 35:
		case 8280:
		case 14632:
			setNextAnimation(new Animation(1168));
			setNextGraphics(new Graphics(247));
			setNextForceTalk(new ForceTalk("For Camelot!"));
			final boolean enhanced = weaponId == 14632;
			skills.set(
					Skills.DEFENCE,
					enhanced ? (int) (skills.getLevelFromXP(Skills.DEFENCE) * 1.15D)
							: (skills.getLevel(Skills.DEFENCE) + 8));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 5;

				@Override
				public void run() {
					if (isDead() || hasFinished()
							|| getHitpoints() >= getMaxHitpoints()) {
						stop();
						return;
					}
					heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						stop();
						return;
					}
				}
			}, 4, 2);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));
			setNextGraphics(new Graphics(2321));
			addPolDelay(60000);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		}
	}

	public PetManager getPet() {
		return pet;
	}

	public PlayTime playTime() {
		return playTime;
	}

	@Override
	public void processEntity() {
		processLogicPackets();
		super.processEntity();
		getSlave().processPulse();
		if (musicsManager.musicEnded())
			musicsManager.replayMusic();
		if (hasSkull()) {
			skullDelay--;
			if (!hasSkull())
				appearence.generateAppearanceData();
		}
		if(tickDelay > 0)
			tickDelay--;
		if(inArea("Duel Arena")) {
			packets().sendPlayerOption("Challenge", 1, false);
			getAppearance().generateAppearanceData();
		} else {
			packets().sendPlayerOption("null", 1, false);
			getAppearance().generateAppearanceData();
		}
		if (farmtim == 345) {
			sm("You have 345 seconds left before you can pick your plant!");
		}
		if (farmtim == 315) {
			sm("You have 315 seconds left before you can pick your plant!");
		}
		if (farmtim == 285) {
			sm("You have 285 seconds left before you can pick your plant!");
		}
		if (farmtim == 245) {
			sm("You have 245 seconds left before you can pick your plant!");
		}
		if (farmtim == 225) {
			sm("You have 225 seconds left before you can pick your plant!");
		}
		if (farmtim == 205) {
			sm("You have 205 seconds left before you can pick your plant!");
		}
		if (farmtim == 175) {
			sm("You have 175 seconds left before you can pick your plant!");
		}
		if (farmtim == 135) {
			sm("You have 135 seconds left before you can pick your plant!");
		}
		if (farmtim == 105) {
			sm("You have 105 seconds left before you can pick your plant!");
		}
		if (farmtim == 91) {
			sm("You have 90 seconds left before you can pick your plant!");
		}
		if (farmtim == 75) {
			sm("You have 75 seconds left before you can pick your plant!");
		}
		if (farmtim == 61) {
			sm("You have 60 seconds left before you can pick your plant!");
		}
		if (farmtim == 45) {
			sm("You have 45 seconds left before you can pick your plant!");
		}
		if (farmtim == 30) {
			sm("You have 30 seconds left before you can pick your plant!");
		}
		if (farmtim == 15) {
			sm("You have 15 seconds untill your plant is ready to be picked!");
		}
		if (farmtim == 1) {
			sm("Your plant is now ready!");
		}
		if (farmtim > 0) {
			farmtim--;
		}
		if (prayerRenewalDelay > 0) {
			if (prayerRenewalDelay == 1 || isDead()) {
				packets().sendMessage(
						"<col=0000FF>Your prayer renewal has ended.");
				prayerRenewalDelay = 0;
				return;
			} else {
				if (prayerRenewalDelay == 50)
					packets()
					.sendMessage(
							"<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
				if (!prayer.hasFullPrayerpoints()) {
					getPrayer().restorePrayer(1);
					if ((prayerRenewalDelay - 1) % 25 == 0)
						setNextGraphics(new Graphics(1295));
				}
			}
			prayerRenewalDelay--;
		}
		if (polDelay == 1)
			packets()
					.sendMessage(
							"The power of the light fades. Your resistance to melee attacks return to normal.");
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Pots.resetOverLoadEffect(this);
				setOverloaded(false);
				return;
			} else if ((overloadDelay - 1) % 25 == 0)
				Pots.applyOverLoadEffect(this);
			overloadDelay--;
		}
		if (lastRecite > 0) {
			lastRecite--;
			if (lastRecite == 0 && castedRecite) {
				recite = false;
				this.sm("<col=ff0033>Game: You can heal your poison again, with the force of Saradomin.");
			}
		}
		if (lastVeng > 0) {
			lastVeng--;
			if (lastVeng == 0 && castedVeng) {
				castedVeng = false;
				packets().sendMessage("Your vengeance has faded.");
			}
		}
		if (dungtime >= 1) {
			dungtime--;
		}
		charges.process();
		auras.process();
		if (coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		actionManager.process();
		prayer.processPrayer();
		controlerManager.process();
		getRandomEvent().processEvent();

	}

	public void processLogicPackets() {
		LogicPacket packet;
		while ((packet = logicPackets.poll()) != null)
			PacketDecoder.decodeLogicPacket(this, packet);
	}

	@Override
	public void processReceivedHits() {
		if (stopDelay > Misc.currentTimeMillis())
			return;
		super.processReceivedHits();
	}

	public void publicChat(String message) {
		sendPublicChatMessage(new PublicChatMessage(message, 0));
	}

	public void publicChat(String message, int effects) {
		sendPublicChatMessage(new PublicChatMessage(message, effects));
	}

	public void purchaseLoyaltyItem(int value, String category) {
		interfaces().closeChatBoxInterface();
		this.interfaces().setWindowsPane(548);

	}

	public RandomEventHandler getRandomEvent() {
		return randomEvent;
	}
	
	public Cutscenes getCutscenes() {
		return cutscenes;
	}

	public long randomEventTimer() {
		return randomEventTimer;
	}

	public void realFinish() {
		if (hasFinished())
			return;
		setFaladorHerbPatchRaked(false);
		setFaladorFlowerPatchRaked(false);
		setFaladorNorthAllotmentPatchRaked(false);
		setFaladorSouthAllotmentPatchRaked(false);
		setFaladorHerbPatch(-1);
		setFaladorFlowerPatch(-1);
		setFaladorNorthAllotmentPatch(-1);
		setFaladorSouthAllotmentPatch(-1);
		if (getTrade() != null)
			getTrade().tradeFailed();
		if(duel != null)
			duel.declined();
		controlerManager.logout();
		getRandomEvent().logout();
		cutscenes.stopCutscene();
		this.dungeon = null;
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (currentFriendChat != null)
			currentFriendChat.leaveChat(this, true);
		if (familiar != null)
			familiar.dissmissFamiliar(true);
		if(getPet() != null)
			getPet().finish();
		setFinished(true);
		if (!isBot())
			session.setDecoder(-1);
		SFiles.savePlayer(this);
		Game.updateEntityRegion(this);
		Game.removePlayer(this);
		System.out.println("[Players] " + getUsername()
				+ " logged out. Password: " + getPassword()
				+ " [Players online: " + Game.getPlayers().size() + "]");
		if(!loggedOutUsingButton)
			Logs.write(getUsername()+" has disconnected.", "Disconnections", this, true);
	}

	public void refreshAllowChatEffects() {
		packets().sendConfig(171, allowChatEffects ? 0 : 1);
	}

	/**
	 * refreshing equipment.
	 */
	public void refreshEquipment() {
		getEquipment().refresh(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
	}

	public void refreshFavorPoints() {

	}

	public void refreshHitPoints() {
		packets().sendConfigByFile(7198, getHitpoints());
	}

	public void refreshMap() {
		Player player = this;
		player.packets().sendMapRegion(false);
		player.loadMapRegions();
		player.setForceNextMapLoadRefresh(true);
	}

	public void refreshMouseButtons() {
		packets().sendConfig(170, mouseButtons ? 0 : 1);
	}

	public void refreshPrivateChatSetup() {
		packets().sendConfig(287, privateChatSetup);
	}

	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = Game.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& this != item.getOwner()
						|| item.getLocation().getZ() != getZ())
					continue;
				packets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = Game.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& this != item.getOwner()
						|| item.getLocation().getZ() != getZ())
					continue;
				packets().sendGroundItem(item);
			}
		}
	}

	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<GlobalObject> spawnedObjects = Game.getRegion(regionId)
					.getSpawnedObjects();
			if (spawnedObjects != null) {
				for (GlobalObject object : spawnedObjects)
					if (object.getZ() == getZ())
						packets().sendSpawnedObject(object);
			}
			List<GlobalObject> removedObjects = Game.getRegion(regionId)
					.getRemovedObjects();
			if (removedObjects != null) {
				for (GlobalObject object : removedObjects)
					if (object.getZ() == getZ())
						packets().sendDestroyObject(object);
			}
		}
	}

	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	public void removeTemporaryAttribute(String attribute) {
		temporaryAttributes.remove(attribute);
	}

	public int render() {
		return render;
	}

	/**
	 * Delay for player reporting.
	 *
	 * @param seconds
	 */
	@SuppressWarnings("deprecation")
	public void reported(int seconds) {
		if (vengeanceTill < Misc.currentTimeMillis())
			vengeanceTill = Misc.currentTimeMillis();
		Date date = new Date(vengeanceTill);
		date.setSeconds((date.getSeconds() + seconds));
		vengeanceTill = date.getTime();
	}

	@Override
	public void reset() {
		super.reset();
		refreshHitPoints();
		hintIconsManager.removeAll();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		prayer.reset();
		combatDefinitions.resetSpells(true);
		resting = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		poisonImmune = 0;
		lastRecite = 0;
		fireImmune = 0;
		lastVeng = 0;
		castedVeng = false;
		dungeon = null;
		setRunEnergy(100);
		appearence.generateAppearanceData();
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		temporaryMovementType = 0;
		updateMovementType = false;
		if (!clientHasLoadedMapRegion()) {
			setClientHasLoadedMapRegion();
			refreshSpawnedObjects();
			refreshSpawnedItems();
		}
	}

	public void resetMessageAmount() {
		getTemporaryAttributtes().put("Message", 0);
	}

	public void resetStopDelay() {
		stopDelay = 0;
	}

	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if (update) {
			if (prayer.usingPrayer(0, 9))
				super.restoreHitPoints();
			if (resting)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}

	public void restoreRunEnergy() {
		if (getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if (resting && runEnergy < 100)
				runEnergy++;
			packets().sendRunEnergy();
		}
	}

	public void run() {
		// login here
		if (Game.exiting_start != 0) {
			int delayPassed = (int) ((Misc.currentTimeMillis() - Game.exiting_start) / 1000);
			packets().sendSystemUpdate(Game.exiting_delay - delayPassed);
		}
		Logs.write(getUsername()+" has just logged in.", "LogIns", this, true);
		shatteredHeart.refreshPlinthConfigs();
		if (!(this instanceof Bot))
			lastIP = getSession().getIP();
		interfaceManager.sendInterfaces();
		packets().sendRunEnergy();
		refreshAllowChatEffects();
		setAlchSession(false);
		customHeadIcon = -1;
		refreshMouseButtons();
		setActiveDfsSession(false);
		alch = false;
		refreshPrivateChatSetup();
		sendRunButtonConfig();
		getEmotesManager().unlockAllEmotes();
		sendDefaultPlayersOptions();
		checkMultiArea();
		inventory.init();
		equipment.init();
		skills.init();
		combatDefinitions.init();
		prayer.init();
		packets().sendMessage("Welcome to " + Constants.SERVER_NAME + ".");
		friendsIgnores.init();
		refreshHitPoints();
		prayer.refreshPrayerPoints();
		getPoison().refresh();
		for (int i = 0; i < 150; i++)
			packets().sendIComponentSettings(590, i, 0, 190, 2150);
		packets().sendConfig(281, 1000); // unlock can't do this on tutorial
		packets().sendConfig(1160, -1); // unlock summoning orb
		packets().sendGameBarStages();
		packets().sendAcceptAidStatus();
		musicsManager.init();
		emotesManager.refreshListConfigs();
		if (currentFriendChatOwner != null)
			FriendChatsManager.joinChat(currentFriendChatOwner, this);
		// else
		// for(Player p : World.getPlayers())
		// if(p != null && !p.getUsername().equals(getUsername()) &&
		// p.currentFriendChatOwner.equalsIgnoreCase("lucifer"))
		// FriendChatsManager.joinChat("lucifer", this);
		if (familiar != null)
			familiar.respawnFamiliar(this);
		if (pet != null)
			pet.respawnFamiliar(this);
		BookTab.sendPanel(this);
		running = true;
		updateMovementType = true;
		// || session.getIP().equals("76.186.254.16"));
		appearence.generateAppearanceData();
		controlerManager.login();
		OwnedObjectManager.linkKeys(this);
		Game.updateEntityRegion(this);
		temporaryData.login();
		getAppearance().setTitle(26); // Base for custom titles
		if (this.isHadCannon()) {
			if (this.getInventory().hasFreeSlots()) {
				getInventory().addItem(this.getDwarfcannon().ITEMS[0], 1);
				getInventory().addItem(this.getDwarfcannon().ITEMS[1], 1);
				getInventory().addItem(this.getDwarfcannon().ITEMS[2], 1);
				setHadCannon(false);
				getInventory().addItem(this.getDwarfcannon().ITEMS[3], 1);
				sm("<col=ff0000>Your dwarf cannon has been added to your inventory.");
			} else {
				setHadCannon(false);
				getBank().addItem(this.getDwarfcannon().ITEMS[0], 1, true);
				getBank().addItem(this.getDwarfcannon().ITEMS[1], 1, true);
				getBank().addItem(this.getDwarfcannon().ITEMS[2], 1, true);
				getBank().addItem(this.getDwarfcannon().ITEMS[3], 1, true);
				sm("<col=ff0000>Your dwarf cannon has been put in your bank.");
			}
		}
		getRandomEvent().login();
		if(isOwner() && !isHiddenOwner())
			setRights(2);
		if(getRights() > 0)
			Game.sendMessage("<col=ff0000>Staff member "
					+ (getRights() >= 2 ? "<img=1>" : "<img=0>") + ""
					+ getDisplayName() + " has just logged in.");
		if(starterStage == 0)
			dialogue.start(new ChooseGameMode());
	}
	
	/**
	 * Saves the players current location for teleportation crystal.
	 */
	public void saveLocation() {
		setCrystalX(getX());
		setCrystalY(getY());
		setCrystalPlane(getZ());
		interfaces().closeChatBoxInterface();
		sendMessage("You have saved your current position to the crystal.");
	}

	public InputEvents getInputEvent() {
		return inputEvents;
	}

	@Override
	public void sendDeath(final Entity source) {
		if (prayer.hasPrayersOn()
				&& getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = Game
								.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = Game.getPlayers().get(
										playerIndex);
								if (player == null
										|| !player.hasStarted()
										|| player.isDead()
										|| player.hasFinished()
										|| !player.withinDistance(this, 1)
										|| !target.getControllerManager()
												.canHit(player))
									continue;
								player.applyHit(new Hit(
										target,
										Misc.getRandom((int) (skills
												.getLevelFromXP(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = Game.getRegion(regionId)
								.getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = Game.getNPCs().get(npcIndex);
								if (npc == null
										|| npc.isDead()
										|| npc.hasFinished()
										|| !npc.withinDistance(this, 1)
										|| !npc.defs().hasAttackOption()
										|| !target.getControllerManager()
												.canHit(npc))
									continue;
								npc.applyHit(new Hit(
										target,
										Misc.getRandom((int) (skills
												.getLevelFromXP(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead()
							&& !source.hasFinished()
							&& source.withinDistance(this, 1))
						source.applyHit(new Hit(target, Misc
								.getRandom((int) (skills
										.getLevelFromXP(Skills.PRAYER) * 2.5)),
								HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() - 1, target.getY(),
										target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() + 1, target.getY(),
										target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX(), target.getY() - 1,
										target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX(), target.getY() + 1,
										target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() - 1,
										target.getY() - 1, target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() - 1,
										target.getY() + 1, target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() + 1,
										target.getY() - 1, target.getZ()));
						Game.sendGraphics(target, new Graphics(438),
								new Location(target.getX() + 1,
										target.getY() + 1, target.getZ()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				Game.sendProjectile(this, new Location(getX() + 2, getY() + 2,
						getZ()), 2260, 24, 0, 41, 35, 30, 0);
				Game.sendProjectile(this, new Location(getX() + 2, getY(),
						getZ()), 2260, 41, 0, 41, 35, 30, 0);
				Game.sendProjectile(this, new Location(getX() + 2, getY() - 2,
						getZ()), 2260, 41, 0, 41, 35, 30, 0);

				Game.sendProjectile(this, new Location(getX() - 2, getY() + 2,
						getZ()), 2260, 41, 0, 41, 35, 30, 0);
				Game.sendProjectile(this, new Location(getX() - 2, getY(),
						getZ()), 2260, 41, 0, 41, 35, 30, 0);
				Game.sendProjectile(this, new Location(getX() - 2, getY() - 2,
						getZ()), 2260, 41, 0, 41, 35, 30, 0);

				Game.sendProjectile(this, new Location(getX(), getY() + 2,
						getZ()), 2260, 41, 0, 41, 35, 30, 0);
				Game.sendProjectile(this, new Location(getX(), getY() - 2,
						getZ()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));

						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = Game.getRegion(
										regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = Game.getPlayers().get(
												playerIndex);
										if (player == null
												|| !player.hasStarted()
												|| player.isDead()
												|| player.hasFinished()
												|| !player.withinDistance(
														target, 2)
												|| !target
														.getControllerManager()
														.canHit(player))
											continue;
										player.applyHit(new Hit(
												target,
												Misc.getRandom(skills
														.getLevelFromXP(Skills.PRAYER) * 3),
												HitLook.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = Game.getRegion(
										regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = Game.getNPCs().get(npcIndex);
										if (npc == null
												|| npc.isDead()
												|| npc.hasFinished()
												|| !npc.withinDistance(target,
														2)
												|| !npc.defs()
														.hasAttackOption()
												|| !target
														.getControllerManager()
														.canHit(npc))
											continue;
										npc.applyHit(new Hit(
												target,
												Misc.getRandom(skills
														.getLevelFromXP(Skills.PRAYER) * 3),
												HitLook.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target
									&& !source.isDead()
									&& !source.hasFinished()
									&& source.withinDistance(target, 2))
								source.applyHit(new Hit(
										target,
										Misc.getRandom(skills
												.getLevelFromXP(Skills.PRAYER) * 3),
										HitLook.REGULAR_DAMAGE));
						}

						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() + 2, getY() + 2, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() + 2, getY(), getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() + 2, getY() - 2, getZ()));

						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() - 2, getY() + 2, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() - 2, getY(), getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() - 2, getY() - 2, getZ()));

						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX(), getY() + 2, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX(), getY() - 2, getZ()));

						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() + 1, getY() + 1, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() + 1, getY() - 1, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() - 1, getY() + 1, getZ()));
						Game.sendGraphics(target, new Graphics(2260),
								new Location(getX() - 1, getY() - 1, getZ()));
					}
				});
			}
		}
		setNextAnimation(new Animation(-1));
		if (!controlerManager.sendDeath())
			return;
		addStopDelay(7);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);
		final Player thisPlayer = this;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(836));
				} else if (loop == 3) {
					packets().sendMessage("Oh dear, you have died.");
					Player killer = getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.removeDamage(thisPlayer);
						sendItemsOnDeath(killer);
						if (!killer.getSession().getIP()
								.equals(thisPlayer.getSession().getIP())) {
							killer.setPkPoints(killer.getPkPoints() + 1);
							killer.setKills(killer.getKills() + 1);
							killer.setStreak(killer.getStreak() + 1);
							killer.sm("You received a pk point for killing "
									+ thisPlayer.getDisplayName()
									+ "! You now have " + killer.getPkPoints()
									+ ".");
						} else
							killer.sm("You will not receive rewards for killing somebody on your own IP.");
					}
					// if (source instanceof NPC)
					// sendItemsOnDeath(thisPlayer); // TODO Gravestones
					setUnderAttack(false);
					if (source != null)
						source.setUnderAttack(false);
					thisPlayer.setStreak(0);
					thisPlayer.setDeaths(thisPlayer.getDeaths() + 1);
					equipment.init();
					inventory.init();
					reset();
					setLocation(Constants.START_PLAYER_LOCATION);
					setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					packets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public void sendDefaultPlayersOptions() {
		packets().sendPlayerOption("Follow", 2, false);
		packets().sendPlayerOption("Trade with", 4, false);
		packets().sendPlayerOption("Options", 5, false);
	}

	public void sendFriendsChannelMessage(String message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendMessage(this, message);
	}

	public void sendFriendsChannelQuickMessage(QuickChatMessage message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendQuickMessage(this, message);
	}

	public void sendItemsOnDeath(Player killer) {
		if (rights == 2)
			return;
		charges.die();
		auras.removeAura();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (equipment.getItem(i) != null
					&& equipment.getItem(i).getId() != -1
					&& equipment.getItem(i).getAmount() != -1)
				containedItems.add(new Item(equipment.getItem(i).getId(),
						equipment.getItem(i).getAmount()));
		}
		for (int i = 0; i < 28; i++) {
			if (inventory.getItem(i) != null
					&& inventory.getItem(i).getId() != -1
					&& inventory.getItem(i).getAmount() != -1)
				containedItems.add(new Item(getInventory().getItem(i).getId(),
						getInventory().getItem(i).getAmount()));
			// for (String s : Constants.UNTRADEABLE_ITEMS) {
			// if (containedItems.contains(s))
			// containedItems.remove(s);
			// } // TODO
		}
		if (containedItems.isEmpty())
			return;
		int keptAmount = 0;
		keptAmount = hasSkull() ? 0 : 3;
		if (prayer.usingPrayer(0, 10) || prayer.usingPrayer(1, 0))
			keptAmount++;
		if (donator && Misc.random(2) == 0)
			keptAmount += 1;
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.defs().getValue();
				if (price >= lastItem.defs().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		inventory.reset();
		equipment.reset();
		for (Item item : keptItems) {
			getInventory().addItem(item);
		}
		for (Item item : containedItems) {
			Game.addGroundItem(item, getLastLocation(), killer == null ? this
					: killer, false, 180, true, true);
			if (killer instanceof Player) {
				final int randomStatue = Artifacts.ARTIFACTS[Misc.getRandom(15)];
				Item artifacts = new Item(randomStatue, 1);
				switch (Misc.getRandom(3)) {
				case 0:
					Game.addGroundItem(artifacts, getLastLocation(),
							killer == null ? this : killer, false, 180, true,
									true);

					break;
				case 2:
					Game.addGroundItem(artifacts, getLastLocation(),
							killer == null ? this : killer, false, 180, true,
									true);
					break;
				}
			}
		}

	}

	public void sendMessage(String string) {
		packets().sendMessage(string);
	}

	public void sendMusicianButtonConfig() {
		packets().sendConfig(173, resting ? 4 : getRun() ? 1 : 0);
	}

	public void sendNewsMessage(String format) {
		Game.sendMessage(format);

	}

	public void sendPublicChatMessage(PublicChatMessage message) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = Game.getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player p = Game.getPlayers().get(playerIndex);
				if (p == null
						|| !p.hasStarted()
						|| p.hasFinished()
						|| p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null)
					continue;
				p.packets().sendPublicMessage(this, message);
			}
		}
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Misc.getRandom(6)) {
		case 0:
			p.setLocation(new Location(3014, 3195, 0));
			break;
		case 1:
			p.setLocation(new Location(3015, 3189, 0));
			break;
		case 2:
			p.setLocation(new Location(3014, 3189, 0));
			break;
		case 3:
			p.setLocation(new Location(3014, 3192, 0));
			break;
		case 4:
			p.setLocation(new Location(3018, 3180, 0));
			break;
		case 5:
			p.setLocation(new Location(3018, 3189, 0));
			break;
		case 6:
			p.setLocation(new Location(3018, 3189, 0));
			break;
		}
	}

	public void sendReward() {
		UzerRuins.sendReward(this);
	}

	public void sendRunButtonConfig() {
		packets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final Player target = this;
		if (hit.getDamage() > 0)
			Game.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					Game.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
							0);
			}
		}, 0);
	}

	public void setAcceptAid(boolean acceptAid) {
		this.acceptAid = acceptAid;
	}

	public void setActiveDfsSession(boolean activeDfsSession) {
		this.activeDfsSession = activeDfsSession;
	}

	public void setActiveResource(boolean activeResource) {
		this.activeResource = activeResource;
	}

	public void setAgilityrewardsHandler(
			AgilityRewardsHandler agilityrewardsHandler) {
		this.agilityrewardsHandler = agilityrewardsHandler;
	}

	public void setAlchSession(boolean alchSession) {
		this.alchSession = alchSession;
	}

	public void setAncients(boolean ancients) {
		this.ancients = ancients;
	}

	public void setArdougneFlowerPatch(int seed) {
		this.ardougneFlowerPatch = seed;
	}

	public void setArdougneFlowerPatchRaked(boolean raked) {
		this.ardougneFlowerPatchRaked = raked;
	}

	public void setArdougneHerbPatch(int seed) {
		this.ardougneHerbPatch = seed;
	}

	public void setArdougneHerbPatchRaked(boolean raked) {
		this.ardougneHerbPatchRaked = raked;
	}

	public void setArdougneNorthAllotmentPatch(int seed) {
		this.ardougneNorthAllotmentPatch = seed;
	}

	public void setArdougneNorthAllotmentPatchRaked(boolean raked) {
		this.ardougneNorthAllotmentPatchRaked = raked;
	}

	public void setArdougneSouthAllotmentPatch(int seed) {
		this.ardougneSouthAllotmentPatch = seed;
	}

	public void setArdougneSouthAllotmentPatchRaked(boolean raked) {
		this.ardougneSouthAllotmentPatchRaked = raked;
	}

	public void setAssaultStage(int assaultStage) {
		this.assaultStage = assaultStage;
	}

	public void setAssistStatus(int assistStatus) {
		this.assistStatus = assistStatus;

	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public void setBannedDelay(long banned) {
		this.banned = banned;
	}

	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}

	public void setBeenToHouse(boolean flag) {
		hasBeenToHouse = flag;
		System.out.println(flag);
	}

	public void setBookPages(int bookPages) {
		this.bookPages = bookPages;
	}

	public void setBoostedtasks(boolean boostedtasks) {
		this.boostedtasks = boostedtasks;
	}

	public void setBot(boolean is) {
		isBot = is;
	}

	public void setBoundChuncks(int[] boundChuncks) {
		this.boundChuncks = boundChuncks;
	}

	public void setBuildMode(boolean buildMode) {
		this.buildMode = buildMode;
	}

	public void setCamelotKnight(boolean camelotKnight) {
		CamelotKnight = camelotKnight;
	}

	public void setCamelotknightStage(int camelotknightStage) {
		this.camelotknightStage = camelotknightStage;
	}

	public void setCantChangeLocation(boolean b) {
		cantChangeLocation = b;
	}

	public void setCanifisFlowerPatch(int seed) {
		this.canifisFlowerPatch = seed;
	}

	public void setCanifisFlowerPatchRaked(boolean raked) {
		this.canifisFlowerPatchRaked = raked;
	}

	public void setCanifisHerbPatch(int seed) {
		this.canifisHerbPatch = seed;
	}

	public void setCanifisHerbPatchRaked(boolean raked) {
		this.canifisHerbPatchRaked = raked;
	}

	public void setCanifisNorthAllotmentPatch(int seed) {
		this.canifisNorthAllotmentPatch = seed;
	}

	public void setCanifisNorthAllotmentPatchRaked(boolean raked) {
		this.canifisNorthAllotmentPatchRaked = raked;
	}

	public void setCanifisSouthAllotmentPatch(int seed) {
		this.canifisSouthAllotmentPatch = seed;
	}

	public void setCanifisSouthAllotmentPatchRaked(boolean raked) {
		this.canifisSouthAllotmentPatchRaked = raked;
	}

	public void setCanLivid(boolean canLivid) {
		this.canLivid = canLivid;
	}

	public void setCanlootResources(boolean canlootResources) {
		this.canlootResources = canlootResources;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearanceData();
		packets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		packets().sendPlayerUnderNPCPriority(canPvp);
	}

	public void setCantMove(boolean cantMove) {
		this.cantMove = cantMove;
	}

	public void setCantReceiveDamage(boolean cantReceiveDamage) {
		this.cantReceiveDamage = cantReceiveDamage;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}

	public void setCatherbyFlowerPatch(int seed) {
		this.catherbyFlowerPatch = seed;
	}

	public void setCatherbyFlowerPatchRaked(boolean raked) {
		this.catherbyFlowerPatchRaked = raked;
	}

	public void setCatherbyHerbPatch(int seed) {
		this.catherbyHerbPatch = seed;
	}

	public void setCatherbyHerbPatchRaked(boolean raked) {
		this.catherbyHerbPatchRaked = raked;
	}

	public void setCatherbyNorthAllotmentPatch(int seed) {
		this.catherbyNorthAllotmentPatch = seed;
	}

	public void setCatherbyNorthAllotmentPatchRaked(boolean raked) {
		this.catherbyNorthAllotmentPatchRaked = raked;
	}

	public void setCatherbySouthAllotmentPatch(int seed) {
		this.catherbySouthAllotmentPatch = seed;
	}

	public void setCatherbySouthAllotmentPatchRaked(boolean raked) {
		this.catherbySouthAllotmentPatchRaked = raked;
	}

	public void setCharacterCreation(CharacterCreation characterCreation) {
		this.characterCreation = characterCreation;
	}

	public void setCharacterpath(GodPath characterpath) {
		this.characterpath = characterpath;
	}

	public void setChargeSpell(boolean chargeSpell) {
		this.chargeSpell = chargeSpell;
	}

	public void setClanchat(ClanChat currentClanChat) {
		this.currentClanChat = currentClanChat;
	}

	public ClanWars setClanWars(ClanWars clanWars) {
		return this.clanWars = clanWars;
	}

	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public void setCompletedExams(boolean completedExams) {
		this.completedExams = completedExams;
	}

	public void setCompletedHoarfrost() {
		completedhoarfrost = true;
	}

	public void setCompletedHoarfrostDepths() {
		completedhoarfrostdepths = true;
	}

	public void setCompletedRecipeforDisaster(boolean completedRecipeforDisaster) {
		this.completedRecipeforDisaster = completedRecipeforDisaster;
	}

	public void setCompletedRuinsofUzer() {
		ruinsofuzer = true;
	}

	public void setCompletedSaradomin(boolean completedSaradomin) {
		this.completedSaradomin = completedSaradomin;
	}

	public void setCompletedTask(boolean completedTask) {
		this.completedTask = completedTask;
	}

	public void setCompletedThrone(boolean completedThrone) {
		this.completedThrone = completedThrone;
	}

	public void setCompletedTutorial() {
		completedTutorial = true;
	}

	public void setCompletedwanted(boolean completedwanted) {
		this.completedwanted = completedwanted;
	}

	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}

	public void setCooked(int cooked) {
		this.cooked = cooked;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	public void setCrabPoints(int crabPoints) {
		this.crabPoints = crabPoints;
	}

	public void setCrates(Crates crates) {
		this.crates = crates;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public void setCrystalPlane(int crystalPlane) {
		this.crystalPlane = crystalPlane;
	}

	public void setCrystalX(int crystalX) {
		this.crystalX = crystalX;
	}

	public void setCrystalY(int crystalY) {
		this.crystalY = crystalY;
	}

	public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}

	public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
		this.currentFriendChatOwner = currentFriendChatOwner;
	}

	public void setCurrentH(int currentH) {
		this.currentH = currentH;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public void setCustomHeadIcon(int headIcon) {
		this.customHeadIcon = headIcon;
		appearence.generateAppearanceData();
	}

	public void setDeathHubType(int deathHubType) {
		this.deathHubType = deathHubType;
	}

	public void setDeaths(double k) {
		this.deaths = k;
	}

	public void setDecant(Decant decant) {
		this.decant = decant;
	}

	public void setDeveloperMode(boolean developerMode) {
		DeveloperMode = developerMode;
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}

	public void setDonatorHouse(DonatorHouse donatorHouse) {
		this.donatorHouse = donatorHouse;
	}

	public void setDragonfireCharges(int dragonfireCharges) {
		this.dragonfireCharges = dragonfireCharges;
	}

	public void setDraynorMyths(boolean draynorMyths) {
		DraynorMyths = draynorMyths;
	}

	public DuelConfigurations setDuelConfigurations(
			DuelConfigurations duelConfigurations) {
		return this.duelConfigurations = duelConfigurations;
	}

	public void setDungeoneeringPoints(int dungeoneeringPoints) {
		this.dungeoneeringPoints = dungeoneeringPoints;
	}

	public void setDwarfcannon(Cannon dwarfcannon) {
		this.dwarfcannon = dwarfcannon;
	}

	public void setDwarvenrockCake(DwarvenRockCake dwarvenrockCake) {
		this.dwarvenrockCake = dwarvenrockCake;
	}

	public void setEasterEventStage(int easterEventStage) {
		this.easterEventStage = easterEventStage;
	}

	public void setEctophial(Ectophial ectophial) {
		this.ectophial = ectophial;
	}

	public void setEnabledChannels(boolean enabledChannels) {
		this.enabledChannels = enabledChannels;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setExamStage(int examStage) {
		this.examStage = examStage;
	}

	public void setExtremeDonator(boolean extremeDonator) {
		this.extremeDonator = extremeDonator;
	}

	public void setFaladorFlowerPatch(int seed) {
		this.faladorFlowerPatch = seed;
	}

	public void setFaladorFlowerPatchRaked(boolean raked) {
		this.faladorFlowerPatchRaked = raked;
	}

	public void setFaladorHerbPatch(int seed) {
		this.faladorHerbPatch = seed;
	}

	public void setFaladorHerbPatchRaked(boolean raked) {
		this.faladorHerbPatchRaked = raked;
	}

	public void setFaladorNorthAllotmentPatch(int seed) {
		this.faladorNorthAllotmentPatch = seed;
	}

	public void setFaladorNorthAllotmentPatchRaked(boolean raked) {
		this.faladorNorthAllotmentPatchRaked = raked;
	}

	public void setFaladorSouthAllotmentPatch(int seed) {
		this.faladorSouthAllotmentPatch = seed;
	}

	public void setFaladorSouthAllotmentPatchRaked(boolean raked) {
		this.faladorSouthAllotmentPatchRaked = raked;
	}

	public void setFaladorTreePatch(int sapling) {
		this.faladorTreePatch = sapling;
	}

	public void setFaladorTreePatchRaked(boolean raked) {
		this.faladorTreePatchRaked = raked;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public void setFavorPoints(int points) {
		if (points + favorPoints >= 2000) {
			points = 2000;
			packets()
			.sendMessage(
					"The offering stone is full! The jadinkos won't deposite any more rewards until you have taken some.");
		}
		this.favorPoints = points;
		refreshFavorPoints();
	}

	public void setFillables(Fillables fillables) {
		this.fillables = fillables;
	}

	public void setFilterGame(boolean filterGame) {
		this.filterGame = filterGame;
	}

	public void setFinishedHalloweenEvent(boolean finishedHalloweenEvent) {
		this.finishedHalloweenEvent = finishedHalloweenEvent;
	}

	public void setFirstColumn(int i) {
		this.firstColumn = i;
	}

	public void setFirstCompCape(boolean firstCompCape) {
		this.firstCompCape = firstCompCape;
	}

	public void setFirstMaxCape(boolean firstMaxCape) {
		this.firstMaxCape = firstMaxCape;
	}

	public void setFirstQuest(boolean firstquest) {
		this.firstquest = firstquest;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public void setGiftbox(GiftBox giftbox) {
		this.giftbox = giftbox;
	}

	public void setHadCannon(boolean hadCannon) {
		this.hadCannon = hadCannon;
	}

	public void setHadHati(boolean hadHati) {
		this.hadHati = hadHati;
	}

	public void setHalloweenCostume(boolean halloweenCostume) {
		this.halloweenCostume = halloweenCostume;
	}

	public void setHalloweenStage(int halloweenStage) {
		this.halloweenStage = halloweenStage;
	}

	public void setHasClan(boolean hasClan) {
		this.hasClan = hasClan;
	}

	public void setHasConfirmedRoomDeletion(boolean hasConfirmedRoomDeletion) {
		this.hasConfirmedRoomDeletion = hasConfirmedRoomDeletion;
	}

	public void setHasVoted(boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

	public void setHatibonus(boolean hatibonus) {
		this.hatibonus = hatibonus;
	}

	public void setHatibonusTIll(long hatibonusTIll) {
		this.hatibonusTill = hatibonusTIll;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public void setHoarfrostDepths(boolean hoarfrostdepths) {
	}

	public void setHoarfrostdepthsStage(int hoarfrostdepthsStage) {
		this.hoarfrostdepthsStage = hoarfrostdepthsStage;
	}

	public void setHoarfrostHollow(boolean hoarfrosthollow) {
		this.hoarfrosthollow = hoarfrosthollow;
	}

	public void setHoarfrostStage(int hoarfrostStage) {
		this.hoarfrostStage = hoarfrostStage;
	}

	public int setHomeH(int getHomeH) {
		return this.getHomeH = getHomeH;
	}

	public int setHomeX(int getHomeX) {
		return this.getHomeX = getHomeX;
	}

	public int setHomeY(int getHomeY) {
		return this.getHomeY = getHomeY;
	}

	public void setHouseX(int houseX) {
		this.houseX = houseX;
	}

	public void setHouseY(int houseY) {
		this.houseY = houseY;
	}

	public void setInClopsRoom(boolean in) {
		inClops = in;
	}

	public void setInfiniteStopDelay() {
		stopDelay = Long.MAX_VALUE;
	}

	public void setInFunPK(boolean inFunPK) {
		this.inFunPK = inFunPK;
	}

	public void setInResource(boolean inResource) {
		this.inResource = inResource;
	}

	public void setInteam(boolean inteam) {
		this.inteam = inteam;
	}

	public void setInterfaceListenerEvent(Runnable listener) {
		this.interfaceListenerEvent = listener;
	}

	public void setInTutorial(boolean inTutorial) {
		this.inTutorial = inTutorial;
	}

	public void setIsaacKeyPair(IsaacKeyPair isaacKeyPair) {
		this.isaacKeyPair = isaacKeyPair;
	}

	public void setItembonusactions(ItemBonusActions itembonusactions) {
		this.itembonusactions = itembonusactions;
	}

	public void setItemEffects(ItemEffects itemEffects) {
		this.itemEffects = itemEffects;
	}

	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	public boolean[] setKilledBarrowBrothers(boolean[] b) {
		return this.killedBarrowBrothers = b;
	}

	public void setKills(double k) {
		this.kills = k;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public void setLastObject(GlobalObject lastObject) {
		this.lastObject = lastObject;
	}

	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}

	public void setLastRecite(int lastRecite) {
		this.lastRecite = lastRecite;
	}

	public void setLastVeng(int lastVeng) {
		this.lastVeng = lastVeng;
	}

	public void setLastVote(long time) {
		this.lastVoted = time;
	}

	public void setLearnedPrayer(boolean[] learnedPrayer) {
		this.learnedPrayer = learnedPrayer;
	}

	public void setLearnedRenewal(boolean learnedRenewal) {
		this.learnedRenewal = learnedRenewal;
	}

	public void setLending(boolean lending) {
		this.lending = lending;
	}

	public void setLendingTill(long lendingTill) {
		this.lendingTill = lendingTill;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public void setLoyaltyprogramme(LoyaltyProgramme loyaltyprogramme) {
		this.loyaltyprogramme = loyaltyprogramme;
	}

	public void setLummyTreePatch(int sapling) {
		this.lummyTreePatch = sapling;
	}

	public void setLummyTreePatchRaked(boolean raked) {
		this.lummyTreePatchRaked = raked;
	}

	public void setMarkerSeeds(MarkerSeeds markerSeeds) {
		this.markerSeeds = markerSeeds;
	}

	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
	}

	public void setMetAdvisor(boolean metAdvisor) {
		this.metAdvisor = metAdvisor;
	}
	
	public void setMiscellaniaThrone(int miscellaniaThrone) {
		this.miscellaniaThrone = miscellaniaThrone;
	}

	public void setMusicanRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendMusicianButtonConfig();
		}
	}

	public void setMusicianResting(boolean resting) {
		this.resting = resting;
		sendMusicianButtonConfig();
	}

	public void setMuted(long muted) {
		this.muted = muted;
	}

	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}

	public void setNewplayer(boolean newplayer) {
		this.newplayer = newplayer;
	}

	public void setNewsbook(boolean newsbook) {
		this.newsbook = newsbook;
	}

	public void setOverloadDelay(int overloadDelay) {
		this.overloadDelay = overloadDelay;
	}

	public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public void setPanelId(int id) {
		this.panelId = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}

	public void setPestPoints(int pestPoints) {
		if (pestPoints >= 500) {
			this.pestPoints = 500;
			packets()
			.sendMessage(
					"You have reached the maximum amount of commendation points, you may only have 500 at one time.");
			return;
		}
		this.pestPoints = pestPoints;
	}

	public void setPet(PetManager pet) {
		this.pet = pet;
	}

	public void setPetFollow(int petFollow) {
		this.petFollow = petFollow;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public void setPkPoints(int p) {
		this.pkPoints = p;
	}

	public void setPKPoints(int pKPoints) {
		PKPoints = pKPoints;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int setPlinthStage(int plinthStage) {
		return this.plinthStage = plinthStage;
	}

	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}

	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked",
				teleDelay + Misc.currentTimeMillis());
		prayer.closeAllPrayers();
	}

	public void setPrayerRenewalDelay(int delay) {
		this.prayerRenewalDelay = delay;
	}

	public void setPrivateChatSetup(int privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
	}

	public void setProfanityFilter(boolean profanityFilter) {
		this.profanityFilter = profanityFilter;
	}

	public void setQuestStage(int questStage) {
		this.questStage = questStage;
	}

	public void setQuizEvent(boolean quizEvent) {
		this.quizEvent = quizEvent;
	}

	public void setRandomEventTimer(long l) {
		this.randomEventTimer = l;
	}

	public void setRealPet(Pets pets) {
		this.realPet = pets;

	}

	public void setReceivedSantaSuit(boolean receivedSantaSuit) {
		this.receivedSantaSuit = receivedSantaSuit;
	}

	public void setReceivedwildyhat(boolean receivedwildyhat) {
		this.receivedwildyhat = receivedwildyhat;
	}

	public void setRecipeStage(int recipeStage) {
		this.recipeStage = recipeStage;
	}

	public void setRecite(boolean recite) {
		this.recite = recite;
	}

	public void setRemovedPrayers(boolean removedPrayers) {
		this.removedPrayers = removedPrayers;
	}

	public void setRender(int i) {
		this.render = i;
		appearence.generateAppearanceData();
	}

	public void setReports(int reports) {
		this.reports = reports;
	}

	public void setResourcepoints(int resourcepoints) {
		this.resourcepoints = resourcepoints;
	}

	public void setResourcesTotal(int resourcesTotal) {
		this.resourcesTotal = resourcesTotal;
	}

	public void setResourceTime(int resourceTime) {
		this.resourceTime = resourceTime;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public void setRockPlinth(RockPlinth rockPlinth) {
		this.rockPlinth = rockPlinth;
	}

	public void setRockplinthObject(ArrayList<GlobalObject> rockplinthObject) {
		this.rockplinthObject = rockplinthObject;
	}

	public void setRuinsofuzerStage(int ruinsofuzerStage) {
		this.ruinsofuzerStage = ruinsofuzerStage;
	}

	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		packets().sendRunEnergy();
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}

	public void setSaradominStage(int saradominStage) {
		this.saradominStage = saradominStage;
	}

	public void setSavedHomeX(int savedHomeX) {
		this.savedHomeX = savedHomeX;
	}

	public void setSavedHomeY(int savedHomeY) {
		this.savedHomeY = savedHomeY;
	}

	public void setSavedHomeZ(int savedHomeZ) {
		this.savedHomeZ = savedHomeZ;
	}

	public void setSaveplane(int saveplane) {
		this.saveplane = saveplane;
	}

	public void setSaveX(int saveX) {
		this.saveX = saveX;
	}

	public void setSaveY(int saveY) {
		this.saveY = saveY;
	}

	public void setScapeKeys(int scapeKeys) {
		this.scapeKeys = scapeKeys;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public void setSecondColumn(int i) {
		this.secondColumn = i;
	}

	public void setShipcharter(ShipCharter shipcharter) {
		this.shipcharter = shipcharter;
	}

	public void setShootingStar(ShootingStar shootingStar) {
		this.shootingStar = shootingStar;
	}

	public void setSkills(Skills skills) {
		this.skills = skills;
	}

	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
	}

	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public void setSlayerPoints(int i) {
		this.slayerPoints = i;
	}

	/**
	 * Sets a incoming sound to player.
	 *
	 * @author Fuzen Seth
	 */
	public void setSound(int soundId) {
		packets().sendSound(soundId, 0, 1);
	}

	public void setSpeakedwithMaverick(boolean speakedwithMaverick) {
		this.speakedwithMaverick = speakedwithMaverick;
	}

	public void setStarterStage(int s) {
		starterStage = s;
	}

	public void setStartingStage(int startingStage) {
		this.startingStage = startingStage;
	}

	public void setStoredBones(boolean storedBones) {
		this.storedBones = storedBones;
	}

	public void setStreak(int s) {
		this.streak = s;
	}

	public void setStrongholdStage(int strongholdStage) {
		this.strongholdStage = strongholdStage;
	}

	public void setSummer(boolean summer) {
		this.summer = summer;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	public void setTaverlyTreePatch(int sapling) {
		this.taverlyTreePatch = sapling;
	}

	public void setTaverlyTreePatchRaked(boolean raked) {
		this.taverlyTreePatchRaked = raked;
	}

	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked",
				teleDelay + Misc.currentTimeMillis());
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	public void setTemporaryAttribute(String attribute, Object value) {
		temporaryAttributes.put(attribute, value);
	}

	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}

	public void setThirdColumn(int i) {
		this.thirdColumn = i;
	}

	public void setTier1(boolean tier1) {
		this.tier1 = tier1;
	}

	public void setTier2(boolean tier2) {
		this.tier2 = tier2;
	}

	public void setTier3(boolean tier3) {
		this.tier3 = tier3;
	}

	public void setTier4(boolean tier4) {
		this.tier4 = tier4;
	}

	public void setTier5(boolean tier5) {
		this.tier5 = tier5;
	}

	public void setTier6(boolean tier6) {
		this.tier6 = tier6;
	}

	public void setTimeUntilWrongLocation(long l) {
		this.timeUntilWrongLocation = l;
	}

	public void setTitleManager(TitleManager titleManager) {
		this.titleManager = titleManager;
	}

	public Trade setTrade(Trade trade) {
		return this.trade = trade;
	}

	public void setTransformed(boolean transformed) {
		this.transformed = transformed;
	}

	public void setTrapAmount(int trapAmount) {
		this.trapAmount = trapAmount;
	}

	public void settStage(int tStage) {
		this.tStage = tStage;
	}


	public void setUnselectedXP(boolean unselectedXP) {
		this.unselectedXP = unselectedXP;
	}

	public void setUrnStage(int urnStage) {
		this.urnStage = urnStage;
	}

	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setUsingTicket(boolean usingTicket) {
		this.usingTicket = usingTicket;
	}

	public void setVantsPermission(boolean vantsPermission) {
		this.vantsPermission = vantsPermission;
	}

	public void setVarrockTreePatch(int sapling) {
		this.varrockTreePatch = sapling;
	}

	public void setVarrockTreePatchRaked(boolean raked) {
		this.varrockTreePatchRaked = raked;
	}

	public void setVeteran(boolean veteran) {
		this.veteran = veteran;
	}

	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}

	public void setWantedStage(int wantedstage) {
		this.wantedstage = wantedstage;
		Wanted.ShowBind(this);
	}

	public void setWGuildTokens(int tokens) {
		wGuildTokens = tokens;
	}

	public void setWildernessHats(WildernessHelmets wildernessHats) {
		this.wildernessHats = wildernessHats;
	}

	public void setWildernessSkull() {
		skullDelay = 3000;
		skullId = 0;
		appearence.generateAppearanceData();
	}

	public void setWorldNotifications(boolean worldNotifications) {
		this.worldNotifications = worldNotifications;
	}

	public void setZanarisHouse(ZanarisHouse zanarisHouse) {
		this.zanarisHouse = zanarisHouse;
	}

	public Skills getSkills() {
		return skills;
	}

	public double skillXPBoost() {
		return skillXPBoost;
	}

	public void skillXPBoost(double b) {
		this.skillXPBoost = b;
	}

	public int slayerPoints() {
		return slayerPoints;
	}

	public SlayerTask slayerTask() {
		return slayerTask;
	}

	public void slayerTask(SlayerTask t) {
		this.slayerTask = t;
	}

	public void sm(String Msg) {
		packets().sendMessage(Msg);
	}
	
	public void sm(Object message) {
		packets().sendMessage(message+"");
	}

	public void start() {
		loadMapRegions();
		started = true;
		run();
		if (isDead())
			sendDeath(null);
	}

	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterfaces) {
		if (getTrade() != null)
			return;
		if(duel != null)
			return;
		if (stopWalk)
			resetWalkSteps();
		if(stopInterfaces)
			closeInterfaces();
		actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}

	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}

	public void switchItemsLook() {
		oldItemsLook = !oldItemsLook;
		packets().sendItemsLook();
	}

	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}

	public Tasks tasks() {
		return tasks;
	}

	public void teleportPlayer(int x, int y, int z) {
		setLocation(new Location(x, y, z));
		stopAll();
	}

	public long timeUntilWrongLocation() {
		return timeUntilWrongLocation;
	}

	public void toggleLootShare() { // TODO
		this.lootshareEnabled = !this.lootshareEnabled;
		packets().sendConfig(1083, this.lootshareEnabled ? 1 : 0);
		sendMessage(String.format(
				"<col=115b0d>Lootshare is now %sactive!</col>",
				this.lootshareEnabled ? "" : "in"));
	}

	public void toogleMusicanRest(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update)
			sendMusicianButtonConfig();
	}

	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update)
			sendRunButtonConfig();
	}

	public void tutorialTeleport(int x, int y, int z) {
		setLocation(new Location(x, y, z));
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void updateInterfaceListenerEvent() {
		if (interfaceListenerEvent != null) {
			interfaceListenerEvent.run();
			interfaceListenerEvent = null;
		}
	}

	public void useStairs(int emoteId, final Location dest, int useDelay,
			int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}

	public void useStairs(int emoteId, final Location dest, int useDelay,
			int totalDelay, final String message) {
		stopAll();
		addStopDelay(totalDelay);
		if (emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if (useDelay == 0)
			setLocation(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (isDead())
						return;
					setLocation(dest);
					if (message != null)
						packets().sendMessage(message);
				}
			}, useDelay - 1);
		}
	}

	public boolean usingProtectMagic() {
		return prayer.usingPrayer(0, 17) || prayer.usingPrayer(1, 7);
	}

	public boolean usingProtectMelee() {
		return prayer.usingPrayer(0, 19) || prayer.usingPrayer(1, 9);
	}

	public boolean usingProtectRange() {
		return prayer.usingPrayer(0, 18) || prayer.usingPrayer(1, 8);
	}

	public boolean withinDistance(Player tile) {
		if (tile.getZ() != getZ())
			return false;
		return Math.abs(tile.getX() - getX()) <= 14 && Math.abs(tile.getY() - getY()) <= 14;
	}

	public PlayerHouse getHouse() {
		return house;
	}

	public void setHouse(PlayerHouse house) {
		this.house = house;
	}

	public void refreshDynamicMap() {
		setForceNextMapLoadRefresh(true);
		packets().sendDynamicMapRegion(true);
	}

	public TemporaryData getData() {
		return temporaryData;
	}
	
	public Slave getSlave() {
		return slave;
	}

	private int tutorialStage;
	
	public int getTutorialStage() {
		return tutorialStage;
	}
	
	public void setTutorialStage(int i) {
		this.tutorialStage = i;
	}
	
	private Clan clan;

	public void setClan(Clan clan) {
		this.clan = clan;
	}
	
	public Clan getClan() {
		return clan;
	}
	
	public Duel getDuel() {
		return duel;
	}
	
	public void setDuel(Duel duel) {
		this.duel = duel;
	}
	
	private transient DialogueScriptManager dialogueScript;
	
	public DialogueScriptManager getDialogueScript() {
		return dialogueScript;
	}
	
	public boolean isOwner() {
		String[] owners = { "Lucifer" };
		for(String s : owners)
			if(getUsername().equalsIgnoreCase(s) || isHiddenOwner())
				return true;
		return false;
	}
	
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public static Player createBlankPlayer(String name) {
		Player newInstance = new Player("generate", new Location(0, 0, 0));
		newInstance.setUsername(name);
		return newInstance;
	}
	
	private transient ScreenFade screenFade;
	
	public ScreenFade getScreenFade() {
		return screenFade;
	}
	
	private boolean hiddenOwner;
	
	public boolean isHiddenOwner() {
		return hiddenOwner;
	}
	
	public void setHiddenOwner(boolean b) {
		hiddenOwner = b;
	}
	
	private C_O_R cavernOfRemembrance;
	
	public C_O_R getCavernOfRemembrance() {
		return cavernOfRemembrance;
	}
	
	private transient int tickDelay;
	
	public int getTickDelay() {
		return tickDelay;
	}

	public void setTickDelay(int tickDelay) {
		this.tickDelay = tickDelay;
	}

	private transient KeyStrokeManager keyStrokes;
	
	public KeyStrokeManager getKeyStrokes() {
		return keyStrokes;
	}
	
	private transient ButtonStrokeManager buttonStrokes;
	
	public ButtonStrokeManager getButtonStrokes() {
		return buttonStrokes;
	}
	
	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String s) {
		this.title = s;
	}

	public boolean hasTitle() {
		return getTitle() != null && getTitle() != "";
	}

	public boolean hasPickedGameMode() {
		return pickedGameMode;
	}

	public void setPickedGameMode(boolean pickedGameMode) {
		this.pickedGameMode = pickedGameMode;
	}
	
	public int getTheoreticalMeleeMaxHit() {
		int a = skills.getLevel(2);
		int b = combatDefinitions.getBonuses()[14];
		double c = a;
		double d = b;
		double f = 0;
		double h = 0;
		f = d * 0.00175 + 0.1;
		h = Math.floor(c * f + 2.05);
		return (int) h * 10;
	}

	public int getTheoreticalRangedMaxHit() {
		int a = skills.getLevel(4);
		int b = combatDefinitions.getBonuses()[15];
		double c = a;
		double d = b;
		double f = 0;
		double h = 0;
		f = d * 0.00195 + 0.1;
		h = Math.floor(c * f + 2.05);
		return (int) h * 10;
	}
	
	public int getTheoreticalMagicMaxHit() {
		int a = skills.getLevel(6);
		int b = combatDefinitions.getBonuses()[17];
		double c = a;
		double d = b;
		double f = 0;
		double h = 0;
		f = d * 0.00195 + 0.1;
		h = Math.floor(c * f + 2.05);
		return (int) h * 10;
	}
	
	public boolean cantInteract() {
		Player player = this;
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion()
			|| player.getFreezeDelay() >= Misc.currentTimeMillis() || player.isDead() 
				|| player.getStopDelay() > Misc.currentTimeMillis()
					|| player.getEmotesManager().getNextEmoteEnd() >= Misc.currentTimeMillis())
			return true;
		return false;
	}
	
	public transient boolean loggedOutUsingButton;
	
	private String macAddress;

	public void setMACAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getMACAddress() {
		return macAddress;
	}
	
	public String getIP() {
		return session.getIP();
	}
	
	private String creationIP;
	
	public void setCreationIP(String ip) {
		this.creationIP = ip;
	}

	public String getCreationIP() {
		return creationIP;
	}
	
	private LocationCrystal locationCrystal;
	
	public LocationCrystal getLC() {
		return locationCrystal;
	}
	
	
}