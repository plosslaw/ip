import java.util.Scanner;

public class Parser {
    private final String HELP_MSG = "Here is the list of available commands:" + Ui.NEW_LINE + Ui.NEW_LINE
                + Ui.PADDING + "todo <name>: Add todo task" + Ui.NEW_LINE
                + Ui.PADDING + "event <name> /at <date>: Add event task. Date format: YYYY-MM-DD" + Ui.NEW_LINE
                + Ui.PADDING + "deadline <name> /by <date>: Add deadline task. Date format: YYYY-MM-DD" + Ui.NEW_LINE
                + Ui.PADDING + "done <index>: Mark task at specified index as done" + Ui.NEW_LINE
                + Ui.PADDING + "list: View list of all tasks added" + Ui.NEW_LINE
                + Ui.PADDING + "delete <index>: Delete the task at specified index" + Ui.NEW_LINE
                + Ui.PADDING + "delete all: Delete all tasks from task list" + Ui.NEW_LINE
                + Ui.PADDING + "bye: Exit programme" + Ui.NEW_LINE
                + Ui.PADDING + "help: You do realise you're already on the help page, right?" + Ui.NEW_LINE;
    Scanner sc;
    String parsedLine;

    Parser(Scanner sc){
        this.sc = sc;
    }

    Boolean hasNext() {
        return sc.hasNext();
    }

    void parseLine() {
        parsedLine = this.sc.nextLine().trim();
    }

    boolean executeCommand(Storage storage, TaskList tasks) {
        String input = this.parsedLine;

        // we convert input to uppercase before checking to make commands case-insensitive
        if (input.toUpperCase().equals(Duke.Commands.EXIT.getString())) {
            Ui.sayGoodbye(storage, tasks);
            return true;
        }
        if (input.toUpperCase().equals(Duke.Commands.LIST.getString())) {
            tasks.displayTasks();
            return false;
        }
        if (input.toUpperCase().equals(Duke.Commands.HELP.getString())) {
            Ui.print(HELP_MSG);
            return false;
        }
        if (input.length() >= 5 && input.substring(0,5).toUpperCase().equals(Duke.Commands.DONE.getString())) {
            try {
                int index = Integer.parseInt(input.substring(5).trim());
                tasks.setTaskDone(index);
            } catch (NumberFormatException ex) {
                Ui.printError("Please input an Integer for the \"Done\" command.");
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }
        if (input.toUpperCase().equals(Duke.Commands.DELETEALL.getString())) {
            try {
                tasks.deleteAllTasks();
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }
        if (input.length() >= 7 && input.substring(0,7).toUpperCase().equals(Duke.Commands.DELETE.getString())) {
            try {
                int index = Integer.parseInt(input.substring(7).trim());
                tasks.deleteTask(index);
            } catch (NumberFormatException ex) {
                Ui.printError("Please input an Integer for the \"Delete\" command.");
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }
        if (input.length() >= 5 && input.substring(0,5).toUpperCase().equals(Duke.Commands.TODO.getString())) {
            try {
                String name = input.substring(5).trim();
                tasks.addTask(Todo.createTodo(name));
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }
        if (input.length() >= 6 && input.substring(0,6).toUpperCase().equals(Duke.Commands.EVENT.getString())) {
            try {
                int limiterPosition = input.indexOf(" /at ");
                String name;
                String timing;
                if (limiterPosition != -1) {
                    name = input.substring(6, limiterPosition).trim();
                    timing = input.substring(limiterPosition + 5).trim();
                } else {
                    throw new DukeException("Missing date for Event task");
                }
                tasks.addTask(Event.createEvent(name, timing));
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }
        if (input.length() >= 9 && input.substring(0,9).toUpperCase().equals(Duke.Commands.DEADLINE.getString())) {
            try {
                int limiterPosition = input.indexOf(" /by ");
                String name;
                String dueDate;

                if (limiterPosition != -1) {
                    name = input.substring(9, limiterPosition).trim();
                    dueDate = input.substring(limiterPosition + 5).trim();
                } else {
                    throw new DukeException("Missing deadline for Deadline task");
                }
                tasks.addTask(Deadline.createDeadline(name, dueDate));
            } catch (DukeException ex) {
                Ui.printError(ex.getMessage());
            }
            return false;
        }

        Ui.printError("Sorry I don't know what that means :(");
        return false;
    }


}
