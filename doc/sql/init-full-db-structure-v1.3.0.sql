SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `zzwork` DEFAULT CHARACTER SET utf8 ;
USE `zzwork` ;

-- -----------------------------------------------------
-- Table `zzwork`.`staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`staff` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `account` VARCHAR(50) NOT NULL COMMENT '员工账号',
  `staff_no` VARCHAR(45) NOT NULL COMMENT '员工工号',
  `dept_code` VARCHAR(50) NOT NULL DEFAULT 0 COMMENT '账号信息',
  `name` VARCHAR(45) NOT NULL COMMENT '员工姓名',
  `email` VARCHAR(45) NULL,
  `sex` VARCHAR(10) NULL COMMENT '性别',
  `avatar` VARCHAR(250) NULL DEFAULT '' COMMENT '账号头像',
  `birthday` DATETIME NULL COMMENT '生日',
  `jobs` VARCHAR(45) NULL DEFAULT '' COMMENT '职位\n0：员工\n1：组长\n2：主管\n3：经理\n4：boss',
  `status` CHAR(1) NOT NULL DEFAULT 0 COMMENT '员工状态\n0：试用员工\n1：正式员工\n2：离职',
  `gmt_entry` DATETIME NOT NULL COMMENT '入职时间',
  `gmt_left` DATETIME NULL COMMENT '离职时间，在职员工留空',
  `note` VARCHAR(1000) NULL DEFAULT '' COMMENT '备注',
  `gmt_created` DATETIME NOT NULL COMMENT '记录创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '记录最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_account` (`account` ASC),
  UNIQUE INDEX `idx_staff_no` (`staff_no` ASC),
  INDEX `idx_status` (`status` ASC),
  INDEX `idx_dept_code` (`dept_code` ASC))
ENGINE = MyISAM
COMMENT = '员工信息';


-- -----------------------------------------------------
-- Table `zzwork`.`contacts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`contacts` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `account` VARCHAR(50) NOT NULL,
  `keys` VARCHAR(45) NOT NULL DEFAULT 0 COMMENT '通讯类型\n0:email\n1:手机\n2:固定电话\n3:qq\n4:其他',
  `values` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '通讯内容',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_account` (`account` ASC))
ENGINE = InnoDB
COMMENT = '员工通讯信息';


-- -----------------------------------------------------
-- Table `zzwork`.`dept`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`dept` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `code` VARCHAR(50) NOT NULL DEFAULT 0 COMMENT '父ID，根为0',
  `note` VARCHAR(1000) NULL COMMENT '部门信息，可以放在工作平台首页显示',
  `gmt_created` DATETIME NOT NULL COMMENT '记录创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '记录修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_code` (`code` ASC))
ENGINE = MyISAM
COMMENT = '公司部门';


-- -----------------------------------------------------
-- Table `zzwork`.`bs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`bs` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL COMMENT '项目代号',
  `password` VARCHAR(45) NOT NULL COMMENT '与keys对应，在单点登录中可能会用到',
  `name` VARCHAR(250) NOT NULL COMMENT '业务系统名称',
  `right_code` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '对应权限父节点',
  `url` VARCHAR(250) NULL COMMENT '连接',
  `avatar` VARCHAR(250) NULL COMMENT '代表的图片',
  `note` VARCHAR(1000) NULL COMMENT '详细描述',
  `types` VARCHAR(45) NOT NULL COMMENT '系统类别：\n0：软件产品\n1：业务系统\n2：客户网站',
  `versions` VARCHAR(45) NOT NULL COMMENT '当前版本',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  INDEX `idx_types` (`types` ASC))
ENGINE = MyISAM
COMMENT = '公司的业务系统';


