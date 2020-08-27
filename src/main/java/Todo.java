public class Todo extends Task{
    Todo(String name) {
        super(name);
    }

    @Override
    public String missingNameError() {
        return "The description of a todo task cannot be empty.";
    }

    @Override
    public String toSaveDataFormat() {
        String saveData = "";
        saveData += this.isDone ? 1 : 0;
        saveData += " T " + this.name;
        return saveData;
    }

    @Override
    public String toString() {
        String marked = this.isDone ? "[✓] " : "[✗] ";
        return "[T]" + marked + this.name;
    }


}