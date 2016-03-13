package plus.crates.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Hologram {

	private List<Object> destroyCache;
	private List<Object> spawnCache;
	private List<UUID> players;
	private List<String> lines;
	private Location loc;

	private static final double ABS = 0.23D;
	private static String path;
	private static String version;

	/*
	 * Cache for getPacket()-Method
	 */
	private static Class<?> armorStand;
	private static Class<?> worldClass;
	private static Class<?> nmsEntity;
	private static Class<?> craftWorld;
	private static Class<?> packetClass;
	private static Class<?> entityLivingClass;
	private static Constructor<?> armorStandConstructor;

	/*
	 * Cache for getDestroyPacket()-Method
	 */
	private static Class<?> destroyPacketClass;
	private static Constructor<?> destroyPacketConstructor;

	/*
	 * Cache for sendPacket()-Method
	 */
	private static Class<?> nmsPacket;


	static {
		path = Bukkit.getServer().getClass().getPackage().getName();
		version = path.substring(path.lastIndexOf(".") + 1, path.length());

		try {
			armorStand = Class.forName("net.minecraft.server." + version + ".EntityArmorStand");
			worldClass = Class.forName("net.minecraft.server." + version + ".World");
			nmsEntity = Class.forName("net.minecraft.server." + version + ".Entity");
			craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			packetClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutSpawnEntityLiving");
			entityLivingClass = Class.forName("net.minecraft.server." + version + ".EntityLiving");
			armorStandConstructor = armorStand.getConstructor(new Class[]{worldClass});

			destroyPacketClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityDestroy");
			destroyPacketConstructor = destroyPacketClass.getConstructor(int[].class);

			nmsPacket = Class.forName("net.minecraft.server." + version + ".Packet");
		} catch (SecurityException ex) {
			System.err.println("Error - Classes not initialized!");
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new hologram
	 * Note: The constructor will automatically initialize the internal cache; it may take some millis
	 *
	 * @param loc   The location where this hologram is shown
	 * @param lines The text-lines, from top to bottom, farbcodes are possible
	 */
	public Hologram(Location loc, String... lines) {
		this(loc, Arrays.asList(lines));
	}

	/**
	 * Create a new hologram
	 * Note: The constructor will automatically initialize the internal cache; it may take some millis
	 *
	 * @param loc   The location where this hologram is shown
	 * @param lines The text-lines, from top to bottom, farbcodes are possible
	 */
	public Hologram(Location loc, List<String> lines) {
		this.lines = lines;
		this.loc = loc;
		this.players = new ArrayList<UUID>();
		this.spawnCache = new ArrayList<Object>();
		this.destroyCache = new ArrayList<Object>();

		// Init
		Location displayLoc = loc.clone().add(0, (ABS * lines.size()) - 1.97D, 0);
		for (int i = 0; i < lines.size(); i++) {
			Object packet;
			packet = this.getPacket(this.loc.getWorld(), displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), this.lines.get(i));
			this.spawnCache.add(packet);
			try {
				Field field = packetClass.getDeclaredField("a");
				field.setAccessible(true);
				this.destroyCache.add(this.getDestroyPacket((Integer) field.get(packet)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			displayLoc.add(0, ABS * (-1), 0);
		}
	}

	/**
	 * Shows this hologram to the given player
	 *
	 * @param p The player who will see this hologram at the location specified by calling the constructor
	 * @return true, if the action was successful, else false
	 */
	public boolean display(Player p) {
		for (Object aSpawnCache : spawnCache) {
			this.sendPacket(p, aSpawnCache);
		}

		this.players.add(p.getUniqueId());
		return true;
	}

	public void displayAll() {
		for (Player player : Bukkit.getOnlinePlayers())
			display(player);
	}

	/**
	 * Removes this hologram from the players view
	 *
	 * @param p The target player
	 * @return true, if the action was successful, else false (including the try to remove a non-existing hologram)
	 */
	public boolean destroy(Player p) {
		if (this.players.contains(p.getUniqueId())) {
			for (Object aDestroyCache : this.destroyCache) {
				this.sendPacket(p, aDestroyCache);
			}
			this.players.remove(p.getUniqueId());
			return true;
		}
		return false;
	}

	public void destroyAll() {
		for (Player player : Bukkit.getOnlinePlayers())
			destroy(player);
	}

	private Object getPacket(World w, double x, double y, double z, String text) {
		try {
			Object craftWorldObj = craftWorld.cast(w);
			Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle", new Class<?>[0]);
			Object entityObject = armorStandConstructor.newInstance(new Object[]{getHandleMethod.invoke(craftWorldObj, new Object[0])});
			Method setCustomName = entityObject.getClass().getMethod("setCustomName", new Class<?>[]{String.class});
			setCustomName.invoke(entityObject, new Object[]{text});
			Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", new Class[]{boolean.class});
			setCustomNameVisible.invoke(entityObject, new Object[]{true});
			Method setGravity = entityObject.getClass().getMethod("setGravity", new Class<?>[]{boolean.class});
			setGravity.invoke(entityObject, new Object[]{false});
			Method setLocation = entityObject.getClass().getMethod("setLocation", new Class<?>[]{double.class, double.class, double.class, float.class, float.class});
			setLocation.invoke(entityObject, new Object[]{x, y, z, 0.0F, 0.0F});
			Method setInvisible = entityObject.getClass().getMethod("setInvisible", new Class<?>[]{boolean.class});
			setInvisible.invoke(entityObject, new Object[]{true});
			Constructor<?> cw = packetClass.getConstructor(new Class<?>[]{entityLivingClass});
			Object packetObject = cw.newInstance(new Object[]{entityObject});
			return packetObject;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getDestroyPacket(int... id) {
		try {
			return destroyPacketConstructor.newInstance(id);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendPacket(Player p, Object packet) {
		try {
			Method getHandle = p.getClass().getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(p);
			Object pConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
			Method sendMethod = pConnection.getClass().getMethod("sendPacket", new Class[]{nmsPacket});
			sendMethod.invoke(pConnection, new Object[]{packet});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}