-- -----------------------------------------------------
-- Table `zzwork`.`bs_dept`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`bs_dept` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `bs_id` INT(20) NOT NULL,
  `dept_id` INT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_bs_id` (`bs_id` ASC),
  INDEX `idx_dept_id` (`dept_id` ASC))
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `zzwork`.`bs_staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`bs_staff` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `bs_id` INT(20) NOT NULL,
  `staff_id` INT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_bs_id` (`bs_id` ASC),
  INDEX `idx_staff_id` (`staff_id` ASC))
ENGINE = MyISAM
COMMENT = '员工单独关联的业务系统';


-- -----------------------------------------------------
-- Table `zzwork`.`auth_role_right`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`auth_role_right` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `right_id` INT(20) NOT NULL,
  `role_id` INT(20) NOT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  INDEX `index_right_id` (`right_id` ASC),
  INDEX `Index_role_id` (`role_id` ASC),
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`auth_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`auth_user` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(32) NOT NULL COMMENT '密码',
  `email` VARCHAR(50) NULL DEFAULT NULL,
  `steping` TINYINT(4) NULL DEFAULT 0 COMMENT '0：正常\n其他：不能正常登录',
  `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `Index_username` (`username` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`auth_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`auth_user_role` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `role_id` INT(20) NOT NULL,
  `user_id` INT(20) NOT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  INDEX `index_role_id` (`role_id` ASC),
  INDEX `index_user_id` (`user_id` ASC),
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`param`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`param` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `types` VARCHAR(64) NOT NULL,
  `names` VARCHAR(254) NULL DEFAULT NULL,
  `key` VARCHAR(254) NOT NULL,
  `value` VARCHAR(254) NOT NULL,
  `sort` TINYINT(4) NULL DEFAULT 0,
  `used` TINYINT(4) NOT NULL DEFAULT 0,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`param_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`param_type` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(45) NOT NULL,
  `name` VARCHAR(254) NULL DEFAULT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `key_UNIQUE` (`key` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`auth_right`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`auth_right` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(50) NOT NULL COMMENT '权限code',
  `name` VARCHAR(50) NOT NULL COMMENT '权限名',
  `sort` INT NOT NULL DEFAULT 0,
  `content` VARCHAR(254) NULL DEFAULT NULL COMMENT '权限',
  `menu` VARCHAR(50) NULL DEFAULT NULL COMMENT '菜单名',
  `menu_url` VARCHAR(254) NULL DEFAULT NULL COMMENT '菜单URL',
  `menu_css` VARCHAR(50) NULL DEFAULT NULL,
  `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_code` (`code` ASC),
  INDEX `idx_sort` (`sort` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`auth_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`auth_role` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '角色名字',
  `remark` VARCHAR(254) NULL DEFAULT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `zzwork`.`dept_right`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`dept_right` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `dept_id` INT(20) NOT NULL,
  `auth_right_id` INT(20) NOT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_dept_id` (`dept_id` ASC),
  INDEX `idx_auth_right_id` (`auth_right_id` ASC))
ENGINE = MyISAM
COMMENT = '部门权限，可以设置每个部门对应的权限';


-- -----------------------------------------------------
-- Table `zzwork`.`feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`feedback` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `bs_id` INT(20) NOT NULL DEFAULT 0,
  `account` VARCHAR(50) NOT NULL DEFAULT '',
  `topic` VARCHAR(100) NULL DEFAULT '',
  `content` VARCHAR(2000) NULL DEFAULT '',
  `status` TINYINT NULL DEFAULT 0 COMMENT '处理状态：\n0：未处理\n1：已解决\n2：不解决\n3：无法解决',
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_bs_id` (`bs_id` ASC),
  INDEX `idx_account` (`account` ASC),
  INDEX `idx_status` (`status` ASC))
ENGINE = MyISAM
COMMENT = '对系统的意见反鐀';


-- -----------------------------------------------------
-- Table `zzwork`.`scheduler_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`scheduler_event` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(254) NULL COMMENT '事件标题',
  `details` VARCHAR(2000) NULL COMMENT '详细内容',
  `start_date` DATETIME NOT NULL COMMENT '开始时间',
  `end_date` DATETIME NOT NULL COMMENT '结束时间',
  `assign_account` VARCHAR(45) NULL DEFAULT '' COMMENT '分配任务的账户',
  `owner_account` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '任务拥有者',
  `dept_code` VARCHAR(50) NULL DEFAULT '' COMMENT '事件创建或更新时所在的部门',
  `persent` TINYINT NULL DEFAULT 0 COMMENT '任务完成百分比\n0-100',
  `importance` TINYINT NULL DEFAULT 0 COMMENT '重要性\n不同程度用不同颜色在日历上表现',
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_owner_account` (`owner_account` ASC),
  INDEX `idx_start_date` (`start_date` ASC),
  INDEX `idx_end_date` (`end_date` ASC))
ENGINE = MyISAM
COMMENT = '日程';


-- -----------------------------------------------------
-- Table `zzwork`.`scheduler_report`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`scheduler_report` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `dept_code` VARCHAR(45) NOT NULL COMMENT '提交周报时，员工所在部门',
  `account` VARCHAR(45) NOT NULL,
  `text` VARCHAR(254) NULL DEFAULT '' COMMENT '日/周报标题',
  `details` VARCHAR(2000) NULL DEFAULT '' COMMENT '周报详细内容',
  `compose_date` DATETIME NOT NULL COMMENT '撰写日期',
  `year` CHAR(5) NULL COMMENT '年份',
  `week` TINYINT NULL COMMENT '一年中第几周',
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_dept_code` (`dept_code` ASC),
  INDEX `idx_account` (`account` ASC),
  INDEX `idx_week` (`week` ASC),
  INDEX `idx_compose_date` (`compose_date` ASC))
ENGINE = MyISAM
COMMENT = '日/周报';


