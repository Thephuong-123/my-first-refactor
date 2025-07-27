# Dự án Refactor code Java theo nguyên tắc Clean Code

## Mục tiêu
Refactor mã nguồn `PersonalTaskManagerViolations.java` theo các nguyên tắc:
- **KISS** – Keep It Simple, Stupid
- **YAGNI** – You Aren’t Gonna Need It
- **DRY** – Don’t Repeat Yourself

---

## Mã nguồn gốc và mã đã refactor

- **Nhánh [main](https://github.com/Thephuong-123/my-first-refactor/tree/main)** chứa mã **gốc chưa refactor**.
- **Nhánh [feature/refactor-code](https://github.com/Thephuong-123/my-first-refactor/tree/feature/refactor-code)** chứa phiên bản **đã refactor** theo các nguyên tắc Clean Code.

> Bạn có thể chuyển sang nhánh `feature/refactor-code` để xem code đã được cải tiến.

---

## Mục đích và nội dung refactor

- Tách các hàm xử lý phức tạp thành các hàm con dễ hiểu hơn.
- Loại bỏ những đoạn mã trùng lặp (áp dụng nguyên tắc DRY).
- Tránh viết code cho các tính năng không cần thiết (theo YAGNI).
- Làm code gọn gàng, dễ bảo trì hơn (KISS).

---

## Cấu trúc dự án

```
my-first-refactor/
├── PersonalTaskManagerViolations.java # Mã gốc
├── feature/refactor-code/             # Nhánh chứa mã đã refactor
├── README.md                          # Tệp mô tả dự án
└── ...
```

---

## Lợi ích sau khi refactor

- Mã nguồn dễ đọc và dễ hiểu hơn.
- Thuận tiện hơn trong việc bảo trì và mở rộng.
- Hạn chế lỗi phát sinh do trùng lặp logic.

---

## Ghi chú

> Dự án được thực hiện nhằm **thực hành kỹ thuật refactor** trong lập trình Java.
> 
> Đây là một ví dụ đơn giản minh họa cách áp dụng các nguyên tắc lập trình sạch (Clean Code).

---

### Danh sách thành viên nhóm

| STT | Họ tên         | MSSV      |
|-----|----------------|-----------|
| 1   | Nguyễn Đình Thiên Kim | 2374802013463 |
| 2   | Ngô Phú Tài   | 2374802010441 |
| 3   | Nguyễn Thế Phương  | 2374802010405 |

