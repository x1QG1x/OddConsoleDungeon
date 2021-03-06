package ocd.controller.commands.shop;

import ocd.OCDConsole;
import ocd.controller.OCDCommand;
import ocd.controller.OCDController;
import ocd.dao.interfaces.EntityDAO;

/**
 * Created by Quentin Gangler on 20/11/2016.
 *
 */
public class ExitCommand extends OCDCommand{

    private EntityDAO entityDAO;

    public ExitCommand(EntityDAO entityDAO) {
        super("exit", 0, "move the adventurer out of the shop");
        this.entityDAO = entityDAO;
    }

    @Override
    public void execute() {
        if (OCDController.verifyLordConnected() && OCDController.verifyCurrentAdventurer()) {
            if (entityDAO.changeStatus(
                    entityDAO.find(OCDController.currentAdventurer.getEntityID()),
                    "idle")) {
                OCDController.currentAdventurer.setStatus("idle");
                OCDConsole.printlnSuccess(OCDController.currentAdventurer.getName() + " exits the shop");
            }
        }
    }

    @Override
    public String getSyntax() {
        return getName();
    }
}