-- -----------------------------------------------------
-- Table `zzwork`.`scheduler_report_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`scheduler_report_event` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `event_id` INT(20) NOT NULL,
  `report_id` INT(20) NOT NULL,
  `gmt_created` VARCHAR(45) NULL,
  `gmt_modified` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `zzwork`.`attendance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`attendance` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '姓名',
  `code` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '考勤表的登记号码',
  `account` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '系统中的账户',
  `gmt_work` DATETIME NOT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `schedule_id` INT(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name` ASC),
  INDEX `idx_code` (`code` ASC),
  INDEX `idx_account` (`account` ASC),
  INDEX `idx_gmt_work` (`gmt_work` ASC),
  INDEX `idx_schedule_id` (`schedule_id` ASC))
ENGINE = MyISAM
COMMENT = '考勤原始数据';


-- -----------------------------------------------------
-- Table `zzwork`.`attendance_count`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`attendance_count` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '考勤表的登记号码',
  `name` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '姓名',
  `account` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '系统中的账户',
  `punch0` INT NOT NULL DEFAULT 0 COMMENT '未打卡次数',
  `punch20` INT NOT NULL DEFAULT 0 COMMENT '迟到/早退 20分钟内',
  `punch60` INT NOT NULL DEFAULT 0 COMMENT '迟到/早退 60分钟内',
  `punch_count` INT NOT NULL DEFAULT 0 COMMENT '非正常打卡总次数',
  `gmt_month` DATETIME NOT NULL COMMENT '统计月份',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_code` (`code` ASC),
  INDEX `idx_name` (`name` ASC),
  INDEX `idx_account` (`account` ASC),
  INDEX `idx_gmt_month` (`gmt_month` ASC))
ENGINE = MyISAM
COMMENT = '考勤统计结果';


-- -----------------------------------------------------
-- Table `zzwork`.`attendance_analysis`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`attendance_analysis` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL DEFAULT '',
  `name` VARCHAR(45) NOT NULL DEFAULT '',
  `account` VARCHAR(45) NOT NULL DEFAULT '',
  `day_full` DECIMAL(7,3) NULL DEFAULT 0 COMMENT '当月应出勤天数',
  `day_actual` DECIMAL(7,3) NULL DEFAULT 0 COMMENT '实际出勤天数',
  `day_leave` DECIMAL(7,3) NULL DEFAULT 0 COMMENT '请假天数',
  `day_other` DECIMAL(7,3) NULL DEFAULT 0 COMMENT '其他天数(单位小时)',
  `day_unwork` DECIMAL(7,3) NULL DEFAULT 0 COMMENT '旷工天数',
  `day_unrecord` INT NULL DEFAULT 0 COMMENT '未打卡次数',
  `day_late` INT NULL DEFAULT 0 COMMENT '迟到次数',
  `day_early` INT NULL DEFAULT 0 COMMENT '早退次数',
  `day_overtime` INT NULL DEFAULT 0 COMMENT '加班次数',
  `gmt_target` DATETIME NOT NULL,
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  `schedule_id` INT(20) NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `idx_code` (`code` ASC),
  INDEX `idx_name` (`name` ASC),
  INDEX `idx_account` (`account` ASC),
  INDEX `idx_gmt_target` (`gmt_target` ASC),
  INDEX `idx_schedule_id` (`schedule_id` ASC))
ENGINE = MyISAM
COMMENT = '考勤统计分析结果';


-- -----------------------------------------------------
-- Table `zzwork`.`attendance_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`attendance_schedule` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '班次名称',
  `isuse` TINYINT NOT NULL DEFAULT 1 COMMENT '是否正常使用\n1：正常（默认）\n0：未使用',
  `created_by` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '创建者',
  `modified_by` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '修改人',
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_isuse` (`isuse` ASC),
  INDEX `idx_created_by` (`created_by` ASC),
  INDEX `idx_modified_by` (`modified_by` ASC))
ENGINE = MyISAM
COMMENT = '排班计划';


-- -----------------------------------------------------
-- Table `zzwork`.`attendance_schedule_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zzwork`.`attendance_schedule_detail` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` INT(20) NOT NULL DEFAULT 0 COMMENT '班次ID',
  `gmt_month` DATETIME NOT NULL COMMENT '排班时间，精确到月，例：2013-05-01表示2013年5月的详细排班计划',
  `day_of_year` INT NULL COMMENT '一年中第N天',
  `day_of_month` INT NULL COMMENT '一个月中的第N天',
  `day_of_week` INT NULL COMMENT '一个星期中的第N天',
  `day` DATETIME NULL COMMENT '工作日',
  `workf` INT(20) NULL,
  `workt` INT(20) NULL,
  `unixtime` INT(20) NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `modified_by` VARCHAR(45) NOT NULL,
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_schedule_id` (`schedule_id` ASC),
  INDEX `idx_gmt_month` (`gmt_month` ASC))
ENGINE = MyISAM
COMMENT = '排班计划（详细）';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
