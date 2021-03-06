package ocd.dao.entities;

/**
 * Created by t00191774 on 16/11/2016.
 *
 */
public class Entity {
    private int id;
    private long rooms;
    private int inventoryId;
    private int equipmentId;
    private double money;
    private int health;
    private int maxHealth;
    private String status;

    public Entity(int id, long rooms, int inventoryId, int equipmentId, double money, int health, int maxHealth, String status) {
        this.id = id;
        this.rooms = rooms;
        this.inventoryId = inventoryId;
        this.equipmentId = equipmentId;
        this.money = money;
        this.health = health;
        this.maxHealth = maxHealth;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public long getRooms() {
        return rooms;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public double getMoney() {
        return money;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        return "@|magenta,bold " + getId() + ": |@\n" +
                "level: @|green,bold " + getRooms() + "|@\n" +
                "money: @|yellow,bold $" + getMoney() + "|@\n" +
                "heath: @|red,bold " + getHealth() + "/" + getMaxHealth() + "|@\n" +
                "status: " + getStatus();
    }
}
