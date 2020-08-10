/*Test procedure prc_trans_monthly_plan_cv31:*/
/*Kiem tra lay danh sach org_code*/
select 
level,connect_by_isleaf,a.org_code 
from ORGANIZATION a
connect by prior  a.org_code = a.parent_org_code 
start with a.org_code = 'SUB_DEMO1_VER2';

/*
Kiem tra danh sach cv duoc lay ra.
*/
select a.month,to_number(NVL(w.month,0)) as num_finish /*so thoi diem chot so lieu lay tu table work_type_item*/,
b.expense_code, b.work_type_code,b.detail_status, b.booked_detail_status,
b.is_transfer ,b.is_transfer_booked, SUM(NVL(b.executed_value,0)) as sum_executed_value, 
SUM(NVL(b.executed_booked_value,0)) as sum_executed_booked_value ,
SUM(NVL(b.approval_value,0)) as sum_approval_value, SUM(NVL(b.approved_booked_value,0)) as sum_approval_booked_value
from monthly_expense_work_general a, monthly_expense_work_detail b , work_type_item w
where a.general_id = b.general_id and b.expense_code = w.item_code
and w.is_active = 1
and a.status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2'
and a.year =2016 and a.month <= 7
and b.detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.booked_detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.work_type_code = 'CV3.1'
and (b.is_transfer = 0 or b.is_transfer_booked = 0)
group by a.month,b.expense_code, b.work_type_code,b.detail_status,
b.booked_detail_status,b.expense_code,b.is_transfer ,b.is_transfer_booked,
b.detail_status,b.booked_detail_status, to_number(NVL(w.month,0));

/*Lay du lieu theo contro c_list_trans_data_cv31*/
select sum_work_cv31.month, sum_work_cv31.num_finish ,
sum_work_cv31.detail_status,sum_work_cv31.booked_detail_status,sum_work_cv31.work_type_code,
sum_work_cv31.is_transfer ,sum_work_cv31.is_transfer_booked,
sum_work_cv31.expense_code,month_plan.parrent_exp_code, 
sum_work_cv31.sum_approval_value - sum_work_cv31.sum_executed_value as tranfer_approval__value,
sum_work_cv31.sum_approval_booked_value - sum_work_cv31.sum_executed_booked_value as tranfer_approval_booked_value, 
sum_executed_value, sum_executed_booked_value
from 
(select a.month,to_number(NVL(w.month,0)) as num_finish /*so thoi diem chot so lieu lay tu table work_type_item*/,
b.expense_code, b.work_type_code,b.detail_status, b.booked_detail_status,
b.is_transfer ,b.is_transfer_booked, SUM(NVL(b.executed_value,0)) as sum_executed_value, 
SUM(NVL(b.executed_booked_value,0)) as sum_executed_booked_value ,
SUM(NVL(b.approval_value,0)) as sum_approval_value, SUM(NVL(b.approved_booked_value,0)) as sum_approval_booked_value
from monthly_expense_work_general a, monthly_expense_work_detail b , work_type_item w
where a.general_id = b.general_id and b.expense_code = w.item_code
and w.is_active = 1
and a.status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2'
and a.year =2016 and a.month <= 10
and b.detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.booked_detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.work_type_code = 'CV3.1'
and (b.is_transfer = 0 or b.is_transfer_booked = 0)
group by a.month,b.expense_code, b.work_type_code,b.detail_status,
b.booked_detail_status,b.expense_code,b.is_transfer ,b.is_transfer_booked,
b.detail_status,b.booked_detail_status, to_number(NVL(w.month,0))
) sum_work_cv31
,
(select b.expense_code, b.parrent_exp_code
from monthly_expense_plan_general a, monthly_expense_plan_detail b
where a.general_id = b.general_id
and a.year = 2016 and b.month = 10
and a.status = 'COMPLETED' and b.detail_status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2' ) month_plan
where sum_work_cv31.expense_code = month_plan.expense_code;

/*Lay du lieu theo tree tu parent_exp_code cua cong viec thang nguon*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1.5.1';
/* Gia tri ban dau:
approval_value: 20| booked_approval_value: 20| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1.5';
/* Gia tri ban dau:
approval_value: 25| booked_approval_value: 25| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1';
/* Gia tri ban dau:
approval_value: 135| booked_approval_value: 135| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2';
/* Gia tri ban dau:
approval_value: 155| booked_approval_value: 155| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1';
/* Gia tri ban dau:
approval_value: 425| booked_approval_value: 405| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1';
/* Gia tri ban dau:
approval_value: 425| booked_approval_value: 405| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

/*Lay du lieu theo tree tu parent_exp_code cua cong viec thang ?ích*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1.5.1';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1.5';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2.1';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.2';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 10
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1';
/* Gia tri ban dau:
approval_value: 0| booked_approval_value: 0| month:10 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

execute prc_trans_monthly_plan_cv31(2016, 10, 'SUB_DEMO1_VER2');