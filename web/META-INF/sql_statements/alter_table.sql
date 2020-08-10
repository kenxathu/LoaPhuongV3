ALTER TABLE monthly_expense_work_detail ADD (
-- Giá trị Đăng ký hạch toán
registered_booked_value NUMBER(15, 2),
-- Giá trị ban hành hạch toán
approved_booked_value NUMBER(15, 2),
-- Tháng hạch toán
booked_month NUMBER(2, 0),
-- Năm hạch toán
booked_year NUMBER(4, 0),
-- Giá trị hạch toán thực tế
executed_booked_value NUMBER(15, 2),
-- Giá trị còn lại (hạch toán)
remained_booked_value NUMBER(15, 2),
-- Phần trăm Thực hiện/Kế hoạch (hạch toán)
percentage_booked_value NUMBER(5, 2) );

COMMENT ON COLUMN monthly_expense_work_detail.registered_booked_value
IS
  'Gia tri Dang ky Thanh toan' COMMENT ON COLUMN monthly_expense_work_detail.approved_booked_value
IS
  'Gia tri Ban hanh Hach toan' COMMENT ON COLUMN monthly_expense_work_detail.booked_month
IS
  'Thang Hach toan' COMMENT ON COLUMN monthly_expense_work_detail.executed_booked_value
IS
  'Gia tri Hach toan thuc te' COMMENT ON COLUMN monthly_expense_work_detail.remained_booked_value
IS
  'Gia tri con lai (hach toan)' COMMENT ON COLUMN monthly_expense_work_detail.percentage_booked_value
IS
  'Phan tram Thuc hien/Ke hoach (hach toan)' COMMENT ON COLUMN monthly_expense_work_detail.booked_year
IS
  'Nam hach toan';

commit;