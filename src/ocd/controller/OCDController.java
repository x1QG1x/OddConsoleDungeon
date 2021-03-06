package ocd.controller;

import ocd.OCDConsole;
import ocd.controller.commands.*;
import ocd.dao.DAOFactory;
import ocd.dao.entities.Adventurer;
import ocd.dao.entities.Lord;
import ocd.dao.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Quentin Gangler on 20/11/2016.
 *
 */
public class OCDController {

    private OCDCommandParser commandParser;
    private LordDAO lordDAO;
    private AdventurerDAO adventurerDAO;
    private ItemDAO itemDAO;
    private EntityDAO entityDAO;
    private InventoryDAO inventoryDAO;
    private MonsterDAO monsterDAO;

    public static Lord currentLord;
    public static Adventurer currentAdventurer;

    public OCDController(DAOFactory daoFactory) {
        lordDAO = daoFactory.getLordDAO();
        adventurerDAO = daoFactory.getAdventurerDAO();
        itemDAO = daoFactory.getItemDAO();
        entityDAO = daoFactory.getEntityDAO();
        inventoryDAO = daoFactory.getInventoryDAO();
        monsterDAO = daoFactory.getMonsterDAO();
        initializeCommands();
    }

    private void initializeCommands() {
        List<OCDCommand> commands = new ArrayList<>();
        commands.add(new RegisterCommand(lordDAO));
        commands.add(new LoginCommand(lordDAO, adventurerDAO));
        commands.add(new AdventurerCommands(lordDAO, adventurerDAO, entityDAO));
        commands.add(new ShopCommands(itemDAO, entityDAO, adventurerDAO));
        commands.add(new InventoryCommands(inventoryDAO, entityDAO));
        commands.add(new FightCommand(adventurerDAO, monsterDAO, entityDAO));
        commands.add(new FleeCommand(adventurerDAO));
        commands.add(new MonsterCommands(monsterDAO, entityDAO, itemDAO, inventoryDAO));
        commands.add(new ExitCommand());
        commands.add(new HelpCommand(commands));
        commandParser = new OCDCommandParser(commands);
    }

    public void startGame() {
        boolean exitGame = false;
        Scanner in = new Scanner(System.in);
        while (!exitGame) {
            OCDConsole.printRender(getTextPrompt());
            System.out.flush();
            String nextLine = in.nextLine();
            OCDCommand command = commandParser.parseCommand(nextLine);
            if (command != null) {
                command.execute();
                if (command instanceof ExitCommand) {
                    exitGame = true;
                }
            }
        }
    }

    private String getTextPrompt() {
        String text = "\n@|white,bold OCD";
        if (isLordConnected()) {
            text += "[|@@|blue,bold " + currentLord.getLogin() + "|@@|white,bold ]";
        }
        if (hasCurrentAdventurer()) {
            text += "[|@@|yellow,bold " + currentAdventurer.getName() + "(" + currentAdventurer.getStatus() + ")|@@|white,bold ]";
        }
        text += "> |@";
        return text;
    }

    public static boolean isLordConnected() {
        return currentLord != null;
    }

    public static boolean verifyLordConnected() {
        if (!isLordConnected()) {
            OCDConsole.printlnError("You must connect to execute this command");
            return false;
        }
        return true;
    }

    public static boolean hasCurrentAdventurer() {
        return currentAdventurer != null;
    }

    public static boolean verifyCurrentAdventurer() {
        if(!hasCurrentAdventurer()) {
            OCDConsole.printlnError("You must have an adventurer to execute this command");
            return false;
        }
        return true;
    }
}
