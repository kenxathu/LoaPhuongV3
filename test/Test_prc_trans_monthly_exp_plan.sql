/*Test procedure prc_trans_monthly_exp_plan:*/
/*Kiem tra lay danh sach org_code*/
select 
level,connect_by_isleaf,a.org_code 
from ORGANIZATION a
connect by prior  a.org_code = a.parent_org_code 
start with a.org_code = 'SUB_DEMO1_VER2';
/*
Kiem tra danh sach cv duoc lay ra.
*/
select b.work_type_code,  b.expense_code, SUM(NVL(b.executed_value,0)) as sum_executed_value, 
SUM(NVL(b.executed_booked_value,0)) as sum_executed_booked_value ,
SUM(NVL(b.approval_value,0)) as sum_approval_value, SUM(NVL(b.approved_booked_value,0)) as sum_approval_booked_value
from monthly_expense_work_general a, monthly_expense_work_detail b 
where a.general_id = b.general_id 
and a.status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2'
and a.year =2016 and a.month =6
and b.detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.booked_detail_status IN ('WORKING','COMPLETED','FINISHED','LOCKED')
and b.work_type_code <> 'CV3.1'
and b.is_transfer = 0 and b.is_transfer_booked =0
group by b.work_type_code, b.expense_code;

/*Lay du lieu theo contro c_list_transfer_data*/
select sum_work.work_type_code, sum_work.expense_code,month_plan.parrent_exp_code, 
sum_work.sum_approval_value - sum_work.sum_executed_value as tranfer_approval__value,
sum_work.sum_approval_booked_value - sum_work.sum_executed_booked_value as tranfer_approval_booked_value, 
sum_executed_value, sum_executed_booked_value
from 
(select b.work_type_code,  b.expense_code, SUM(NVL(b.executed_value,0)) as sum_executed_value, 
SUM(NVL(b.executed_booked_value,0)) as sum_executed_booked_value ,
SUM(NVL(b.approval_value,0)) as sum_approval_value, SUM(NVL(b.approved_booked_value,0)) as sum_approval_booked_value
from monthly_expense_work_general a, monthly_expense_work_detail b 
where a.general_id = b.general_id 
and a.status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2'
and a.year =2016 and a.month =6
and b.detail_status IN('WORKING','COMPLETED','FINISHED','LOCKED') 
and b.booked_detail_status IN ('WORKING','COMPLETED','FINISHED','LOCKED')
and b.work_type_code <> 'CV3.1'
and b.is_transfer = 1 and b.is_transfer_booked =1
group by b.work_type_code, b.expense_code
) sum_work
,
(select b.expense_code, b.parrent_exp_code
from monthly_expense_plan_general a, monthly_expense_plan_detail b
where a.general_id = b.general_id
and a.year = 2016 and b.month = 6
and a.status = 'COMPLETED' and b.detail_status = 'COMPLETED'
and a.org_code = 'SUB_DEMO1_VER2' ) month_plan
where sum_work.expense_code = month_plan.expense_code;
/*Du lieu duoc lay ra:
work_type_code:CV6	|expense_code: 1.1.1.1|parent_exp_code:	1.1.1|
Tong Gia tri giao dich TH CP:	20|Tong Gia tri giao dich GNCP:	0|Tong gia tri TH CP:	0| Tong gia tri GN CP	0
*/

/*Lay du lieu theo tree tu parent_exp_code cua cong viec thang nguon*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 6
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.1.1';
/* Gia tri ban dau:
approval_value: 50| booked_approval_value: 50| month:6 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 6
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.1';
/* Gia tri ban dau:
approval_value: 200| booked_approval_value: 200| month:6 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 6
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1';
/* Gia tri ban dau:
approval_value: 435| booked_approval_value: 435| month:6 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 6
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1';
/* Gia tri ban dau:
approval_value: 435| booked_approval_value: 435| month:6 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

/*Lay du lieu theo tree tu parent_exp_code cua cong viec thang dich*/
select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.1.1';
/* Gia tri ban dau:
approval_value: 50| booked_approval_value: 50| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

select  a.*
from monthly_expense_plan_detail a
where a.month = 7
and general_id = (select general_id from monthly_expense_plan_general
       where year = 2016 and status = 'COMPLETED' and org_code = 'SUB_DEMO1_VER2')     
and expense_code = '1.1.1';
/* Gia tri ban dau:
approval_value: 200| booked_approval_value: 200| month:7 |
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
approval_value: 405| booked_approval_value: 405| month:7 |
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
approval_value: 405| booked_approval_value: 405| month:7 |
origin_approval_value: null|Origin_approval_booked_value: null| 
after_approval_value:null| after_approval_booked_value: null
*/

/*update monthly_expense_plan_detail 
set APPROVAL_VALUE = origin_approval_value ,booked_approval_value= origin_approval_booked_value,
after_approval_value = origin_approval_value, after_approval_booked_value = origin_approval_booked_value;
/*Chay store procedure*/
execute prc_trans_Monthly_exp_plan(2016, 6, 7, 'SUB_DEMO1_VER2');