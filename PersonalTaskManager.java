import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

public class PersonalTaskManager {

    // Khai báo dùng chung
    private static final String FILE_PATH = "tasks.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final List<String> MUC_DO_UU_TIEN = Arrays.asList("Thấp", "Trung bình", "Cao");
    private static final AtomicInteger currentId = new AtomicInteger(1);

    // Repository tách riêng - nhưng ở chung file
    public static class TaskRepository {
        public JSONArray loadAll() {
            try {
                if (!Files.exists(Paths.get(FILE_PATH))) {
                    return new JSONArray();
                }
                String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                return new JSONArray(content);
            } catch (IOException e) {
                e.printStackTrace();
                return new JSONArray();
            }
        }

        public void saveAll(JSONArray tasks) {
            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(tasks.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addTask(JSONObject task) {
            JSONArray tasks = loadAll();
            tasks.put(task);
            saveAll(tasks);
        }
    }

    private final TaskRepository taskRepo = new TaskRepository();

    public JSONObject addNewTask(String title, String description,
                                 String dueDateStr, String priorityLevel) {

        if (!validateInput(title, dueDateStr, priorityLevel)) return null;

        LocalDate dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
        JSONArray tasks = taskRepo.loadAll();

        if (checkDuplicateTask(tasks, title, dueDate)) return null;

        JSONObject newTask = createTaskObject(title, description, dueDate, priorityLevel);
        taskRepo.addTask(newTask);

        System.out.println(String.format("Đã thêm nhiệm vụ mới thành công với ID: %s", newTask.get("id")));
        return newTask;
    }

    private boolean validateInput(String title, String dueDateStr, String priorityLevel) {
        return isTitleValid(title) && isDateValid(dueDateStr) && isPriorityValid(priorityLevel);
    }

    private boolean isTitleValid(String title) {
        if (title == null || title.trim().isEmpty()) {
            inLoi("Tiêu đề không được để trống.");
            return false;
        }
        return true;
    }

    private boolean isDateValid(String dueDateStr) {
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            inLoi("Ngày đến hạn không được để trống.");
            return false;
        }
        try {
            LocalDate.parse(dueDateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            inLoi("Ngày đến hạn không hợp lệ. Vui lòng dùng định dạng yyyy-MM-dd.");
            return false;
        }
    }

    private boolean isPriorityValid(String priorityLevel) {
        if (!MUC_DO_UU_TIEN.contains(priorityLevel)) {
            inLoi("Mức độ ưu tiên không hợp lệ. Vui lòng chọn Thấp, Trung bình hoặc Cao.");
            return false;
        }
        return true;
    }

    private boolean checkDuplicateTask(JSONArray tasks, String title, LocalDate dueDate) {
        for (Object obj : tasks) {
            JSONObject task = (JSONObject) obj;
            if (task.get("title").toString().equalsIgnoreCase(title)
                    && task.get("due_date").toString().equals(dueDate.format(DATE_FORMATTER))) {
                inLoi(String.format("Nhiệm vụ '%s' đã tồn tại với cùng ngày đến hạn.", title));
                return true;
            }
        }
        return false;
    }

    private JSONObject createTaskObject(String title, String description, LocalDate dueDate,
                                        String priorityLevel) {
        JSONObject task = new JSONObject();
        task.put("id", currentId.getAndIncrement());
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

    private void inLoi(String thongBao) {
        System.out.println("Lỗi: " + thongBao);
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
