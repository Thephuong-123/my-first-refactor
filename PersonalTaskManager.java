import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PersonalTaskManager {

    private static final String DB_FILE_PATH = "tasks_database.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Load tasks
    private static JSONArray loadTasksFromDb() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.err.println("Lỗi khi đọc file database: " + e.getMessage());
        }
        return new JSONArray();
    }

    // Save tasks
    private static void saveTasksToDb(JSONArray tasksData) {
        try (FileWriter file = new FileWriter(DB_FILE_PATH)) {
            file.write(tasksData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file database: " + e.getMessage());
        }
    }

    public JSONObject addNewTask(String title, String description,
                                 String dueDateStr, String priorityLevel) {

        if (!validateInput(title, dueDateStr, priorityLevel)) return null;

        LocalDate dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
        JSONArray tasks = loadTasksFromDb();

        if (checkDuplicateTask(tasks, title, dueDate)) return null;

        JSONObject newTask = createTaskObject(title, description, dueDate, priorityLevel);
        tasks.add(newTask);
        saveTasksToDb(tasks);

        System.out.println(String.format("Đã thêm nhiệm vụ mới thành công với ID: %s", newTask.get("id")));
        return newTask;
    }

    //Tách các hàm xử lý riêng 

    private boolean validateInput(String title, String dueDateStr, String priorityLevel) {
        return isTitleValid(title) && isDateValid(dueDateStr) && isPriorityValid(priorityLevel);
    }

    private boolean isTitleValid(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Lỗi: Tiêu đề không được để trống.");
            return false;
        }
        return true;
    }

    private boolean isDateValid(String dueDateStr) {
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            System.out.println("Lỗi: Ngày đến hạn không được để trống.");
            return false;
        }
        try {
            LocalDate.parse(dueDateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi: Ngày đến hạn không hợp lệ. Vui lòng dùng định dạng yyyy-MM-dd.");
            return false;
        }
    }

    private boolean isPriorityValid(String priorityLevel) {
        String[] validLevels = {"Thấp", "Trung bình", "Cao"};
        for (String level : validLevels) {
            if (level.equals(priorityLevel)) {
                return true;
            }
        }
        System.out.println("Lỗi: Mức độ ưu tiên không hợp lệ. Vui lòng chọn Thấp, Trung bình hoặc Cao.");
        return false;
    }

    private boolean checkDuplicateTask(JSONArray tasks, String title, LocalDate dueDate) {
        for (Object obj : tasks) {
            JSONObject task = (JSONObject) obj;
            if (task.get("title").toString().equalsIgnoreCase(title)
                    && task.get("due_date").toString().equals(dueDate.format(DATE_FORMATTER))) {
                System.out.println(String.format("Lỗi: Nhiệm vụ '%s' đã tồn tại với cùng ngày đến hạn.", title));
                return true;
            }
        }
        return false;
    }

    private JSONObject createTaskObject(String title, String description,
                                        LocalDate dueDate, String priorityLevel) {
        JSONObject task = new JSONObject();
        task.put("id", UUID.randomUUID().toString());
        task.put("title", title);
        task.put("description", description);
        task.put("due_date", dueDate.format(DATE_FORMATTER));
        task.put("priority", priorityLevel);
        task.put("status", "Chưa hoàn thành");
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        task.put("created_at", now);
        task.put("last_updated_at", now);
        return task;
    }

    public static void main(String[] args) {
        PersonalTaskManager manager = new PersonalTaskManager();

        System.out.println("\nThêm nhiệm vụ hợp lệ:");
        manager.addNewTask("Mua sách", "Sách Công nghệ phần mềm.", "2025-07-20", "Cao");

        System.out.println("\nThêm nhiệm vụ trùng lặp:");
        manager.addNewTask("Mua sách", "Sách Công nghệ phần mềm.", "2025-07-20", "Cao");

        System.out.println("\nThêm nhiệm vụ với tiêu đề rỗng:");
        manager.addNewTask("", "Không có tiêu đề.", "2025-07-22", "Thấp");
    }
}